package com.cmit.mvne.billing.rating.gprs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmit.mvne.billing.rating.gprs.common.*;
import com.cmit.mvne.billing.rating.gprs.config.MyBatisPlusConfig;
import com.cmit.mvne.billing.rating.gprs.creditcontrol.CreditControl;
import com.cmit.mvne.billing.rating.gprs.dto.RedoDto;
import com.cmit.mvne.billing.rating.gprs.dto.ReratDto;
import com.cmit.mvne.billing.rating.gprs.service.GprsRatingService;
import com.cmit.mvne.billing.rating.gprs.service.RedoService;
import com.cmit.mvne.billing.rating.gprs.service.ReratService;
import com.cmit.mvne.billing.rating.gprs.service.RollbackRatingService;
import com.cmit.mvne.billing.user.analysis.common.MvneException;
import com.cmit.mvne.billing.user.analysis.dto.QueryCdrGprsDto;
import com.cmit.mvne.billing.user.analysis.dto.QueryCdrGprsErrDto;
import com.cmit.mvne.billing.user.analysis.dto.QueryCdrGprsReratDto;
import com.cmit.mvne.billing.user.analysis.entity.*;
import com.cmit.mvne.billing.user.analysis.service.*;
import com.cmit.mvne.billing.user.analysis.util.ObjectUtil;
import com.cmit.mvne.billing.user.analysis.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.cmit.mvne.billing.rating.gprs.common.RedoStatus.REDO_FAIL_STATUS;
import static com.cmit.mvne.billing.rating.gprs.common.RedoStatus.REDO_SUCCESS_STATUS;
import static com.cmit.mvne.billing.rating.gprs.common.ReratStatus.*;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/5/7 10:38
 */
@Slf4j
@Service
public class RedoServiceImpl implements RedoService {
    @Autowired
    GprsRatingService gprsRatingService;
    @Autowired
    CdrGprsService cdrGprsService;
    @Autowired
    CdrGprsErrorService cdrGprsErrorService;
    @Autowired
    RatingCdrGprsErrorHisService ratingCdrGprsErrorHisService;
    @Autowired
    RatingCdrGprsReratService ratingCdrGprsReratService;
    @Autowired
    RatingCdrGprsReratRecService ratingCdrGprsReratRecService;
    @Autowired
    RollbackRatingService rollbackRatingService;
    @Autowired
    ReratService reratService;
    @Autowired
    private DistributeLock distributeLock;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    CreditControl creditControl;

    @Value(value = "${rating.load.format}")
    String tableFormat;
    @Value(value = "${yellow-mobile.rerat.limit}")
    int limitCon;

    @Override
    public IPage<QueryCdrGprsErrDto> gprsErrForQuery(RedoDto redoDto) {
        int page = redoDto.getPage()==0?1:redoDto.getPage();
        int size = redoDto.getSize()==0?Integer.MAX_VALUE:redoDto.getSize();
        Page<QueryCdrGprsErrDto> pageParam = new Page<>(page, size, true);
        QueryWrapper<QueryCdrGprsErrDto> queryWrapper = new QueryWrapper<QueryCdrGprsErrDto>();
        queryGprsErr(redoDto, queryWrapper);
        return cdrGprsErrorService.redoQuery(pageParam, queryWrapper);
    }

    @Override
    public IPage<CdrGprsError> gprsErrForRedo(RedoDto redoDto) {
        int page = redoDto.getPage()==0?1:redoDto.getPage();
        int size = redoDto.getSize()==0?Integer.MAX_VALUE:redoDto.getSize();
        Page<CdrGprsError> pageParam = new Page<>(page, size, true);
        QueryWrapper<CdrGprsError> queryWrapper = new QueryWrapper<CdrGprsError>();
        queryGprsErr(redoDto, queryWrapper);
        return cdrGprsErrorService.page(pageParam, queryWrapper);
    }

