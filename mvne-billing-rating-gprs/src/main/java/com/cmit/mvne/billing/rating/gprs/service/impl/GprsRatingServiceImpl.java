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

        // ?????????
        String errCode = null;
        String errDesc = null;
        // ?????????rating_error???????????????????????????
        RatingError ratingError = new RatingError();

        BigDecimal ratingValue = new BigDecimal("0");
        BigDecimal deductFreeres = new BigDecimal("0");
        BigDecimal ratingFee = new BigDecimal("0");

        RateType rateType = new RateType();
        rateType.setRateType(0);
        // ???????????????1?????????????????????2?????????????????????????????????3?????????
        Integer sumType = 0;

        String freeResKey = "";
        String balanceKey = "";

        ApsFreeRes apsFreeRes = null;
        ApsBalanceFee apsBalanceFee = null;

        log.info("rating gprs cdr , cdr???{}", cdrGprs);
        Long id = cdrGprs.getId();

        // ??????????????????
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

        // ?????????????????????????????????
        // ?????????????????????????????????
        //try {
            //creditControl.init();
            // ????????????????????????????????????????????????redolog?????????
            // readFlag=true?????????redis?????????????????????
            if (readFlag.equals(RATING_FLAG)) {
                cdrJson = getRatedCdr(cdrGprs.getOriginalFile(), cdrGprs.getId());
            } else {
                cdrJson = null;
            }
            if (cdrJson == null) {
                int retry = tryTimes;
                while (retry-- > 0) {
                    // ????????????????????????????????????????????????????????????????????????

                    //boolean acquiredLock = distributeLock.fairLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);
                    boolean acquiredLock = true;
                    if (acquiredLock) {
                        log.info("Rating Cdr: {}, Acquired lock:{} !", cdrGprs.getMsisdn(), lockKey);
                        // ??????????????????
                        Long eventItemId = getPmEventItem();
                        cdrGprs.setItemId(eventItemId);
                        // ?????????????????????????????????
                        CmUserDetail cmUserDetail = getUserDetail(msisdn, eventTimestamp);
                        Long productId = 999999999L;
                        if (null != cmUserDetail) {
                            log.info("Rating userInfo: {}", cmUserDetail.toJsonString());
                            Long userId = cmUserDetail.getUserId();
                            cdrGprs.setUserId(userId);
                            balanceKey = "BalanceFee:" + userId;
                            // ???????????????????????????
                            CmProdInsInfo cmProdInsInfo = getProdInsInfo(userId, eventTimestamp);
                            // ??????????????????
                            // ?????????????????????????????????????????????????????????????????????
                            // ??????operatorCode???????????????????????????????????????simmin
                            if ("GP".equals(recordType) && (("".equals(operatorCode)) || (operatorCode==null))) {
                                operatorCode = "AAMOW";
                            }

                            // do-while???????????????????????????????????????????????????balance???freeres?????????
                            Boolean sumResult = false;
                            // ?????????
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

                                    // ??????????????????
                                    apsFreeRes = getFreeRes(userId, productInsId, eventItemId);

                                    if (null != apsFreeRes) {
                                        log.info("Aps freeRes: {}", apsFreeRes.toJsonString());
                                        // ??????????????????????????????????????????????????????0
                                        // ?????????=?????????+????????????
                                        BigDecimal hadUsedValue = apsFreeRes.getUsedValue().add(totalVol);
                                        // ????????????????????????????????????????????????????????????0
                                        if (hadUsedValue.compareTo(apsFreeRes.getAmount()) < 1) {
                                            deductFreeres = totalVol;
                                            // ??????????????????
                                            sumResult = sum(userId, productInsId, eventItemId, apsFreeRes, totalVol, creditInfo);
                                        } else {
                                            // ?????????????????????????????????
                                            // ????????????????????????????????????????????????????????????
                                            deductFreeres = apsFreeRes.getAmount().subtract(apsFreeRes.getUsedValue());
                                            ratingValue = hadUsedValue.subtract(apsFreeRes.getAmount());
                                            ratingFee = getRatingFee(productId, eventItemId, ratingValue, ratingError);
                                            if (ratingError.isNotNull()) {
                                                return ratingError;
                                            }
                                            // ??????????????????????????????
                                            sumResult = sum(userId, productInsId, eventItemId, apsFreeRes, ratingFee, apsBalanceFee, creditInfo, rateType);
                                        }
                                    } else {
                                        return new RatingError(CANNOT_FIND_FREERES_CODE, CANNOT_FIND_FREERES_DESC);
                                    }

                                } else {
                                    if (ratingError.isNotNull()) {
                                        return ratingError;
                                    }
                                    // ???????????????????????????????????????
                                    // ?????????????????????
                                    if (cmProdInsInfo != null) {
                                        freeResKey = "FreeRes:" + userId + ":" + cmProdInsInfo.getProductInsId() + ":" + eventItemId;
                                        apsFreeRes = getFreeRes(userId, cmProdInsInfo.getProductInsId(), eventItemId);
                                        if (null != apsFreeRes) {
                                            // ??????cmProdInsInfo??????????????????????????????????????????
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
                                    // ????????????
                                    sumResult = sum(userId, ratingFee, apsBalanceFee, creditInfo);
                                }
                                if (!sumResult) {
                                    retrySum++;
                                    Thread.sleep(tryInterval);
                                }
                            } while ((retrySum < RETRY_TIME) && !sumResult);
                            // ???????????????n????????????????????????????????????????????????????????????
                            if (!sumResult) {
                                log.error("Over retry times! Please ensure the infoManager is running correctlt~");
                                throw new MvneException(OVER_RETRY_SUM_TIME_CODE, OVER_RETRY_SUM_TIME_DESC);
                            }
                            if (ratingError.isNotNull()) {
                                return ratingError;
                            }

                            // ??????????????????????????????????????????????????????????????????????????????
                            if ((null == creditInfo.getLeftFreeRes()) && (null == creditInfo.getBalance())) {
                                return new RatingError(CANNOT_SUM_BALANCE_CODE, CANNOT_SUM_BALANCE_DESC);
                            } else {
                                // ??????????????????
                                creditCallInfoList.add(new CreditCallInfo(id, msisdn, rateType.getRateType(), creditInfo));
                                /*try {
                                    creditControl.call(msisdn, rateType.getRateType(), creditInfo);
                                } catch (Exception e) {
                                    // ????????????????????????????????????????????????????????????
                                    log.error("Call credit info fail!", e);
                                }*/
//                        cdrGprs = new CdrGprs(cdr, fileName, productId, deductFreeres, ratingValue, ratingFee);

                                // ????????????
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
                            // ????????????????????????
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
                // ??????????????????????????????
                // ???????????????????????????
                /*if (cdrJson.contains("errCode")) {
                    // ??????
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
     * !!????????????????????????
     * ??????????????????
     * @param apsFreeRes ????????????????????????
     * @param totalVol ???????????????
     */
    private Boolean sum(Long userId, Long productInsId, Long itemId, ApsFreeRes apsFreeRes, BigDecimal totalVol, CreditInfo creditInfo) {
        BigDecimal usedValue = apsFreeRes.getUsedValue().add(totalVol);
        apsFreeRes.setUsedValue(usedValue);
        // ?????????????????????????????????
        LambdaUpdateWrapper<ApsFreeRes> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ApsFreeRes::getUserId, apsFreeRes.getUserId())
                .eq(ApsFreeRes::getProductInsId, apsFreeRes.getProductInsId())
                .eq(ApsFreeRes::getItemId, apsFreeRes.getItemId())
                .set(ApsFreeRes::getUsedValue, apsFreeRes.getUsedValue());
        Boolean sumResult = apsFreeResService.update(apsFreeRes, lambdaUpdateWrapper);
        if (sumResult) {
            log.info("Sum freeres:'{}'", totalVol);
            // ????????????
            // ??????????????????
            BigDecimal leftValue = apsFreeRes.getAmount().subtract(usedValue);
            creditInfo.setLeftFreeRes(leftValue);
        } else {
            log.error("Cannot sum aps freeres:'{}'", apsFreeRes);
        }

        return sumResult;
    }

    /**
     * ????????????????????????????????????
     */
    private Boolean sum(Long userId, Long productInsId, Long itemId, ApsFreeRes apsFreeRes, BigDecimal ratingFee, ApsBalanceFee apsBalanceFee, CreditInfo creditInfo, RateType rateType) {

        // ???????????????????????????????????????????????????usedValue???amount???????????????????????????
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
            // ??????????????????
            rateType.setRateType(APPLIED_BUT_OVER_TYPE);
        }
        // ???????????????????????????????????????????????????????????????????????????
        creditInfo.setLeftFreeRes(new BigDecimal(0));

        // ?????????
        return sum(userId, ratingFee, apsBalanceFee, creditInfo);

    }

    /**
     * ????????????
     * @param userId
     * @param ratingFee
     */
    private Boolean sum(Long userId, BigDecimal ratingFee, ApsBalanceFee apsBalanceFee, CreditInfo creditInfo) {
        // ?????????????????????????????????????????????
        // ?????????????????????balancefee???????????????
        if (null!=apsBalanceFee) {
            log.info("Aps balanceFee: {}", apsBalanceFee.toJsonString());
            BigDecimal balanceNow = apsBalanceFee.getRemainFee().subtract(ratingFee);
            apsBalanceFee.setRemainFee(balanceNow);
            apsBalanceFee.setUpdateTime(new Date());

            // ???????????????
            LambdaUpdateWrapper<ApsBalanceFee> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            // set?????????????????????????????????????????????????????????mysql????????????????????????????????????????????????????????????
            lambdaUpdateWrapper.eq(ApsBalanceFee::getUserId, apsBalanceFee.getUserId())
                    .set(ApsBalanceFee::getRemainFee, apsBalanceFee.getRemainFee())
                    .set(ApsBalanceFee::getUpdateTime, apsBalanceFee.getUpdateTime());
            Boolean sumResult = apsBalanceFeeService.update(apsBalanceFee, lambdaUpdateWrapper);
            if (sumResult) {
                // ????????????
                // ?????????????????????????????????????????????
                // ??????????????????????????????????????????????????????
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
        // ????????????
        // ??????????????????????????????id???????????????rating_rate???????????????????????????999999999?????????
        RatingRate ratingRate = ratingRateService.selectByKey(productId, itemId);
        if (null!=ratingRate) {
            BigDecimal destMeasureId = ratingRate.getMeasureId();

            // ??????
            // ratingValue?????????B?????????????????????KB??????????????????
            BigDecimal sum_KB = ratingValue.divide(new BigDecimal(1024), 0, RoundingMode.CEILING);
            BigDecimal measureId = new BigDecimal(104);
            SysMeasureUnitExchange exchange = sysMeasureUnitExchangeService.exchange(measureId, destMeasureId);

            // ??????1????????????1??????
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
        // ??????????????????key?????????hashmap??????
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
        // ??????????????????????????????????????????false
        if (null == cmProdInsInfo) {
            rateType.setRateType(NULL_ORDER_TYPE);
            return false;
        }
        log.info("Rating prodInfo of user: {}", cmProdInsInfo.toJsonString());
        // ??????productId??????pm_product?????????group_id
        Long productId = cmProdInsInfo.getProductId();
        Date orderDate = cmProdInsInfo.getValidDate();
        PmProduct pmProduct = pmProductService.select(productId);
        if (null == pmProduct) {
            ratingError.setErrCode(CANNOT_FIND_PM_PRODUCT_CODE);
            ratingError.setErrDesc(CANNOT_FIND_PM_PRODUCT_DESC);
            return false;
        }
        Integer groupId = pmProduct.getGroupId();

        // ??????operatorCode???????????????????????????????????????????????????
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


        // ?????????????????????ISO??????sys_roam_zone_group
        //List<SysRoamZoneGroup> sysRoamZoneGroupList = sysRoamZoneGroupService.selectByCountryAndDate(countryId);
        List<SysRoamZoneGroup> sysRoamZoneGroupList = sysRoamZoneGroupService.selectByISO(countryInitials);
        // ?????????????????????????????????????????????group_id
        SysRoamZoneGroup sysRoamZoneGroup = getSysRoamZone(sysRoamZoneGroupList, orderDate);

        // ??????????????????????????????????????????????????????????????????true?????????false
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

        // ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
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

        // ??????????????????
        CmProdInsInfo cmProdInsInfo = cmProdInsInfoList.get(0);
        // ???????????????????????????????????????
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
        // ????????????????????????pm_event_def???pm_event_item_rel?????????????????????
        return 66020001L;
    }

}
