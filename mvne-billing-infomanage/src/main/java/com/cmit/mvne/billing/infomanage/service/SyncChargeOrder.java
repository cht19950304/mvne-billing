package com.cmit.mvne.billing.infomanage.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.infomanage.entity.IOrdOrder;
import com.cmit.mvne.billing.infomanage.remote.service.CreditControlService;
import com.cmit.mvne.billing.infomanage.util.StringUtils;
import com.cmit.mvne.billing.user.analysis.entity.ApsBalanceFee;
import com.cmit.mvne.billing.user.analysis.service.ApsBalanceFeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static com.cmit.mvne.billing.infomanage.common.InfoManageErrorConstant.*;

/**
 * @Author: luxf
 * @Description:
 * @Date: Create in 2019/12/26 11:50
 */

@Service
@Slf4j
public class SyncChargeOrder  {

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
    public void sync(IOrdOrder iOrdOrder) throws MvneException {
        //同步DB
        try {
            syncDB(iOrdOrder);
            creditControlService.CreditControlChargeSms(iOrdOrder);
        } catch (Exception e) {
            log.error("SyncChargeOrder-sync error! order is {}",iOrdOrder.getOrderId());
            log.error("SyncChargeOrder-sync iOrdOrder is : {} , error message : {}" , iOrdOrder.toString() , StringUtils.getExceptionText(e).substring(0, 1023));
            //distributeLock.unlock(lockKey);
            //throw new MvneException(MYSQL_INSERT_FAILED_CODE, StringUtils.getExceptionText(e).substring(0, 1023));
            if ( e instanceof MvneException ){
                throw new MvneException(((MvneException) e).getErrCode(),((MvneException) e).getErrDesc());
            }else if (e instanceof QueryTimeoutException) {
                log.error("SyncChargeOrder-sync update timeout !");
                throw new MvneException(MYSQL_UPDATE_TIMEOUT_CODE, StringUtils.getExceptionText(e).substring(0, 1023));
            } else {
                throw new MvneException(MYSQL_UPDATE_FAILED_CODE,StringUtils.getExceptionText(e).substring(0, 1023));
            }
        }

    }
    private void syncDB(IOrdOrder iOrdOrder) throws MvneException, InterruptedException {
        log.info("SyncChargeOrder-sync-syncDB start ! orderId is {} , iUser is : {} ,factMoney is {}",iOrdOrder.getOrderId(),iOrdOrder.getUserId(),iOrdOrder.getFactMoney());
        //插入订单表
        iOrdOrderService.insert(iOrdOrder);

        int retry = 0;
        Boolean isUpdateBalance = false;
        //余额同步更新，添加乐观锁，尝试3次更新操作，更新失败抛出异常
        do {
            ApsBalanceFee apsBalanceFee = apsBalanceFeeService.selectByUserId(iOrdOrder.getUserId());
            log.info("SyncChargeOrder-sync-syncDBRedis before apsBalanceFee is {} ", apsBalanceFee);

            //充值金额,单位由欧元转为欧分
            BigDecimal balanceFee = iOrdOrder.getFactMoney().multiply(new BigDecimal(100));
            if (apsBalanceFee == null) {
                log.error("SyncChargeOrder-sync-syncDB charge userId is not exist ! balance insert failed ! userId is {}", iOrdOrder.getUserId());
                throw new MvneException(MYSQL_SELECT_FAILED_CODE, "charge userId is not exist ! balance insert failed !");
            } else {
                BigDecimal remain_FeeOld = apsBalanceFee.getRemainFee();
                BigDecimal remain_FeeNew = remain_FeeOld.add(balanceFee);
                log.info("SyncChargeOrder-sync-syncDB remainFeeOld value is {} , remainFeeNew is {}", remain_FeeOld, remain_FeeNew);
                //ApsBalanceFee apsBalanceFeeNew = new ApsBalanceFee(userId,remain_FeeNew,new BigDecimal(balanceFeeMap.get("measureId").toString()),new Date());
                //apsBalanceFeeService.insert(apsBalanceFeeNew);
                //更新余额，只保留一条新的记录
                apsBalanceFee.setRemainFee(remain_FeeNew);
                apsBalanceFee.setUpdateTime(new Date());
                LambdaUpdateWrapper<ApsBalanceFee> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                lambdaUpdateWrapper.eq(ApsBalanceFee::getUserId,apsBalanceFee.getUserId());
                isUpdateBalance = apsBalanceFeeService.update(apsBalanceFee,lambdaUpdateWrapper);
                log.info("SyncChargeOrder-sync-syncDBRedis isUpdateBalance is {}",isUpdateBalance);
                if ( ! isUpdateBalance ){
                    retry++;
                    Thread.sleep(tryInterval);
                }
                /*int updateStatus = apsBalanceFeeService.updateBalance(userId, remain_FeeNew, new Date());
                if (updateStatus > 0) {
                    log.info("SyncChargeOrder-sync-syncDB update apsBalanceFee updateStatus is {} ", updateStatus);
                    log.info("SyncChargeOrder-sync-syncDB update apsBalanceFee success ! orderId is {} , apsBalanceFeeNew is {}", orderId, remain_FeeNew);

                } else {
                    log.info("SyncChargeOrder-sync-syncDB update apsBalanceFee updateStatus is {} ", updateStatus);
                    log.info("SyncChargeOrder-sync-syncDB update apsBalanceFee failed ! orderId is {} , apsBalanceFeeNew is {}", orderId, remain_FeeNew);
                    throw new MvneException(MYSQL_UPDATE_FAILED_CODE, "update remainFee failed!");
                }*/

            }
        }while ((retry < RETRY_TIME) && !isUpdateBalance );
        log.info("SyncChargeOrder-sync-syncDBRedis retry is {}",retry);
        // 如果重试了n次都失败，直接抛异常回退
        if ( ! isUpdateBalance ){
            log.error("SyncChargeOrder-sync-syncDBRedis Over retry times! Please ensure the rating is running correct !");
            throw new MvneException(OVER_RETRY_UPDATE_TIME_CODE, OVER_RETRY_UPDATE_TIME_DESC);
        }

        log.info("SyncChargeOrder-sync-syncDB end ! orderId is {} , iUser is : {}",iOrdOrder.getOrderId(),iOrdOrder.getUserId());
    }

}
