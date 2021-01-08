package com.cmit.mvne.billing.rating.gprs.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cmit.mvne.billing.rating.gprs.common.*;
import com.cmit.mvne.billing.rating.gprs.config.SystemConfiguration;
import com.cmit.mvne.billing.rating.gprs.creditcontrol.CreditControl;
import com.cmit.mvne.billing.rating.gprs.creditcontrol.CreditInfo;
import com.cmit.mvne.billing.rating.gprs.service.GprsRatingService;
import com.cmit.mvne.billing.rating.gprs.util.DateUtils;
import com.cmit.mvne.billing.user.analysis.common.MvneException;
import com.cmit.mvne.billing.user.analysis.entity.*;
import com.cmit.mvne.billing.user.analysis.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.cmit.mvne.billing.rating.gprs.common.RateTypeConstant.*;
import static com.cmit.mvne.billing.rating.gprs.common.RatingErrorConstant.*;
import static com.cmit.mvne.billing.rating.gprs.common.ReadFlag.*;
import static com.cmit.mvne.billing.rating.gprs.common.RedoStatus.REDO_NORMAL_STATUS;
import static com.cmit.mvne.billing.rating.gprs.common.RedoStatus.REDO_SUCCESS_STATUS;
import static com.cmit.mvne.billing.rating.gprs.common.ReratStatus.RERAT_NORMAL_STATUS;
import static com.cmit.mvne.billing.rating.gprs.common.ReratStatus.RERAT_SUCCESS_STATUS;
import static com.cmit.mvne.billing.rating.gprs.common.UserStatusConstant.NORMAL_STATUS;

/**
 * @author caikunchi
 * @since 2020/3/2
 */