    private <T> void queryGprsErr(RedoDto redoDto, QueryWrapper<T> queryWrapper) {
        log.info("Query gprs err for redo and dto: '{}'", redoDto.toJSONString());

        // 基础参数
        String msisdn = redoDto.getMsisdn();
        Long finishTimeS = redoDto.getFinishTimeS();
        Long finishTimeE = redoDto.getFinishTimeE();
        String originalFile = redoDto.getOriginalFile();
        String errCode = redoDto.getErrCode();
        String redoFlag = (redoDto.getRedoFlag().equals("All") || redoDto.getRedoFlag().equals("all") ? "" : redoDto.getRedoFlag());
        String tableIds = redoDto.getTableIds();
        List<Long> tableIdList = redoDto.getTableIdList();
        int page = redoDto.getPage()==0?1:redoDto.getPage();
        int size = redoDto.getSize()==0?Integer.MAX_VALUE:redoDto.getSize();

        // 转换参数
        Date finishDateS = (finishTimeS==null?null:new Date(finishTimeS*1000));
        Date finishDateE = (finishTimeE==null?null:new Date(finishTimeE*1000));
//        Date finishDateS = (finishTimeS==null?null:new Date(finishTimeS));
//        Date finishDateE = (finishTimeE==null?null:new Date(finishTimeE));
        List<Long> tableIdsList = StringUtil.str2LongLost(tableIds);


        Page<CdrGprsError> pageParam = new Page<>(page, size, true);
        IPage<CdrGprsError> cdrGprsErrorIPage = null;
        /*if (!isQuery) {
            LambdaQueryWrapper<CdrGprsError> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper
                    .eq(ObjectUtil.isNotNullOrBlank(msisdn), CdrGprsError::getMsisdn, msisdn)
                    .ge(ObjectUtil.isNotNullOrBlank(finishDateS), CdrGprsError::getFinishTime, finishDateS)     //大于等于
                    .lt(ObjectUtil.isNotNullOrBlank(finishDateE), CdrGprsError::getFinishTime, finishDateE)    //小于
                    .eq(ObjectUtil.isNotNullOrBlank(originalFile), CdrGprsError::getOriginalFile, originalFile)
                    .eq(ObjectUtil.isNotNullOrBlank(errCode), CdrGprsError::getErrCode, errCode)
                    .eq(ObjectUtil.isNotNullOrBlank(redoFlag), CdrGprsError::getRedoFlag, redoFlag)
                    .in(ObjectUtil.isNotNullOrEmpty(tableIdList), CdrGprsError::getTableId, tableIdList)
                    .in(ObjectUtil.isNotNullOrEmpty(tableIdsList), CdrGprsError::getTableId, tableIdsList);

            cdrGprsErrorIPage = cdrGprsErrorService.page(pageParam, lambdaQueryWrapper);
        } else {*/
            //QueryWrapper<CdrGprsError> queryWrapper = new QueryWrapper<CdrGprsError>();
            queryWrapper.eq(ObjectUtil.isNotNullOrBlank(msisdn), "msisdn", msisdn)
                    .ge(ObjectUtil.isNotNullOrBlank(finishDateS), "finish_time", finishDateS)     //大于等于
                    .lt(ObjectUtil.isNotNullOrBlank(finishDateE), "finish_time", finishDateE)    //小于
                    .eq(ObjectUtil.isNotNullOrBlank(originalFile), "original_file", originalFile)
                    .eq(ObjectUtil.isNotNullOrBlank(errCode), "err_code", errCode)
                    .eq(ObjectUtil.isNotNullOrBlank(redoFlag), "redo_flag", redoFlag)
                    .in(ObjectUtil.isNotNullOrEmpty(tableIdList), "table_id", tableIdList)
                    .in(ObjectUtil.isNotNullOrEmpty(tableIdsList), "table_id", tableIdsList);
            //cdrGprsErrorIPage = cdrGprsErrorService.page(pageParam, queryWrapper);
        //}

        //return cdrGprsErrorIPage;
    }

    @Override
    public IPage<QueryCdrGprsDto> gprsCdrForQuery(ReratDto reratDto) {
        int page = reratDto.getPage()==0?1:reratDto.getPage();
        int size = reratDto.getSize()==0?Integer.MAX_VALUE:reratDto.getSize();
        String tableDate = reratDto.getTableDate();
        Page<QueryCdrGprsDto> pageParam = new Page<>(page, size, true);
        QueryWrapper<QueryCdrGprsDto> queryWrapper = new QueryWrapper<QueryCdrGprsDto>();
        queryGprsCdr(reratDto, queryWrapper);
        MyBatisPlusConfig.tableNameHolder.set(tableDate);
        return cdrGprsService.reratQueryCdr(pageParam, queryWrapper);
    }

