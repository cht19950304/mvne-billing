package com.cmit.mvne.billing.infomanage.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cmit.mvne.billing.infomanage.common.DistributeLock;
import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.infomanage.entity.IOrdOrder;
import com.cmit.mvne.billing.infomanage.util.StringUtils;
import com.cmit.mvne.billing.user.analysis.entity.ApsBalanceFee;
import com.cmit.mvne.billing.user.analysis.mapper.ApsBalanceFeeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.cmit.mvne.billing.infomanage.common.InfoManageErrorConstant.*;

/**
 * @Author: luxf
 * @Description:
 * @Date: Create in 2019/12/26 11:50
 */

@Service
@Slf4j
public class SyncChargeOrder  {

    @Value(value = "${yellow-mobile.redis.redisson.wait-time}")
    int waitTime;
    @Value(value = "${yellow-mobile.redis.redisson.lease-time}")
    int leaseTime;

    @Autowired
    private DistributeLock distributeLock;

    @Autowired
    private ApsBalanceFeeMapper apsBalanceFeeMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    //@Override
    public void sync(IOrdOrder iOrdOrder) throws MvneException {
        System.out.println(Thread.currentThread().getName() + "-charge- " + iOrdOrder.getOrderId());


        Long orderId = iOrdOrder.getOrderId();
        log.info("Sync charge user order : {}", orderId);

        // 同一个号码所有操作的key都是一样的
        String lockKey = "InfoManageKey:" + iOrdOrder.getUserId();
        // 尝试使用redisson对号码加锁操作，同步方式，公平锁，线程退出的话时间会久一点，如果影响性能可以改成一般的锁
        boolean acquiredLock = distributeLock.fairLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);

        if (acquiredLock) {
            //同步DB
            try {
                syncDB(iOrdOrder.getUserId(),iOrdOrder.getFactMoney());
            } catch (MvneException e) {
                log.error("Sync DB error! order is {}",orderId);
                log.error(iOrdOrder.toString() + "_" + e.getMessage(), e);

                distributeLock.unlock(lockKey);

                throw new MvneException(MYSQL_INSERT_FAILED_CODE, StringUtils.getExceptionText(e).substring(0, 1023));
            }
            // ͬ同步redis
            try {
                syncRedis(iOrdOrder.getUserId(),iOrdOrder.getFactMoney());
            } catch (Exception e) {
                log.error("Sync Redis error! order is {}",orderId);
                log.error(iOrdOrder.toString() + "_" + e.getMessage(), e);

                distributeLock.unlock(lockKey);
                throw new MvneException(REDIS_LPUSH_FAILED_CODE, StringUtils.getExceptionText(e).substring(0, 1023));
            }
            distributeLock.unlock(lockKey);
        }
        return;

    }

    /**
     * 同步mysql
     * @param userId,factMoney
     * @throws Exception
     */
    private void syncDB(long userId, BigDecimal factMoney) throws MvneException{
        QueryWrapper<ApsBalanceFee> queryWrapper = new  QueryWrapper<>();
        queryWrapper.eq("USER_ID",userId);
        int userIdCount = apsBalanceFeeMapper.selectCount(queryWrapper);
        System.out.println("userIdCount:"+userIdCount);

        if ( userIdCount == 0 ) {
            ApsBalanceFee apsBalanceFee = new ApsBalanceFee(userId,factMoney, new BigDecimal("10403"),new Date());
            apsBalanceFeeMapper.insert(apsBalanceFee);
        }else if( userIdCount > 0)
        {
            ApsBalanceFee apsBalanceFeeOld = apsBalanceFeeMapper.selectByUserId(userId);
            BigDecimal remain_FeeOld = apsBalanceFeeOld.getRemainFee();
            BigDecimal remain_FeeNew = remain_FeeOld.add(factMoney);
            ApsBalanceFee apsBalanceFeeNew = new ApsBalanceFee(userId,remain_FeeNew,apsBalanceFeeOld.getMesureId(),new Date());
            apsBalanceFeeMapper.insert(apsBalanceFeeNew);
        }
    }


    /**
     * 同步redis
     * @param userId,factMoney
     * @throws Exception
     */
    private void syncRedis(long userId, BigDecimal factMoney) throws MvneException {
        // ͬ获取key
        String userTableKey = "BalanceFee:" + userId;
        //获取最新记录
        List<ApsBalanceFee> apsBalanceFeeList= JSONObject.parseArray(redisTemplate.opsForList().range(userTableKey,0,0).toString(),ApsBalanceFee.class);
        System.out.println(apsBalanceFeeList.toString()+apsBalanceFeeList.size());
        if( apsBalanceFeeList.size() == 0 )
        {
            ApsBalanceFee apsBalanceFee=new ApsBalanceFee(userId,factMoney,new BigDecimal("10403"),new Date());
            redisTemplate.opsForList().leftPush(userTableKey, apsBalanceFee.toJsonString());
        }else if( apsBalanceFeeList.size() > 0 ){
            Boolean updateStatus = updateBalance(apsBalanceFeeList, userId, factMoney, userTableKey);
            if (updateStatus) {
                log.info("Sync update balanceFee success! userId is {}",userId);
            } else {
                log.error("Sync update balanceFee failed! userId is {}",userId);
                throw new MvneException(MYSQL_UPDATE_FAILED_CODE,"update balanceFee failed!");
            }
        }

    }
    public Boolean updateBalance(List<ApsBalanceFee> apsBalanceFeeList,long userId,BigDecimal factMoney,String userTableKey) throws MvneException{

        for(ApsBalanceFee apsBalanceFee:apsBalanceFeeList){
            if(apsBalanceFee.getUserId() == userId){
                BigDecimal remainFee=factMoney.add(apsBalanceFee.getRemainFee());
                System.out.println("remainFee:"+remainFee);
                apsBalanceFee.setRemainFee(remainFee);
                apsBalanceFee.setUpdateTime(new Date());
                //是否保留历史记录
                //redisTemplate.opsForList().leftPop(userTableKey);
                redisTemplate.opsForList().leftPush(userTableKey,apsBalanceFee.toJsonString());
                return true;
            }
        }
        return false;

    }

}