@Slf4j
@Service
public class GprsRatingServiceImpl implements GprsRatingService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private DistributeLock distributeLock;
    @Autowired
    private RatingRateService ratingRateService;
    @Autowired
    private SysMeasureUnitExchangeService sysMeasureUnitExchangeService;
    @Autowired
    private PmProductService pmProductService;
    @Autowired
    private SysRoamZoneGroupService sysRoamZoneGroupService;
    @Autowired
    private BdOperatorCodeService bdOperatorCodeService;
    @Autowired
    SystemConfiguration systemConfiguration;
    @Autowired
    CmUserDetailService cmUserDetailService;
    @Autowired
    CmProdInsInfoService cmProdInsInfoService;
    @Autowired
    ApsFreeResService apsFreeResService;
    @Autowired
    ApsBalanceFeeService apsBalanceFeeService;
    @Autowired
    CreditControl creditControl;

    @Value(value = "${yellow-mobile.redis.redisson.wait-time}")
    int waitTime;
    @Value(value = "${yellow-mobile.redis.redisson.lease-time}")
    int leaseTime;
    @Value(value = "${yellow-mobile.redis.redisson.try-interval}")
    int tryInterval;
    @Value(value = "${yellow-mobile.redis.redisson.try-times}")
    int tryTimes;

    final int RETRY_TIME = 3;

    @Override
    //@Transactional(rollbackFor = Exception.class)
    //@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public RatingError ratingGprs(CdrGprs cdrGprs, HashMap<String, List<CdrGprs>> successMap, List<CdrGprsError> errorList, List<CreditCallInfo> creditCallInfoList, String readFlag) throws MvneException, InterruptedException {
        CreditInfo creditInfo = new CreditInfo();

        // 初始化
        String errCode = null;
        String errDesc = null;
        // 产生了rating_error，就需要打错单！！
        RatingError ratingError = new RatingError();

        BigDecimal ratingValue = new BigDecimal("0");
        BigDecimal deductFreeres = new BigDecimal("0");
        BigDecimal ratingFee = new BigDecimal("0");

        RateType rateType = new RateType();
        rateType.setRateType(0);
        // 汇总类型：1只扣免费资源；2免费资源用完，扣余额；3扣余额
        Integer sumType = 0;

        String freeResKey = "";
        String balanceKey = "";

        ApsFreeRes apsFreeRes = null;
        ApsBalanceFee apsBalanceFee = null;

        log.info("rating gprs cdr , cdr：{}", cdrGprs);
        Long id = cdrGprs.getId();

        // 获取计费要素
        String msisdn = cdrGprs.getMsisdn();
        String imsi = cdrGprs.getImsi();
        String recordType = cdrGprs.getRecordType();

        Date eventTimestamp = cdrGprs.getLocalEventTimeStamp();
        BigDecimal downloadVol = cdrGprs.getDownloadVol()==null ? new BigDecimal(0):cdrGprs.getDownloadVol();
        BigDecimal uploadVol = cdrGprs.getUploadVol()==null ? new BigDecimal(0):cdrGprs.getUploadVol();
        BigDecimal totalVol = downloadVol.add(uploadVol);
        String operatorCode = cdrGprs.getOperatorCode();
        String lockKey = "InfoManageKey:" + msisdn;
        String cdrJson = null;

        // 判断话单是否已经批过价
        // 如果未批价，则正常批价
        //try {
            //creditControl.init();
            // 如果是错单重处理或重批，则不会从redolog中再查
            // readFlag=true，则从redis查，否则直接批
            if (readFlag.equals(RATING_FLAG)) {
                cdrJson = getRatedCdr(cdrGprs.getOriginalFile(), cdrGprs.getId());
            } else {
                cdrJson = null;
            }
            if (cdrJson == null) {
                int retry = tryTimes;
                while (retry-- > 0) {
                    // 尝试对号码加公平锁，如果无法加锁，会放到队列后面

                    //boolean acquiredLock = distributeLock.fairLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);
                    boolean acquiredLock = true;
                    if (acquiredLock) {
                        log.info("Rating Cdr: {}, Acquired lock:{} !", cdrGprs.getMsisdn(), lockKey);
                        // 查询计费科目
                        Long eventItemId = getPmEventItem();
                        cdrGprs.setItemId(eventItemId);
                        // 获取话单时间的用户信息
                        CmUserDetail cmUserDetail = getUserDetail(msisdn, eventTimestamp);
                        Long productId = 999999999L;
                        if (null != cmUserDetail) {
                            log.info("Rating userInfo: {}", cmUserDetail.toJsonString());
                            Long userId = cmUserDetail.getUserId();
                            cdrGprs.setUserId(userId);
                            balanceKey = "BalanceFee:" + userId;
                            // 用户可能没订购产品
                            CmProdInsInfo cmProdInsInfo = getProdInsInfo(userId, eventTimestamp);
                            // 判断适用区域
                            // 如果产品使用区域与漫游区域一致，一致则必有产品
                            // 如果operatorCode为空且是本地流量，则默认为simmin
                            if ("GP".equals(recordType) && (("".equals(operatorCode)) || (operatorCode==null))) {
                                operatorCode = "AAMOW";
                            }

                            // do-while循环里面的就是批价代码，也就是修改balance和freeres的部分
                            Boolean sumResult = false;
                            // 初始化
                            int retrySum = 0;
                            do {
                                apsBalanceFee = getBalanceFee(userId);
                                if (null == apsBalanceFee) {
                                    log.error("Cannot find balance fee by userid:'{}'", userId);
                                    return new RatingError(CANNOT_FIND_BALANCE_CODE, CANNOT_FIND_BALANCE_DESC);
                                }
                                if (isAppliedGroup(recordType, cmProdInsInfo, operatorCode, ratingError, rateType)) {
                                    productId = cmProdInsInfo.getProductId();
                                    cdrGprs.setProductId(productId);
                                    cdrGprs.setProductInsId(cmProdInsInfo.getProductInsId());
                                    Long productInsId = cmProdInsInfo.getProductInsId();
                                    freeResKey = "FreeRes:" + userId + ":" + productInsId + ":" + eventItemId;
                                    balanceKey = "BalanceFee:" + userId;

                                    // 查询免费资源
                                    apsFreeRes = getFreeRes(userId, productInsId, eventItemId);

                                    if (null != apsFreeRes) {
                                        log.info("Aps freeRes: {}", apsFreeRes.toJsonString());
                                        // 如果免费资源足量，直接扣减，批价量为0
                                        // 总用量=已用量+本次用量
                                        BigDecimal hadUsedValue = apsFreeRes.getUsedValue().add(totalVol);
                                        // 如果总用量小于等于免费资源总量，批价量为0
                                        if (hadUsedValue.compareTo(apsFreeRes.getAmount()) < 1) {
                                            deductFreeres = totalVol;
                                            // 只扣免费资源
                                            sumResult = sum(userId, productInsId, eventItemId, apsFreeRes, totalVol, creditInfo);
                                        } else {
                                            // 包括免费资源用完的情况
                                            // 如果总用量超过了免费资源总量，计算批价量
                                            deductFreeres = apsFreeRes.getAmount().subtract(apsFreeRes.getUsedValue());
                                            ratingValue = hadUsedValue.subtract(apsFreeRes.getAmount());
                                            ratingFee = getRatingFee(productId, eventItemId, ratingValue, ratingError);
                                            if (ratingError.isNotNull()) {
                                                return ratingError;
                                            }
                                            // 免费资源用完，扣余额
                                            sumResult = sum(userId, productInsId, eventItemId, apsFreeRes, ratingFee, apsBalanceFee, creditInfo, rateType);
                                        }
                                    } else {
                                        return new RatingError(CANNOT_FIND_FREERES_CODE, CANNOT_FIND_FREERES_DESC);
                                    }

                                } else {
                                    if (ratingError.isNotNull()) {
                                        return ratingError;
                                    }
                                    // 套餐不适用或者没有订购产品
                                    // 直接计算批价量
                                    if (cmProdInsInfo != null) {
                                        freeResKey = "FreeRes:" + userId + ":" + cmProdInsInfo.getProductInsId() + ":" + eventItemId;
                                        apsFreeRes = getFreeRes(userId, cmProdInsInfo.getProductInsId(), eventItemId);
                                        if (null != apsFreeRes) {
                                            // 因为cmProdInsInfo不为空，所以一定要有免费资源
                                            creditInfo.setLeftFreeRes(apsFreeRes.getAmount().subtract(apsFreeRes.getUsedValue()));
                                        } else {
                                            log.error("Order but not applied, and cannot find free res.");
                                            return new RatingError(CANNOT_FIND_FREERES_CODE, CANNOT_FIND_FREERES_DESC);
                                        }
                                    }
                                    productId = 999999999L;
                                    ratingValue = totalVol;
                                    ratingFee = getRatingFee(productId, eventItemId, ratingValue, ratingError);
                                    if (ratingError.isNotNull()) {
                                        return ratingError;
                                    }
                                    // 只扣余额
                                    sumResult = sum(userId, ratingFee, apsBalanceFee, creditInfo);
                                }
                                if (!sumResult) {
                                    retrySum++;
                                    Thread.sleep(tryInterval);
                                }
                            } while ((retrySum < RETRY_TIME) && !sumResult);
                            // 如果重试了n次都失败，直接抛异常回退，等待下一次扫描
                            if (!sumResult) {
                                log.error("Over retry times! Please ensure the infoManager is running correctlt~");
                                throw new MvneException(OVER_RETRY_SUM_TIME_CODE, OVER_RETRY_SUM_TIME_DESC);
                            }
                            if (ratingError.isNotNull()) {
                                return ratingError;
                            }

                            // 正常流程下，必定扣免费资源或者余额，即信控一定会有值
                            if ((null == creditInfo.getLeftFreeRes()) && (null == creditInfo.getBalance())) {
                                return new RatingError(CANNOT_SUM_BALANCE_CODE, CANNOT_SUM_BALANCE_DESC);
                            } else {
                                // 调用信控方法
                                creditCallInfoList.add(new CreditCallInfo(id, msisdn, rateType.getRateType(), creditInfo));
                                /*try {
                                    creditControl.call(msisdn, rateType.getRateType(), creditInfo);
                                } catch (Exception e) {
                                    // 信控异常，计费不处理，需要保证信控高可用
                                    log.error("Call credit info fail!", e);
                                }*/
//                        cdrGprs = new CdrGprs(cdr, fileName, productId, deductFreeres, ratingValue, ratingFee);

                                // 包装详单
                                cdrGprs.setProductId(productId);
                                cdrGprs.setDeductFreeres(deductFreeres);
                                cdrGprs.setRatingValue(ratingValue);
                                cdrGprs.setFee1(ratingFee);
                                cdrGprs.setFinishTime(new Date());
                                cdrGprs.setUserId(userId);
                                if (readFlag.equals(RATING_FLAG)) {
                                    cdrGprs.setRedoFlag(REDO_NORMAL_STATUS);
                                    cdrGprs.setReratFlag(RERAT_NORMAL_STATUS);
                                } else if (readFlag.equals(REDO_FLAG)) {
                                    cdrGprs.setRedoFlag(REDO_SUCCESS_STATUS);
                                } else if (readFlag.equals(RERAT_FLAG)) {
                                    cdrGprs.setReratTime(new Date());
                                    cdrGprs.setReratFlag(RERAT_SUCCESS_STATUS);
                                    cdrGprs.setReratTime(new Date());
                                }


                                RedoLogWriter.writeRedoLog(cdrGprs.getOriginalFile(), cdrGprs.getId(), cdrGprs.toJsonString());
                                log.info("Cdr after rating:'{}'", cdrGprs.toJsonString());
                                String dateStr = DateUtils.Date2Str(cdrGprs.getLocalEventTimeStamp(), "yyyyMMdd");
                                if (successMap.containsKey(dateStr)) {
                                    successMap.get(dateStr).add(cdrGprs);
                                } else {
                                    List<CdrGprs> cdrGprsList = new ArrayList<>();
                                    cdrGprsList.add(cdrGprs);
                                    successMap.put(dateStr, cdrGprsList);
                                }
                                ratingError.setRatingTime(new Date());
                                return ratingError;
                            }

                        } else {
                            // 无法找到用户关系
                            return new RatingError(NO_USER_INFO_CODE, NO_USER_INFO_DESC);
                        }

                    } else {
                        if (retry < 0) {
                            return new RatingError(ACQUIRE_LOCK_FAILED_CODE, ACQUIRE_LOCK_FAILED_DESC);
                        }
                        Thread.sleep(tryInterval);
                    }

                }

            } else {
                // 如果批过价，入错单表
                // 判断是详单还是错单
                /*if (cdrJson.contains("errCode")) {
                    // 错单
                    errorList.add(JSONObject.parseObject(cdrJson, CdrGprsError.class));
                } else {
                    successMap.add(JSONObject.parseObject(cdrJson, CdrGprs.class));
                }*/

                log.error("Dup cdr id:'{}'! Check the id from current table, date is '{}'!", cdrGprs.getId(), new Date());
                return new RatingError(DUP_CDR_CODE, DUP_CDR_DESC);
            }

        return null;
    }


    private String getRatedCdr(String fileName, Long id) {
        String idS = Long.toString(id);
        String redoKey = "RatingRedo:" + fileName;
        String cdrJson = null;
        Object result = redisTemplate.opsForHash().get(redoKey, idS);
        if(result != null) {
            cdrJson = String.valueOf(result);
        }
        return cdrJson;
    }

    /**
     * !!注意需要抛出异常
     * 只扣免费资源
     * @param apsFreeRes 查询到的免费资源
     * @param totalVol 本次扣除量
     */
    private Boolean sum(Long userId, Long productInsId, Long itemId, ApsFreeRes apsFreeRes, BigDecimal totalVol, CreditInfo creditInfo) {
        BigDecimal usedValue = apsFreeRes.getUsedValue().add(totalVol);
        apsFreeRes.setUsedValue(usedValue);
        // 使用乐观锁更新免费资源
        LambdaUpdateWrapper<ApsFreeRes> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ApsFreeRes::getUserId, apsFreeRes.getUserId())
                .eq(ApsFreeRes::getProductInsId, apsFreeRes.getProductInsId())
                .eq(ApsFreeRes::getItemId, apsFreeRes.getItemId())
                .set(ApsFreeRes::getUsedValue, apsFreeRes.getUsedValue());
        Boolean sumResult = apsFreeResService.update(apsFreeRes, lambdaUpdateWrapper);
        if (sumResult) {
            log.info("Sum freeres:'{}'", totalVol);
            // 信控信息
            // 剩余免费资源
            BigDecimal leftValue = apsFreeRes.getAmount().subtract(usedValue);
            creditInfo.setLeftFreeRes(leftValue);
        } else {
            log.error("Cannot sum aps freeres:'{}'", apsFreeRes);
        }

        return sumResult;
    }

    /**
     * 免费资源用完，同时扣余额
     */
    private Boolean sum(Long userId, Long productInsId, Long itemId, ApsFreeRes apsFreeRes, BigDecimal ratingFee, ApsBalanceFee apsBalanceFee, CreditInfo creditInfo, RateType rateType) {

        // 如果现在不是已经用完的状态，则更新usedValue为amount（因为已经用完了）
        if (apsFreeRes.getAmount().compareTo(apsFreeRes.getUsedValue())!=0) {
            LambdaUpdateWrapper<ApsFreeRes> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(ApsFreeRes::getUserId, apsFreeRes.getUserId())
                    .eq(ApsFreeRes::getProductInsId, apsFreeRes.getProductInsId())
                    .eq(ApsFreeRes::getItemId, apsFreeRes.getItemId())
                    .set(ApsFreeRes::getUsedValue, apsFreeRes.getAmount());
            Boolean sumResult = apsFreeResService.update(apsFreeRes, lambdaUpdateWrapper);
            if (!sumResult) {
                log.error("Cannot sum aps freeres:'{}'", apsFreeRes);
                return false;
            }

            log.info("Sum freeres:all");
        } else {
            // 如果已经用完
            rateType.setRateType(APPLIED_BUT_OVER_TYPE);
        }
        // 低余量提醒，但如果是免费资源用完的情况，就不用提醒
        creditInfo.setLeftFreeRes(new BigDecimal(0));

        // 扣余额
        return sum(userId, ratingFee, apsBalanceFee, creditInfo);

    }

    /**
     * 只扣余额
     * @param userId
     * @param ratingFee
     */
    private Boolean sum(Long userId, BigDecimal ratingFee, ApsBalanceFee apsBalanceFee, CreditInfo creditInfo) {
        // 不适用或者没有订购，就扣减余额
        // 已经判断过了，balancefee必定不为空
        if (null!=apsBalanceFee) {
            log.info("Aps balanceFee: {}", apsBalanceFee.toJsonString());
            BigDecimal balanceNow = apsBalanceFee.getRemainFee().subtract(ratingFee);
            apsBalanceFee.setRemainFee(balanceNow);
            apsBalanceFee.setUpdateTime(new Date());

            // 更新数据库
            LambdaUpdateWrapper<ApsBalanceFee> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            // set语句没有起到限制作用，但是不影响性能（mysql会优化）。这里写主要是为了说明更新的字段
            lambdaUpdateWrapper.eq(ApsBalanceFee::getUserId, apsBalanceFee.getUserId())
                    .set(ApsBalanceFee::getRemainFee, apsBalanceFee.getRemainFee())
                    .set(ApsBalanceFee::getUpdateTime, apsBalanceFee.getUpdateTime());
            Boolean sumResult = apsBalanceFeeService.update(apsBalanceFee, lambdaUpdateWrapper);
            if (sumResult) {
                // 信控信息
                // 要么不适用，要么没有免费资源了
                // 不管哪种情况，都不需要提醒免费资源了
                creditInfo.setBalance(balanceNow);
            } else {
                log.error("Cannot sum aps balance fee:'{}'", apsBalanceFee);
            }

            return sumResult;
        } else {
            creditInfo.setBalance(null);
            creditInfo.setLeftFreeRes(null);
            return false;
        }

    }

    private BigDecimal getRatingFee(Long productId, Long itemId, BigDecimal ratingValue, RatingError ratingError) throws MvneException {
        // 获取单价
        // 套外可以直接根据产品id和科目查询rating_rate，没订购的直接查询999999999和科目
        RatingRate ratingRate = ratingRateService.selectByKey(productId, itemId);
        if (null!=ratingRate) {
            BigDecimal destMeasureId = ratingRate.getMeasureId();

            // 转换
            // ratingValue单位是B，向上取整转成KB，即计费单位
            BigDecimal sum_KB = ratingValue.divide(new BigDecimal(1024), 0, RoundingMode.CEILING);
            BigDecimal measureId = new BigDecimal(104);
            SysMeasureUnitExchange exchange = sysMeasureUnitExchangeService.exchange(measureId, destMeasureId);

            // 低于1欧分的取1欧分
            BigDecimal ratingFee = sum_KB.multiply(exchange.getExchangeNumerator())
                    .multiply(ratingRate.getRateVal())
                    .divide(ratingRate.getCycleUnit(), 10, RoundingMode.CEILING)
                    .divide(exchange.getExchangeDenominator(), 0, RoundingMode.CEILING);

            return ratingFee;
        } else {
            log.error("No rating rules found by productId: {}, itemId: {}", productId, itemId);
            //throw new MvneException(NO_RATING_RULES_CODE, NO_RATING_RULES_DESC);
            ratingError.setErrCode(NO_RATING_RULES_CODE);
            ratingError.setErrDesc(NO_RATING_RULES_DESC);
            return null;
        }
    }

    private ApsFreeRes getFreeRes(Long userId, Long productInsId, Long itemId) {
        // 可以直接根据key查询出hashmap对象
        ApsFreeRes apsFreeRes = apsFreeResService.selectByKey(userId, productInsId, itemId);

        return apsFreeRes;
    }

    private ApsBalanceFee getBalanceFee(Long userid) {
        ApsBalanceFee apsBalanceFee = apsBalanceFeeService.selectByUserId(userid);
        return apsBalanceFee;
    }

    /*private ApsBalanceFee getBalanceFee(String balanceKey) {
        Map<Object, Object> balanceFee = redisTemplate.opsForHash().entries(balanceKey);
        if (balanceFee.size()!=0) {
            try {
                ApsBalanceFee apsBalanceFee = new ApsBalanceFee(balanceFee);
                return apsBalanceFee;
            } catch (Exception e) {
                log.error("cannot parse balance fee:'{}'", balanceFee.toString() + "_" + e.getMessage(), e);
                return null;
            }
        }
        log.error("Cannot find balance fee by key:'{}'", balanceKey);
        return null;
    }*/

    private Boolean isAppliedGroup(String recordType, CmProdInsInfo cmProdInsInfo, String operatorCode, RatingError ratingError, RateType rateType) {
        // 如果用户没有订购产品，也返回false
        if (null == cmProdInsInfo) {
            rateType.setRateType(NULL_ORDER_TYPE);
            return false;
        }
        log.info("Rating prodInfo of user: {}", cmProdInsInfo.toJsonString());
        // 根据productId查询pm_product，获得group_id
        Long productId = cmProdInsInfo.getProductId();
        Date orderDate = cmProdInsInfo.getValidDate();
        PmProduct pmProduct = pmProductService.select(productId);
        if (null == pmProduct) {
            ratingError.setErrCode(CANNOT_FIND_PM_PRODUCT_CODE);
            ratingError.setErrDesc(CANNOT_FIND_PM_PRODUCT_DESC);
            return false;
        }
        Integer groupId = pmProduct.getGroupId();

        // 根据operatorCode查询视图，获得运营商对应的漫游地区
        /*CountryOperator countryOperator = countryOperatorService.selectByOperator(operatorCode);
        if (null == countryOperator) {
            ratingError.setErrCode(CANNOT_FIND_COUNTRY_CODE);
            ratingError.setErrDesc(CANNOT_FIND_COUNTRY_DESC);
            return false;
        }
        int countryId = countryOperator.getCountryId();*/
        String countryInitials = "ISL";
        BdOperatorCode bdOperatorCode = bdOperatorCodeService.selectByOperatorCode(operatorCode);
        if (null == bdOperatorCode) {
            if (!"GP".equals(recordType)) {
                ratingError.setErrCode(CANNOT_FIND_COUNTRY_CODE);
                ratingError.setErrDesc(CANNOT_FIND_COUNTRY_DESC);
                return false;
            }
        } else {
            countryInitials = bdOperatorCode.getCountryInitials();
        }


        // 根据漫游地区的ISO查询sys_roam_zone_group
        //List<SysRoamZoneGroup> sysRoamZoneGroupList = sysRoamZoneGroupService.selectByCountryAndDate(countryId);
        List<SysRoamZoneGroup> sysRoamZoneGroupList = sysRoamZoneGroupService.selectByISO(countryInitials);
        // 获得订购时间内有效的适用区域的group_id
        SysRoamZoneGroup sysRoamZoneGroup = getSysRoamZone(sysRoamZoneGroupList, orderDate);

        // 如果产品的适用区域与漫游地适用区域一致，返回true，否则false
        if (sysRoamZoneGroup != null) {
            if (groupId.equals(sysRoamZoneGroup.getGroupId())) {
                log.info("Get roam zone group:applied!");
                rateType.setRateType(APPLIED_TYPE);
                return true;
            }
        } else {
            log.info("Get roam zone group:cannot get roam zone group!");
            rateType.setRateType(NOT_APPLIED_TYPE);
            return false;
        }
        log.info("Get roam zone group:applied!");
        rateType.setRateType(NOT_APPLIED_TYPE);
        return false;
    }

    private SysRoamZoneGroup getSysRoamZone(List<SysRoamZoneGroup> sysRoamZoneGroupList, Date orderDate) {
        for (SysRoamZoneGroup sysRoamZoneGroup : sysRoamZoneGroupList) {
            if ((sysRoamZoneGroup.getValidDate().compareTo(orderDate)<0) &&
                    (sysRoamZoneGroup.getExpireDate().compareTo(orderDate)>0)) {
                return sysRoamZoneGroup;
            }
        }

        return null;
    }

    private CmUserDetail getUserDetail(String msisdn, Date eventTimestamp) {
        List<CmUserDetail> cmUserDetailList = cmUserDetailService.findAllByMsisdn(msisdn);
        for (CmUserDetail cmUserDetail : cmUserDetailList) {
            if ((eventTimestamp.compareTo(cmUserDetail.getValidDate()) >= 0) && (eventTimestamp.compareTo(cmUserDetail.getExpireDate()) < 0)) {
                log.info("Get user detail: '{}'", cmUserDetail.toJsonString());
                if (NORMAL_STATUS.equals(cmUserDetail.getUserStatus())) {
                    return cmUserDetail;
                }
            }
        }

        log.error("Can't get user detail on '{}'", eventTimestamp);
        return null;
    }

    CmProdInsInfo getProdInsInfo(Long userId, Date eventTimestamp) {
        List<CmProdInsInfo> cmProdInsInfoList = cmProdInsInfoService.findAllByUserId(userId);
        if (cmProdInsInfoList.size() == 0) {
            log.error("Can't get product ins on '{}'", eventTimestamp);
            return null;
        }

        // 按生效时间降序，取最新的订购产品，因为只有最新产品的免费资源可能是没有用完的
        cmProdInsInfoList.sort(new Comparator<CmProdInsInfo>() {
            @Override
            public int compare(CmProdInsInfo o1, CmProdInsInfo o2) {
                return o2.getValidDate().compareTo(o1.getValidDate());
            }
        });
        /*for (CmProdInsInfo cmProdInsInfo : cmProdInsInfoList) {
            if ((eventTimestamp.compareTo(cmProdInsInfo.getValidDate()) >= 0) && (eventTimestamp.compareTo(cmProdInsInfo.getExpireDate()) < 0)) {
                log.info("Get product ins info: '{}'", cmProdInsInfo.toJsonString());
                return cmProdInsInfo;
            }
        }*/

        // 取最新的产品
        CmProdInsInfo cmProdInsInfo = cmProdInsInfoList.get(0);
        // 判断最新的产品当前是否生效
        Date now = new Date();
        if ((now.compareTo(cmProdInsInfo.getExpireDate()) <= 0) && (now.compareTo(cmProdInsInfo.getValidDate()) >= 0)) {
            return cmProdInsInfo;
        } else {
            return null;
        }
        /*if ((eventTimestamp.compareTo(cmProdInsInfo.getValidDate()) >= 0) && (eventTimestamp.compareTo(cmProdInsInfo.getExpireDate()) < 0)) {
            log.info("Get product ins info: '{}'", cmProdInsInfo.toJsonString());
            return cmProdInsInfo;
        }*/
    }

    private Long getPmEventItem() {
        // 根据计费要素查询pm_event_def和pm_event_item_rel，返回计费科目
        return 66020001L;
    }

}