    @Override
    public IPage<CdrGprs> gprsCdrForRerat(ReratDto reratDto) {
        int page = reratDto.getPage()==0?1:reratDto.getPage();
        int size = reratDto.getSize()==0?Integer.MAX_VALUE:reratDto.getSize();
        String tableDate = reratDto.getTableDate();
        Page<CdrGprs> pageParam = new Page<>(page, size, true);
        QueryWrapper<CdrGprs> queryWrapper = new QueryWrapper<CdrGprs>();
        queryGprsCdr(reratDto, queryWrapper);
        MyBatisPlusConfig.tableNameHolder.set(tableDate);
        return cdrGprsService.page(pageParam, queryWrapper);
    }

    /*@Override
    public IPage<CdrGprs> queryGprsCdr(ReratDto reratDto) {
        IPage<CdrGprs> cdrGprsErrorIPage = null;
        return cdrGprsErrorIPage;
    }*/

    //@Override
    public <T> void queryGprsCdr(ReratDto reratDto, QueryWrapper<T> queryWrapper) {
        log.info("Query gprs cdr for redat and dto: '{}'", reratDto.toJSONString());

        // 基础参数
        String tableDate = reratDto.getTableDate();
        Long createTimeS = reratDto.getCreateTimeS();
        Long createTimeE = reratDto.getCreateTimeE();
        Long finishTimeS = reratDto.getFinishTimeS();
        Long finishTimeE = reratDto.getFinishTimeE();
        String msisdn = reratDto.getMsisdn();
        Long productId = reratDto.getProductId();
        Long itemId = reratDto.getItemId();
        String reratStatus = reratDto.getReratStatus();
        String ids = reratDto.getIds();
        List<Long> idList = reratDto.getIdList();
        int page = reratDto.getPage()==0?1:reratDto.getPage();
        int size = reratDto.getSize()==0?Integer.MAX_VALUE:reratDto.getSize();

        // 转换参数
        Date createDateS = (createTimeS==null?null:new Date(createTimeS*1000));
        Date createDateE = (createTimeE==null?null:new Date(createTimeE*1000));
        Date finishDateS = (finishTimeS==null?null:new Date(finishTimeS*1000));
        Date finishDateE = (finishTimeE==null?null:new Date(finishTimeE*1000));
        List<Long> idsList = StringUtil.str2LongLost(ids);

        //LambdaQueryWrapper<CdrGprs> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .ge(ObjectUtil.isNotNullOrBlank(createDateS), "create_time", createDateS)
                .le(ObjectUtil.isNotNullOrBlank(createDateE), "create_time", createDateE)
                .ge(ObjectUtil.isNotNullOrBlank(finishDateS), "finish_time", finishDateS)
                .le(ObjectUtil.isNotNullOrBlank(finishDateE), "finish_time", finishDateE)
                .eq(ObjectUtil.isNotNullOrBlank(msisdn), "msisdn", msisdn)
                .eq(ObjectUtil.isNotNullOrBlank(productId), "product_id", productId)
                .eq(ObjectUtil.isNotNullOrBlank(itemId), "item_id", itemId)
                .eq(ObjectUtil.isNotNullOrBlank(reratStatus), "rerat_flag", reratStatus)
                .in(ObjectUtil.isNotNullOrEmpty(idList), "id", idList)
                .in(ObjectUtil.isNotNullOrEmpty(idsList), "id", idsList);

        /*Page<CdrGprs> pageParam = new Page<>(page, size, true);
        MyBatisPlusConfig.tableNameHolder.set(tableDate);
        IPage<CdrGprs> cdrGprsIPage = cdrGprsService.page(pageParam, lambdaQueryWrapper);

        return cdrGprsIPage;*/
    }

