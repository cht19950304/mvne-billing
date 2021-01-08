package com.cmit.mvne.billing.rating.gprs.service.impl;

import com.cmit.mvne.billing.rating.gprs.common.*;
import com.cmit.mvne.billing.rating.gprs.config.MyBatisPlusConfig;
import com.cmit.mvne.billing.rating.gprs.creditcontrol.CreditControl;
import com.cmit.mvne.billing.rating.gprs.service.GprsRatingService;
import com.cmit.mvne.billing.rating.gprs.service.ReratService;
import com.cmit.mvne.billing.rating.gprs.service.RollbackRatingService;
import com.cmit.mvne.billing.user.analysis.common.MvneException;
import com.cmit.mvne.billing.user.analysis.entity.CdrGprs;
import com.cmit.mvne.billing.user.analysis.entity.CdrGprsError;
import com.cmit.mvne.billing.user.analysis.entity.RatingCdrGprsRerat;
import com.cmit.mvne.billing.user.analysis.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.cmit.mvne.billing.rating.gprs.common.ReratStatus.RERAT_ERROR_STATUS;
import static com.cmit.mvne.billing.rating.gprs.common.ReratStatus.RERAT_SUCCESS_STATUS;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/5/18 11:03
 */

@Slf4j
@Service
public class ReratServiceImpl implements ReratService {
    @Autowired
    GprsRatingService gprsRatingService;
    @Autowired
    CdrGprsService cdrGprsService;
    @Autowired
    CdrGprsErrorService cdrGprsErrorService;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HashMap<String, Integer> reratGprs(String date, List<RatingCdrGprsRerat> ratingCdrGprsReratList) throws MvneException, InterruptedException {
        String FAIL = "Fail";
        String SUCCESS = "Success";
        String ERROR = "Error";
        String BALANCE = "BalanceFail";
        String FREERES = "FreeresFail";
        HashMap<String, Integer> resultMap = new HashMap<>();
        resultMap.put(FAIL, 0);
        resultMap.put(SUCCESS, 0);
        resultMap.put(ERROR, 0);
        resultMap.put(BALANCE, 0);
        resultMap.put(FREERES, 0);

        // 从重批表中批量读取话单，恢复成话单批价前的状态。注意恢复话单的时候也可能会有错单
        // 注意这里返回的是成功回退的，回退失败的已经修改过状态了
        List<RatingCdrGprsRerat> ratingCdrGprsReratListAfterRollback = rollbackRatingService.rollbackGprs(ratingCdrGprsReratList, resultMap);

        // 事务重批
        if (ratingCdrGprsReratListAfterRollback.size()!=0) {
            Boolean haveRerat = ratGprs(ratingCdrGprsReratListAfterRollback, resultMap);
        }
        return resultMap;

    }

    public Boolean ratGprs(List<RatingCdrGprsRerat> cdrGprsListAfterRollback, HashMap<String, Integer> resultMap) throws MvneException, InterruptedException {
        String SUCCESS = "Success";
        String ERROR = "Error";

        HashMap<String, List<CdrGprs>> successMap = new HashMap<>();
        List<CdrGprsError> errorList = new ArrayList<>();
        List<String> expireList = new ArrayList<>();
        List<CreditCallInfo> creditCallInfoList = new ArrayList<>();

        for (RatingCdrGprsRerat ratingCdrGprsRerat : cdrGprsListAfterRollback) {
            CdrGprs cdrGprs = new CdrGprs();
            BeanUtils.copyProperties(ratingCdrGprsRerat, cdrGprs);

            expireList.add(ratingCdrGprsRerat.getOriginalFile());

            // 将fee2设置为重批前的价格
            RatingError ratingError = gprsRatingService.ratingGprs(cdrGprs, successMap, errorList, creditCallInfoList, ReadFlag.RERAT_FLAG);
            // 批价后，将fee2的值设置为新的
            ratingCdrGprsRerat.setFee2(cdrGprs.getFee1());
            // 回退成功的，设置重批时间
            ratingCdrGprsRerat.setReratTime(new Date());
            String lockKey = "InfoManageKey:" + ratingCdrGprsRerat.getMsisdn();
            distributeLock.unlock(lockKey);

            if (ratingError.isNotNull()) {
                // 回写重批表的状态为重批错单
                ratingCdrGprsRerat.setReratFlag(RERAT_ERROR_STATUS);

                CdrGprsError cdrGprsError = new CdrGprsError(cdrGprs);
                // 代码可以捕捉到的业务或处理异常
                cdrGprsError.setUserId(ratingCdrGprsRerat.getUserId());
                //cdrGprsError.setFinishTime(new Date());
                cdrGprsError.setErrCode(ratingError.getErrCode());
                cdrGprsError.setErrDesc(ratingError.getErrDesc());
                cdrGprsError.setReratFlag(RERAT_ERROR_STATUS);
                cdrGprsError.setFinishTime(new Date());
                log.info("Rating Error, Error Cdr : {} ", cdrGprsError.toJsonString());
                errorList.add(cdrGprsError);
                RedoLogWriter.writeRedoLog(cdrGprsError.getOriginalFile(), cdrGprsError.getId(), cdrGprsError.toJsonString());
            } else {
                // 回写重批表的状态为重批成功
                ratingCdrGprsRerat.setReratFlag(RERAT_SUCCESS_STATUS);
                ratingCdrGprsRerat.setReratTime(new Date());
            }
        }

        // 详单和错单入库（写入了reratTime），更新重批表状态
        loadSuccessCdr(successMap);
        loadFailedCdr(errorList);
        ratingCdrGprsReratService.updateBatchById(cdrGprsListAfterRollback);
        // 批量发送信控
        try {
            callCreditControl(creditCallInfoList);
        } catch (Exception e) {
            log.error("Call credit info fail!", e);
        }

        int successCdr = 0;
        for (String s : successMap.keySet()) {
            successCdr+= successMap.get(s).size();
        }
        resultMap.put(SUCCESS, successCdr);
        resultMap.put(ERROR, errorList.size());

        return true;
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

    }

    protected void loadFailedCdr(List<CdrGprsError> errorList) {
        log.info("Failed cdrs: {}.", errorList.size());
        cdrGprsErrorService.saveBatch(errorList);
    }
}
