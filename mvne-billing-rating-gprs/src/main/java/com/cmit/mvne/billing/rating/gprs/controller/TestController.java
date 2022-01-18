package com.cmit.mvne.billing.rating.gprs.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cmit.mvne.billing.rating.gprs.common.DistributeLock;
import com.cmit.mvne.billing.rating.gprs.common.Measures;
import com.cmit.mvne.billing.rating.gprs.creditcontrol.CreditControl;
import com.cmit.mvne.billing.rating.gprs.creditcontrol.CreditControlOperation;
import com.cmit.mvne.billing.rating.gprs.creditcontrol.CreditControlReason;
import com.cmit.mvne.billing.rating.gprs.creditcontrol.SmsGatewayDto;
import com.cmit.mvne.billing.rating.gprs.dto.ProductInfo;
import com.cmit.mvne.billing.rating.gprs.config.MyBatisPlusConfig;
import com.cmit.mvne.billing.rating.gprs.dto.RedoDto;
import com.cmit.mvne.billing.rating.gprs.dto.ReratDto;
import com.cmit.mvne.billing.rating.gprs.remote.TestClient;
import com.cmit.mvne.billing.rating.gprs.remote.service.CreditControlService;
import com.cmit.mvne.billing.rating.gprs.service.ProcessCdrService;
import com.cmit.mvne.billing.rating.gprs.service.ProductManagementService;
import com.cmit.mvne.billing.rating.gprs.service.RedoService;
import com.cmit.mvne.billing.rating.gprs.service.ReratService;
import com.cmit.mvne.billing.rating.gprs.util.DateUtils;
import com.cmit.mvne.billing.rating.gprs.util.MeasureExchangeUtils;
import com.cmit.mvne.billing.user.analysis.common.MvneException;
import com.cmit.mvne.billing.user.analysis.dto.QueryCdrGprsReratDto;
import com.cmit.mvne.billing.user.analysis.entity.*;
import com.cmit.mvne.billing.user.analysis.mapper.CdrGprsErrorMapper;
import com.cmit.mvne.billing.user.analysis.mapper.CmUserDetailMapper;
import com.cmit.mvne.billing.user.analysis.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2019/12/31 14:21
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {
    // 把所有操作符限制了
    static String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"
            + "(\\b(select|update|and|or|delete|insert|trancate|char|desc|show|alter|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";

    static Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);//表示忽略大小写

    @Autowired
    ProcessCdrService processCdrService;
    @Autowired
    CdrGprsService cdrGprsService;
    @Autowired
    CdrGprsErrorService cdrGprsErrorService;
    @Autowired
    CdrGprsErrorMapper cdrGprsErrorMapper;
    @Autowired
    PmProductService pmProductService;
    @Autowired
    CmUserDetailMapper cmUserDetailMapper;
    @Autowired
    ProductManagementService productManagementService;

    @Autowired
    TestClient testClient;
    @Autowired
    DistributeLock distributeLock;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    CountryOperatorService countryOperatorService;
    @Autowired
    RatingRateService ratingRateService;
    @Autowired
    SysRoamZoneGroupService sysRoamZoneGroupService;
    @Autowired
    SysMeasureUnitExchangeService sysMeasureUnitExchangeService;
    @Autowired
    ApsFreeResService apsFreeResService;
    @Autowired
    ApsBalanceFeeService apsBalanceFeeService;
    @Autowired
    CreditControlService creditControlService;
    @Autowired
    CmProdInsInfoService cmProdInsInfoService;
    @Autowired
    ReratService reratService;
    @Autowired
    RedoService redoService;
    @Autowired
    MyBatisPlusConfig myBatisPlusConfig;
    @Autowired
    SysSmsModelService sysSmsModelService;
    @Autowired
    CreditControl creditControl;
    @Autowired
    RatingCdrGprsReratService ratingCdrGprsReratService;

    private volatile static int i = 0;
    private Queue<String> waitQueue = new LinkedList<String>();

    @RequestMapping(value = "/mysql", method = RequestMethod.GET)
    String mysql() {
        LambdaQueryWrapper<PmProduct> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PmProduct::getProductOfferingId, 101700062);
        PmProduct pmProduct = pmProductService.getOne(lambdaQueryWrapper);

        return "hello";
    }

    @RequestMapping(value = "/mpp5", method = RequestMethod.GET)
    IPage<RatingCdrGprsRerat> mpp5() {
        ReratDto reratDto = new ReratDto();
        reratDto.setPage(1);
        reratDto.setSize(10);
        reratDto.setIds("62");
        
        return redoService.gprsReratForRerat(reratDto);
    }

    @RequestMapping(value = "/mpp4", method = RequestMethod.GET)
    IPage<CdrGprs> mpp4() {
        ReratDto reratDto = new ReratDto();
        reratDto.setPage(1);
        reratDto.setSize(10);
        reratDto.setTableDate("20200325");
        reratDto.setIds("1,4,5");

        return redoService.gprsCdrForRerat(reratDto);
    }

    @RequestMapping(value = "/mpp3", method = RequestMethod.GET)
    IPage<CdrGprsError> mpp3() {
        RedoDto redoDto = new RedoDto();
        redoDto.setPage(1);
        redoDto.setSize(10);
        redoDto.setTableIds("7,8");

        return redoService.gprsErrForRedo(redoDto);
    }

    @RequestMapping(value = "/mpp2", method = RequestMethod.GET)
    IPage<QueryCdrGprsReratDto> mpp2() {
        QueryWrapper<QueryCdrGprsReratDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(true, "id", 62);
        Page<QueryCdrGprsReratDto> pageParam = new Page<>(1, 10, true);
        IPage<QueryCdrGprsReratDto> queryCdrGprsReratDtoIPage = ratingCdrGprsReratService.reratQueryRerat(pageParam, queryWrapper);

        return queryCdrGprsReratDtoIPage;
    }

    /*@RequestMapping(value = "/mpp1", method = RequestMethod.GET)
    IPage<QueryCdrGprsDto> mpp1() {
        LambdaQueryWrapper<QueryCdrGprsDto> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        Page<QueryCdrGprsDto> pageParam = new Page<>(1, 10, true);
        MyBatisPlusConfig.tableNameHolder.set("20200325");
        IPage<QueryCdrGprsDto> queryCdrGprsDtoIPage = cdrGprsService.reratQueryCdr(pageParam, lambdaQueryWrapper);

        return queryCdrGprsDtoIPage;
    }*/

    /*@RequestMapping(value = "/mpp", method = RequestMethod.GET)
    IPage<QueryCdrGprsErrDto> mpp() {
        LambdaQueryWrapper<QueryCdrGprsErrDto> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        Page<QueryCdrGprsErrDto> pageParam = new Page<>(1, 10, true);
        IPage<QueryCdrGprsErrDto> queryCdrGprsErrDtoIPage = cdrGprsErrorService.redoQuery(pageParam, lambdaQueryWrapper);

        return queryCdrGprsErrDtoIPage;
    }*/

    @RequestMapping(value = "/select", method = RequestMethod.GET)
    List<SysSmsModel> select() {
        LambdaQueryWrapper<SysSmsModel> queryWrapper = new QueryWrapper<SysSmsModel>().lambda();
        Predicate<TableFieldInfo> predicate = test -> test.getColumn().equals("id") || test.getColumn().equals("text");
        queryWrapper.select(SysSmsModel.class, predicate);

        List<SysSmsModel> sysSmsModelList = sysSmsModelService.list(queryWrapper);
        return sysSmsModelList;
    }

    @RequestMapping(value = "/dou", method = RequestMethod.GET)
    String dou(@RequestParam(value = "str") String str) {
        return str;
    }

    @RequestMapping(value = "/nec", method = RequestMethod.GET)
    BigDecimal nec(@RequestParam(value = "num") BigDecimal num) {
        return MeasureExchangeUtils.exchangeCeiling(num, Measures.B, Measures.B, 2);
    }

    @RequestMapping(value = "/nec2", method = RequestMethod.GET)
    BigDecimal nec2(@RequestParam(value = "num") BigDecimal num) {
        return MeasureExchangeUtils.exchangeFloor(num, Measures.B, Measures.B, 2);
    }

    @RequestMapping(value = "/injection", method = RequestMethod.GET)
    Boolean injection(@RequestParam(value = "whereSql") String whereSql) {
        Matcher matcher = sqlPattern.matcher(whereSql);
        if (matcher.find()) {
            System.out.println("参数存在非法字符，请确认："+matcher.group());//获取非法字符：or
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/smsCache", method = RequestMethod.GET)
    String smsCache() {
        return creditControl.getText("0", "12");
    }

    @RequestMapping(value = "/version", method = RequestMethod.GET)
    Boolean version() {
        SysSmsModel sysSmsModel = new SysSmsModel();
        sysSmsModel.setId(1);
        LambdaUpdateWrapper<SysSmsModel> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SysSmsModel::getId, 1);
        lambdaUpdateWrapper.set(SysSmsModel::getOperation, "555");
        Boolean is = sysSmsModelService.update(sysSmsModel, lambdaUpdateWrapper);

        return is;
    }

    /*@RequestMapping(value = "/export", method = RequestMethod.GET)
    void export(HttpServletResponse response) throws IOException {
        List<CdrGprs> cdrGprsList = redoService.queryGprsCdr("20200325", null, null, null, null, "", null, null, Stream.of(21L, 22L).collect(Collectors.toList()));

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("test.xlsx", "UTF-8");
        String str="attachment;filename=" + new String(fileName.getBytes("GBK"),"iso-8859-1");
        response.setContentType("application/vnd.ms-excel; charset=GBK");
        response.addHeader("Content-disposition", str);
        EasyExcel.write(response.getOutputStream(), CdrGprs.class).sheet("test").doWrite(cdrGprsList);

    }*/

    /*@RequestMapping(value = "/excel", method = RequestMethod.GET)
    void excel() {
        String fileName = "D:\\file\\test.xlsx";
        List<CdrGprs> cdrGprsList = redoService.queryGprsCdr("20200325", null, null, null, null, "", null, null, Stream.of(21L, 22L).collect(Collectors.toList()));
        EasyExcel.write(fileName, CdrGprs.class).sheet("test").doWrite(cdrGprsList);
    }*/

    @RequestMapping(value = "/single", method = RequestMethod.GET)
    void casinglecheOrder() {
        //myBatisPlusConfig.setParam("SS");
    }

    CmProdInsInfo getProdINsInfo(Long userId, Date eventTimestamp) {
        List<CmProdInsInfo> cmProdInsInfoList = cmProdInsInfoService.findAllByUserId(userId);
        if (cmProdInsInfoList.size() == 0) {
            return null;
        }

        cmProdInsInfoList.sort(new Comparator<CmProdInsInfo>() {
            @Override
            public int compare(CmProdInsInfo o1, CmProdInsInfo o2) {
                return o2.getValidDate().compareTo(o1.getValidDate());
            }
        });

        CmProdInsInfo cmProdInsInfo = cmProdInsInfoList.get(0);
        if ((eventTimestamp.compareTo(cmProdInsInfo.getValidDate()) >= 0) && (eventTimestamp.compareTo(cmProdInsInfo.getExpireDate()) < 0)) {
            return cmProdInsInfo;
        }

        return null;
    }

    @RequestMapping(value = "/cacheOrder", method = RequestMethod.GET)
    CmProdInsInfo cacheOrder(@RequestParam(value = "userId") Long userId) {
        return getProdINsInfo(userId, new Date());
    }

    @RequestMapping(value = "/credit", method = RequestMethod.GET)
    void credit() {
        String operation = CreditControlOperation.STOP_OPERATION;
        String reason = CreditControlReason.LOW_BALANCE_REASON;
        String text = "Hello!";
        SmsGatewayDto smsGatewayDto = new SmsGatewayDto("17050000000", text, operation, reason);
        try {
            creditControlService.sms(smsGatewayDto);
        } catch (MvneException e) {
            System.out.println("Credit control failed");
        }
    }

    @RequestMapping(value = "/opmLock", method = RequestMethod.GET)
    @Transactional
    ApsFreeRes opmLock() {
        ApsFreeRes apsFreeRes = apsFreeResService.selectByKey(100000000L, 1001L, 66020001L);
        System.out.println(apsFreeRes);
        apsFreeRes.setUsedValue(new BigDecimal(100));
        apsFreeRes.setValidDate(new Date());
        System.out.println(apsFreeRes);
        LambdaUpdateWrapper<ApsFreeRes> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ApsFreeRes::getUserId, apsFreeRes.getUserId())
                .eq(ApsFreeRes::getProductInsId, apsFreeRes.getProductInsId())
                .eq(ApsFreeRes::getItemId, apsFreeRes.getItemId());
        Boolean updateSuccess = apsFreeResService.update(apsFreeRes, lambdaUpdateWrapper);
        if (updateSuccess) {
            return apsFreeResService.selectByKey(100000000L, 1001L, 66020001L);
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/forUpdate", method = RequestMethod.GET)
    ApsBalanceFee forUpdate() {
        return apsBalanceFeeService.selectByUserId(100L);
    }

    @RequestMapping(value = "/operatorTest", method = RequestMethod.GET)
    CountryOperator operatorTest(@RequestParam(value="operator") String operator) {
        return countryOperatorService.selectByOperator(operator);
    }

    @Cacheable(value = "cm_user_detail", key = "#p0", cacheManager = "localCaffeineCacheManager")
    public CmUserDetail selectCmUserDetail(Long userId) {
        CmUserDetail cmUserDetail = cmUserDetailMapper.selectById(userId);
        System.out.println(cmUserDetail);
        return cmUserDetail;
    }

    @RequestMapping(value = "/cacheTest", method = RequestMethod.GET)
    void cacheTest(@RequestParam(value="userId") Long userId) {
        selectCmUserDetail(userId);
        System.out.println("1");
    }

    @RequestMapping(value = "/cache", method = RequestMethod.GET)
    String cache() {
        /*pmProductService.cacheAll();
        ratingRateService.cacheAll();
        countryOperatorService.cacheAll();
        sysRoamZoneGroupService.cacheAll();
        sysMeasureUnitExchangeService.cacheAll();*/
        return "success";
    }

    @RequestMapping(value = "/fromCache", method = RequestMethod.GET)
    void fromCache() {
        System.out.println("first");
        pmProductService.select(101700004L);
        System.out.println("second");
        pmProductService.select(101700004L);
    }

    @RequestMapping(value = "/sql", method = RequestMethod.GET)
    String sql() {
        List<CdrGprsError> errList = cdrGprsErrorService.errRedoSelect("select * from rating_cdr_gprs_error where event_duration=99 for update");
        if (errList!=null) {
            return errList.get(0).toJsonString();
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/json", method = RequestMethod.GET)
    String json() {
        ProductInfo productInfo = new ProductInfo("1111");
        return JSONObject.toJSONString(productInfo);
    }

    @RequestMapping(value = "/redis", method = RequestMethod.GET)
    Long redis() {
        Long size = redisTemplate.opsForList().size("UserDetail:3540000004");
        return size;
    }

    @RequestMapping(value = "/sentinel", method = RequestMethod.GET)
    String sentinel() {
        redisTemplate.opsForHash().put("mytest", "sentinel", "test");
        return "hello";
    }

    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    void insert() {
        /*LambdaQueryWrapper<CdrGprs> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CdrGprs::getId, 11);*/
        List<CdrGprs> cdrGprsList = new ArrayList<>();
        CdrGprs cdrGprs = new CdrGprs();
        cdrGprs.setId(1L);
        cdrGprs.setEventDuration(1);
        cdrGprs.setImsi("1");
        cdrGprs.setMsisdn("111");
        cdrGprs.setOperatorCode("SB");
        cdrGprs.setRecordType("GP");
        cdrGprs.setSource("1");
        cdrGprs.setFinishTime(new Date());
        cdrGprsList.add(cdrGprs);

        cdrGprsService.saveBatch(cdrGprsList);
    }

    @RequestMapping(value = "/remote", method = RequestMethod.GET)
    void remote() {
        String test = testClient.test();
        System.out.println(test);
    }

    @RequestMapping(value = "/lock", method = RequestMethod.GET)
    void lock() throws MvneException {
        String lockKey = "InfoManageKey:1700000188";
        boolean acquiredLock = distributeLock.fairLock(lockKey, TimeUnit.SECONDS, 60, 60);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        return "go";
    }

    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    String test2(@RequestBody String s) {
        return "hello "+s+"!";
    }

    @RequestMapping(value = "/zone", method = RequestMethod.POST)
    void testZone() {
        Date date = new Date(2020, 2 ,14, 18, 00, 00);
        String UTCoffset = "+08";
        DateUtils.getLocalEventTimestamp(date, UTCoffset);
    }

    @RequestMapping(value = "/volatile", method = RequestMethod.POST)
    boolean testVolatile() throws MvneException, InterruptedException {
        Queue<String> cdrQueue = new LinkedList<String>();

        cdrQueue.offer("1");
        cdrQueue.offer("1");
        cdrQueue.offer("2");
        cdrQueue.offer("3");
        cdrQueue.offer("2");
        cdrQueue.offer("4");
        cdrQueue.offer("1");
        cdrQueue.offer("2");
        cdrQueue.offer("1");
        int size = cdrQueue.size();

        System.out.println("StartDate:" + LocalDateTime.now());
        while (!cdrQueue.isEmpty()) {
            String cdr = cdrQueue.poll();
            processCdrService.dealCdr(cdr, waitQueue);
        }

        while (i != size) {
            for (int j=0; j<waitQueue.size(); j++) {
                processCdrService.dealCdr(waitQueue.poll(), waitQueue);
            }
        }

        System.out.println("finishDate:" + LocalDateTime.now());

        return true;
    }

    public static void callBack() {
        i++;
    }

    public static void pri() {
        System.out.println("Hello:" + i);
    }
}