    @Override
    public IPage<QueryCdrGprsReratDto> gprsReratForQuery(ReratDto reratDto) {
        int page = reratDto.getPage()==0?1:reratDto.getPage();
        int size = reratDto.getSize()==0?Integer.MAX_VALUE:reratDto.getSize();
        Page<QueryCdrGprsReratDto> pageParam = new Page<>(page, size, true);
        QueryWrapper<QueryCdrGprsReratDto> queryWrapper = new QueryWrapper<QueryCdrGprsReratDto>();
        queryGprsRerat(reratDto, queryWrapper);
        return ratingCdrGprsReratService.reratQueryRerat(pageParam, queryWrapper);
    }

    @Override
    public IPage<RatingCdrGprsRerat> gprsReratForRerat(ReratDto reratDto) {
        int page = reratDto.getPage()==0?1:reratDto.getPage();
        int size = reratDto.getSize()==0?Integer.MAX_VALUE:reratDto.getSize();
        Page<RatingCdrGprsRerat> pageParam = new Page<>(page, size, true);
        QueryWrapper<RatingCdrGprsRerat> queryWrapper = new QueryWrapper<RatingCdrGprsRerat>();
        queryGprsRerat(reratDto, queryWrapper);
        return ratingCdrGprsReratService.page(pageParam, queryWrapper);
    }

    /*@Override
    public IPage<RatingCdrGprsRerat> queryGprsRerat(ReratDto reratDto) {
        IPage<RatingCdrGprsRerat> ratingCdrGprsReratIPage = null;
        return ratingCdrGprsReratIPage;
    }*/

