package com.cmit.mvne.billing.infomanage.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.infomanage.entity.IOrdOrder;
import com.cmit.mvne.billing.infomanage.remote.service.CreditControlService;
import com.cmit.mvne.billing.infomanage.util.StringUtils;
import com.cmit.mvne.billing.user.analysis.entity.*;
import com.cmit.mvne.billing.user.analysis.service.ApsBalanceFeeService;
import com.cmit.mvne.billing.user.analysis.service.ApsFreeResService;
import com.cmit.mvne.billing.user.analysis.service.CmProdInsInfoService;
import com.cmit.mvne.billing.user.analysis.service.FreeResService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.cmit.mvne.billing.infomanage.common.InfoManageErrorConstant.*;

/**
 * @Author: luxf
 * @Description:
 * @Date: Create in 2019/12/23 14:42
 */

@Service
@Slf4j
public class SyncChangeOfferOrder  {

    @Autowired
    private CmProdInsInfoService cmProdInsInfoService;
    @Autowired
    private ApsFreeResService apsFreeResService;
    @Autowired
    private FreeResService freeResService;
    @Autowired
    private ApsBalanceFeeService apsBalanceFeeService;
    @Autowired
    private IOrdOrderService iOrdOrderService;
    @Autowired
    private CreditControlService creditControlService;

    final int RETRY_TIME = 3;

    @Value(value = "${yellow-mobile.redis.redisson.try-interval}")
    int tryInterval;

    @Transactional(rollbackFor = Exception.class)
    public void sync(IOrdOrder iOrdOrder, IProd iProd) throws MvneException{
        try {
            syncDBRedis(iOrdOrder,iProd);
            creditControlService.CreditControlOfferSms(iOrdOrder,iProd);
        } catch (Exception e) {
            log.error("SyncChangeOfferOrder-sync error! order is {}",iOrdOrder.getOrderId());
            log.error("SyncChangeOfferOrder-sync iOrdOrder is : {} , error message : {}" , iOrdOrder.toString() , StringUtils.getExceptionText(e).substring(0, 1023));
            //distributeLock.unlock(lockKey);
            //throw new MvneException(MYSQL_INSERT_FAILED_CODE, StringUtils.getExceptionText(e).substring(0, 1023));
            if ( e instanceof MvneException ){
                throw new MvneException(((MvneException) e).getErrCode(),((MvneException) e).getErrDesc());
            }else if (e instanceof QueryTimeoutException) {
                log.error("SyncChangeOfferOrder-sync insert timeout !");
                throw new MvneException(MYSQL_INSERT_TIMEOUT_CODE, StringUtils.getExceptionText(e).substring(0, 1023));
            } else {
                throw new MvneException(MYSQL_INSERT_FAILED_CODE,StringUtils.getExceptionText(e).substring(0, 1023));
            }
        }
    }
    private void syncDBRedis(IOrdOrder iOrdOrder,IProd iProd) throws MvneException, InterruptedException {
        log.info("SyncChangeOfferOrder-sync-syncDBRedis start ! iUser is : {} , productFee is {}",iProd.getUserId(),iProd.getProductFee());
        //插入订单表
        iOrdOrderService.insert(iOrdOrder);

        int retry = 0;
        Boolean isUpdateBalance = false;
        // 同步数据到mysql
        //免费资源同步
        /*List<ApsFreeRes> apsFreeResList = apsFreeResService.findByUserId(iProd.getUserId());
        log.info("SyncChangeOfferOrder-sync-syncDBRedis apsFreeResList is {} ",apsFreeResList);
        if ( apsFreeResList.size() > 0 ){
            log.error("SyncChangeOfferOrder-sync-syncDBRedis have apsFreeResList ! apsFreeResList is {}",apsFreeResList);
            throw new MvneException(MYSQL_SELECT_FAILED_CODE,"have valid apsFreeResList");
        }*/
        //失效旧的免费资源信息
        LambdaUpdateWrapper<ApsFreeRes> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ApsFreeRes::getUserId,iOrdOrder.getUserId());
        updateWrapper.eq(ApsFreeRes::getMeasureId,"103");
        updateWrapper.gt(ApsFreeRes::getExpireDate,new Date());
        updateWrapper.set(ApsFreeRes::getExpireDate,iProd.getValidDate());
        Boolean updateStatus = apsFreeResService.update(updateWrapper);
        if ( updateStatus ){
            log.info("SyncChangeOfferOrder-sync-syncDBRedis update apsFreeRes success !");
        }else {
            log.info("SyncChangeOfferOrder-sync-syncDBRedis update apsFreeRes failed !");
        }

        List<FreeRes> freeResList = freeResService.selectProduct(iProd.getProductId());
        log.info("SyncChangeOfferOrder-sync-syncDBRedis freeResList is {}",freeResList);
        if ( freeResList.size() > 0 ){
            for(FreeRes freeRes : freeResList) {
                apsFreeResService.insert(new ApsFreeRes(freeRes,iProd.getProductInsId(),iProd.getUserId(),iProd.getValidDate(),iProd.getExpireDate()));
            }
        }else {
            log.error("SyncChangeOfferOrder-sync-syncDBRedis select freeRes is null , freeResList is {}",freeResList);
            throw new MvneException(MYSQL_SELECT_FAILED_CODE,"select freeRes is null !");
        }