    //@Override
    private <T> void queryGprsRerat(ReratDto reratDto, QueryWrapper<T> queryWrapper) {
        log.info("Query gprs rerat for rerat, and dto: '{}'", reratDto.toJSONString());

        // 基础参数
        String tableDate = reratDto.getTableDate();
        Long createTimeS = reratDto.getCreateTimeS();
        Long createTimeE = reratDto.getCreateTimeE();
        Long finishTimeS = reratDto.getFinishTimeS();
        Long finishTimeE = reratDto.getFinishTimeE();
        String msisdn = reratDto.getMsisdn();
        Long productId = reratDto.getProductId();
        Long itemId = reratDto.getItemId();
        String reratStatus = reratDto.getReratStatus();
        String ids = reratDto.getIds();
        List<Long> idList = reratDto.getIdList();
        int page = reratDto.getPage()==0?1:reratDto.getPage();
        int size = reratDto.getSize()==0?Integer.MAX_VALUE:reratDto.getSize();

        // 转换参数
        Date createDateS = (createTimeS==null?null:new Date(createTimeS*1000));
        Date createDateE = (createTimeE==null?null:new Date(createTimeE*1000));
        Date finishDateS = (finishTimeS==null?null:new Date(finishTimeS*1000));
        Date finishDateE = (finishTimeE==null?null:new Date(finishTimeE*1000));
        List<Long> idsList = StringUtil.str2LongLost(ids);

        //LambdaQueryWrapper<RatingCdrGprsRerat> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .ge(ObjectUtil.isNotNullOrBlank(createDateS), "create_time", createDateS)
                .le(ObjectUtil.isNotNullOrBlank(createDateE), "create_time", createDateE)
                .ge(ObjectUtil.isNotNullOrBlank(finishDateS), "finish_time", finishDateS)
                .le(ObjectUtil.isNotNullOrBlank(finishDateE), "finish_time", finishDateE)
                .eq(ObjectUtil.isNotNullOrBlank(msisdn), "msisdn", msisdn)
                .eq(ObjectUtil.isNotNullOrBlank(productId), "product_id", productId)
                .eq(ObjectUtil.isNotNullOrBlank(itemId), "item_id", itemId)
                .eq(ObjectUtil.isNotNullOrBlank(reratStatus), "rerat_flag", reratStatus)
                .in(ObjectUtil.isNotNullOrEmpty(idList), "id", idList)
                .in(ObjectUtil.isNotNullOrEmpty(idsList), "id", idsList);

        /*Page<RatingCdrGprsRerat> pageParam = new Page<>(page, size, true);
        IPage<RatingCdrGprsRerat> ratingCdrGprsReratIPage = ratingCdrGprsReratService.page(pageParam, lambdaQueryWrapper);

        return ratingCdrGprsReratIPage;*/
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HashMap<String, Integer> redoGprs(List<CdrGprsError> errList) throws MvneException, InterruptedException {
        HashMap<String, Integer> returnMap = redoGprsByList(errList);
        log.info("Redo gprs result: '{}'", returnMap);
        return returnMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HashMap<String, Integer> redoGprs(String whereSql) throws MvneException, InterruptedException {
        String redoSql = "select * from rating_cdr_gprs_error where " + whereSql;
        List<CdrGprsError> errList = cdrGprsErrorService.errRedoSelect(redoSql);

        HashMap<String, Integer> returnMap = redoGprsByList(errList);
        return returnMap;
    }

    protected HashMap<String, Integer> redoGprsByList(List<CdrGprsError> errList) throws MvneException, InterruptedException {
        // 插入历史表
        List<RatingCdrGprsErrorHis> ratingCdrGprsErrorHisList = new ArrayList<>();
        for (CdrGprsError cdrGprsError : errList) {
            if (cdrGprsError.getRedoFlag().equals(REDO_SUCCESS_STATUS)) {
                throw new MvneException("Cannot redo errs which have been redone! Please check!");
            }
            RatingCdrGprsErrorHis ratingCdrGprsErrorHis = new RatingCdrGprsErrorHis();
            BeanUtils.copyProperties(cdrGprsError, ratingCdrGprsErrorHis);
            //ratingCdrGprsErrorHis.setRedoTime(new Date());
            ratingCdrGprsErrorHisList.add(ratingCdrGprsErrorHis);
        }
        ratingCdrGprsErrorHisService.saveBatch(ratingCdrGprsErrorHisList);
        log.info("Insert into err his, errList size is: '{}'", ratingCdrGprsErrorHisList.size());

        HashMap<String, List<CdrGprs>> successMap = new HashMap<>();
        List<CdrGprsError> successCdrs = new ArrayList<>();
        List<CdrGprsError> failCdrs = new ArrayList<>();
        List<CdrGprsError> errorList = new ArrayList<>();
        List<String> expireList = new ArrayList<>();
        List<CreditCallInfo> creditCallInfoList = new ArrayList<>();

        if (errList.size() != 0) {
            log.info("Total num of this errRedo is: {}", errList.size());
            for (CdrGprsError redoCdrGprs : errList) {
                //try {
                CdrGprs cdrGprs = new CdrGprs(redoCdrGprs);
                expireList.add(cdrGprs.getOriginalFile());
                CdrGprsError cdrGprsError = new CdrGprsError(cdrGprs);
                RatingError ratingError = gprsRatingService.ratingGprs(cdrGprs, successMap, errorList, creditCallInfoList, ReadFlag.REDO_FLAG);
                String lockKey = "InfoManageKey:" + cdrGprs.getMsisdn();
                distributeLock.unlock(lockKey);

                redoCdrGprs.setRedoTime(new Date());
                if (ratingError.isNotNull()) {
                    cdrGprsError.setUserId(cdrGprs.getUserId());
                    //cdrGprsError.setFinishTime(new Date());
                    cdrGprsError.setErrCode(ratingError.getErrCode());
                    cdrGprsError.setErrDesc(ratingError.getErrDesc());
                    cdrGprsError.setRedoFlag(REDO_FAIL_STATUS);
                    cdrGprsError.setReratFlag(RERAT_NORMAL_STATUS);
                    Date now = new Date();
                    cdrGprsError.setFinishTime(now);
                    cdrGprsError.setRedoTime(now);
                    log.info("Rating Error, Error Cdr : {} ", cdrGprsError.toJsonString());
                    errorList.add(cdrGprsError);
                    RedoLogWriter.writeRedoLog(cdrGprsError.getOriginalFile(), cdrGprsError.getId(), cdrGprsError.toJsonString());

                    redoCdrGprs.setRedoFlag(REDO_FAIL_STATUS);
                    failCdrs.add(redoCdrGprs);
                } else {
                    redoCdrGprs.setRedoFlag(REDO_SUCCESS_STATUS);
                    successCdrs.add(redoCdrGprs);
                }

            }

            // 详单入库
            loadSuccessCdr(successMap);
            // 更新重处理的错单的状态和redoTime
            updateErrorCdr(successCdrs, failCdrs);
            // 批量发送信控
            try {
                callCreditControl(creditCallInfoList);
            } catch (Exception e) {
                log.error("Call credit info fail!", e);
            }
        }

        // 这里不再设置redolog中话单文件的过期时间，按照之前处理话单文件的过期时间即可

        // 返回一个对象，包含扫描到的错单数，处理成功的话单数，失败话单数
        HashMap<String, Integer> returnMap = new HashMap<String, Integer>();
        returnMap.put("total", errList.size());
        returnMap.put("success", successMap.size());
        returnMap.put("fail", errorList.size());

        return returnMap;
    }

    private void callCreditControl(List<CreditCallInfo> creditCallInfoList) throws MvneException {
        creditControl.callList(creditCallInfoList);
    }

    protected void loadSuccessCdr(HashMap<String, List<CdrGprs>> successMap) {
        log.info("Successful cdrs: {}.", successMap.size());
        for (String s : successMap.keySet()) {
            MyBatisPlusConfig.tableNameHolder.set(s);
            cdrGprsService.saveBatch(successMap.get(s));
        }
        //cdrGprsService.saveBatch(successMap);

    }

    protected void updateErrorCdr(List<CdrGprsError> successTableIds, List<CdrGprsError> failTableIds) {
        log.info("Failed cdrs: {}.", failTableIds.size());

        List<CdrGprsError> updateList = Stream.of(successTableIds, failTableIds).flatMap(Collection::stream).collect(Collectors.toList());
        cdrGprsErrorService.updateBatchById(updateList);
    }

    @Override
    public HashMap<String, Integer> reratGprs(String date, String reratSql) throws MvneException, InterruptedException {
        HashMap<String, Integer> resultMap = new HashMap<>();
        /*List<CdrGprs> cdrGprsList = null;
        *//*do {
            // 根据条件查询出当天对应条件的详单
            // 条件最好有limit，用来对当天的话单分批处理
            cdrGprsList = cdrGprsService.cdrReratSelect(reratSql);
            // 重批会删除详单相应的记录，所以可以直接用原来的limit
            Boolean haveRerat = reratService.reratGprs(date, cdrGprsList);

        } while (cdrGprsList.size()!=0);*//*

        MyBatisPlusConfig.tableNameHolder.set(date);
        if ((cdrGprsList = cdrGprsService.cdrReratSelect(reratSql)).size()!=0) {
            resultMap = reratService.reratGprs(date, cdrGprsList);
        }*/

        return resultMap;
    }

    /**
     * 根据传入的重批表详单id进行重批
     * @param date
     * @param ratingCdrGprsReratList
     * @return
     * @throws MvneException
     * @throws InterruptedException
     */
    @Override
    public HashMap<String, Integer> reratGprs(String date, List<RatingCdrGprsRerat> ratingCdrGprsReratList) throws MvneException, InterruptedException {
        String FAIL = "Fail";
        String SUCCESS = "Success";
        String ERROR = "Error";
        String BALANCE = "BalanceFail";
        String FREERES = "FreeresFail";

        // 初始化resultNap
        HashMap<String, Integer> resultMap = new HashMap<>();
        resultMap.put(FAIL, 0);
        resultMap.put(SUCCESS, 0);
        resultMap.put(ERROR, 0);
        resultMap.put(BALANCE, 0);
        resultMap.put(FREERES, 0);

        // 从重批表查询数据

        int i = 0;
        int size = ratingCdrGprsReratList.size();
        List<RatingCdrGprsRerat> ratingCdrGprsReratLimit = new ArrayList<>();
        log.info("Rerat size: '{}'", size);
        if (ratingCdrGprsReratList.size() != 0) {
            for (RatingCdrGprsRerat ratingCdrGprsRerat : ratingCdrGprsReratList) {
                String reratFlag = ratingCdrGprsRerat.getReratFlag();
                if (reratFlag.equals(RERAT_SUCCESS_STATUS) || reratFlag.equals(RERAT_ERROR_STATUS)) {
                    throw new MvneException("Cannot rerat rerats which have been rerat! Check and move to rec.");
                }
                // 批次处理
                ratingCdrGprsReratLimit.add(ratingCdrGprsRerat);
                int batch = i/limitCon + 1;
                i++;
                log.info("Rerating batch '{}'", batch);
                int mod = i%limitCon;
                if ((mod == 0) || (i == size)) {
                    try {
                        // 返回成功数、错单数、回滚失败数
                        HashMap<String, Integer> batchResultMap = reratService.reratGprs(date, ratingCdrGprsReratLimit);
                        resultMap.put(SUCCESS, resultMap.get(SUCCESS)+batchResultMap.get(SUCCESS));
                        resultMap.put(ERROR, resultMap.get(ERROR)+batchResultMap.get(ERROR));
                        resultMap.put(BALANCE, resultMap.get(BALANCE)+batchResultMap.get(BALANCE));
                        resultMap.put(FREERES, resultMap.get(FREERES)+batchResultMap.get(FREERES));
                        // 批处理后，清空list
                        ratingCdrGprsReratLimit = new ArrayList<>();
                    } catch (Exception e) {
                        // 事务失败，1000条回滚，不会再被处理
                        // 统计失败数
                        log.error("Rerat Fail!", e);
                        resultMap.put(FAIL, resultMap.get(FAIL) + ratingCdrGprsReratLimit.size());
                    }
                }
            }
        }

        for (String s : resultMap.keySet()) {
            if (resultMap.get(s) == null) {
                resultMap.put(s, 0);
            }
        }
        return resultMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean moveGprsCdr(List<CdrGprs> cdrGprsList) {
        if (cdrGprsList.size()==0) {
            return false;
        }

        return moveAndInsert(cdrGprsList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean rerat2rec() {
        LambdaQueryWrapper<RatingCdrGprsRerat> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RatingCdrGprsRerat::getReratFlag, RERAT_SUCCESS_STATUS).or().eq(RatingCdrGprsRerat::getReratFlag, RERAT_ERROR_STATUS);
        List<RatingCdrGprsRerat> ratingCdrGprsReratList = ratingCdrGprsReratService.list(lambdaQueryWrapper);

        List<RatingCdrGprsReratRec> ratingCdrGprsReratRecList = new ArrayList<>();
        List<Long> idList = new ArrayList<>();
        for (RatingCdrGprsRerat ratingCdrGprsRerat : ratingCdrGprsReratList) {
            RatingCdrGprsReratRec ratingCdrGprsReratRec = new RatingCdrGprsReratRec();
            BeanUtils.copyProperties(ratingCdrGprsRerat, ratingCdrGprsReratRec);
            ratingCdrGprsReratRecList.add(ratingCdrGprsReratRec);
            idList.add(ratingCdrGprsRerat.getId());
        }

        Boolean haveInsered = ratingCdrGprsReratRecService.saveBatch(ratingCdrGprsReratRecList);
        if (haveInsered) {
            log.info("Moved cdr to rerat record!");
            if (idList.size()==0) {
                return true;
            }
            return ratingCdrGprsReratService.removeByIds(idList);
        }

        return false;
    }

    private Boolean moveAndInsert(List<CdrGprs> cdrGprsList) {
        List<RatingCdrGprsRerat> ratingCdrGprsReratList = new ArrayList<>();
        for (CdrGprs cdrGprs : cdrGprsList) {
            RatingCdrGprsRerat ratingCdrGprsRerat = new RatingCdrGprsRerat();
            BeanUtils.copyProperties(cdrGprs, ratingCdrGprsRerat);
            // 恢复为可重批状态
            ratingCdrGprsRerat.setReratFlag(RERAT_TO_BE_RERAT);
            ratingCdrGprsReratList.add(ratingCdrGprsRerat);
        }
        // 插入重批表
        Boolean haveInserted = ratingCdrGprsReratService.saveBatch(ratingCdrGprsReratList);
        List<Long> ids = cdrGprsList.stream().map(CdrGprs::getId).collect(Collectors.toList());

        // 如果插入成功，删除原表详单
        if (haveInserted) {
            return cdrGprsService.removeByIds(ids);
        }

        return false;
    }

}