        //订购产品同步,并存入缓存中
        List<CmProdInsInfo> cmProdInsInfoList = cmProdInsInfoService.insertOffer(iProd.getUserId(),new CmProdInsInfo(iProd));
        if ( cmProdInsInfoList.size() > 0 ){
            log.info("SyncChangeOfferOrder-sync-syncDBRedis update cmProdInsInfo success! cmProdInsInfoList is {}",cmProdInsInfoList);
        }else {
            log.error("SyncChangeOfferOrder-sync-syncDBRedis update cmProdInsInfo failed! cmProdInsInfoList is {}",cmProdInsInfoList);
            throw new MvneException(MYSQL_UPDATE_FAILED_CODE,"update cmProdInsInfo failed!");
        }
        log.info("SyncChangeOfferOrder-sync-syncDBRedis insert CmProdInsInfo ! iUser is : {}",iProd.getUserId());
        //余额同步更新，添加乐观锁，尝试3次更新操作，更新失败抛出异常
        do{
            //更新余额
            ApsBalanceFee apsBalanceFee = apsBalanceFeeService.selectByUserId(iProd.getUserId());
            log.info("SyncChangeOfferOrder-sync-syncDBRedis before apsBalanceFee is {} ",apsBalanceFee);

            //订购金额,单位由欧元转为欧分
            BigDecimal productFee = iProd.getProductFee().multiply(new BigDecimal(100));
            if ( apsBalanceFee == null  ) {
                log.error("SyncChangeOfferOrder-sync-syncDBRedis changeOffer balance userId is not exist ! balance insert failed ! userId is {}",iProd.getUserId());
                throw new MvneException(MYSQL_SELECT_FAILED_CODE,"changeOffer balance userId is not exist ! balance insert failed !");
            }else {
                BigDecimal remain_FeeOld = apsBalanceFee.getRemainFee();
                BigDecimal remain_FeeNew = remain_FeeOld.subtract(productFee);
                log.info("SyncChangeOfferOrder-sync-syncDBRedis remainFeeOld value is {} , remainFeeNew is {}", remain_FeeOld, remain_FeeNew);
                //ApsBalanceFee apsBalanceFeeNew = new ApsBalanceFee(iProd.getUserId(),remain_FeeNew,new BigDecimal(balanceFeeMap.get("measureId").toString()),new Date());
                //apsBalanceFeeService.insert(apsBalanceFeeNew);
                //更新余额，只保留一条新的记录
                apsBalanceFee.setRemainFee(remain_FeeNew);
                apsBalanceFee.setUpdateTime(new Date());
                LambdaUpdateWrapper<ApsBalanceFee> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(ApsBalanceFee::getUserId, apsBalanceFee.getUserId());
                isUpdateBalance = apsBalanceFeeService.update(apsBalanceFee, lambdaUpdateWrapper);
                log.info("SyncChangeOfferOrder-sync-syncDBRedis isUpdateBalance is {}", isUpdateBalance);
                if (!isUpdateBalance) {
                    retry++;
                    Thread.sleep(tryInterval);
                }
            }
        }while ((retry < RETRY_TIME) && !isUpdateBalance );

        log.info("SyncChangeOfferOrder-sync-syncDBRedis retry is {}",retry);
        // 如果重试了n次都失败，直接抛异常回退
        if ( ! isUpdateBalance ){
            log.error("SyncChangeOfferOrder-sync-syncDBRedis Over retry times! Please ensure the rating is running correct !");
            throw new MvneException(OVER_RETRY_UPDATE_TIME_CODE, OVER_RETRY_UPDATE_TIME_DESC);
        }
            /*int updateStatus = apsBalanceFeeService.updateBalance(iProd.getUserId(),remain_FeeNew,new Date());
            if ( updateStatus > 0 )
            {
                log.info("SyncChangeOfferOrder-sync-syncDBRedis update apsBalanceFee updateStatus is {} ",updateStatus);
                log.info("SyncChangeOfferOrder-sync-syncDBRedis update apsBalanceFee success ! orderId is {} , apsBalanceFeeNew is {}",iProd.getOrderId(),remain_FeeNew);

            }else {
                log.info("SyncChangeOfferOrder-sync-syncDBRedis update apsBalanceFee updateStatus is {} ",updateStatus);
                log.info("SyncChangeOfferOrder-sync-syncDBRedis update apsBalanceFee failed ! orderId is {} , apsBalanceFeeNew is {}",iProd.getOrderId(),remain_FeeNew);
                throw new MvneException(MYSQL_UPDATE_FAILED_CODE,"changeOffer update remainFee failed!");
            }*/

        log.info("SyncChangeOfferOrder-sync-syncDBRedis end ! iUser is : {}",iProd.getUserId());
    }

}
