package com.cmit.mvne.billing.infomanage.service;

import com.cmit.mvne.billing.infomanage.common.DistributeLock;
import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.infomanage.entity.*;
import com.cmit.mvne.billing.infomanage.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.cmit.mvne.billing.infomanage.common.InfoManageErrorConstant.*;

/**
 * @Author: caikunchi
 * @Description: 同步开户订单信息
 * @Date: Create in 2019/12/3 15:13
 */
@Service
@Slf4j
public class SyncCreateUserOrder {
    @Value(value = "${yellow-mobile.redis.redisson.wait-time}")
    int waitTime;
    @Value(value = "${yellow-mobile.redis.redisson.lease-time}")
    int leaseTime;

    int maxTryTime = 3;

    @Autowired
    private CmUserDetailService cmUserDetailService;
    @Autowired
    private CmProdInsInfoService cmProdInsInfoService;

    @Autowired
    private DistributeLock distributeLock;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 同步数据到redis和DB，保证事务一致
     *
     * @param iOrdOrder 需要同步的订单
     * @return void
     */
    //@Override
    //@Transactional(rollbackFor = Exception.class)
    public void sync(IOrdOrder iOrdOrder,IProd iProd,IUser iUser) throws MvneException{
        System.out.println(Thread.currentThread().getName() + "-createuser- " + iOrdOrder.getOrderId());

        Long orderId = iOrdOrder.getOrderId();
        log.info("Sync create user order : {}", orderId);

        // 同一个号码所有操作的key都是一样的
        String lockKey = "InfoManageKey:" + iOrdOrder.getUserId();
        // 尝试使用redisson对号码加锁操作，同步方式，公平锁，线程退出的话时间会久一点，如果影响性能可以改成一般的锁
        // 如果无法获取到锁则直接抛异常
        boolean acquiredLock = distributeLock.fairLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);

        if (acquiredLock) {
            // 这里可以考虑一下顺序，是不是可以放在获取锁之前？
            // 根据orderId查询i_user，业务逻辑上只会有一条
            //List<IUser> iUserList = iUserService.selectByOrderId(orderId);
            // 根据orderId查询i_prod，可能会有多条
            //List<IProd> iProdList = iProdService.selectByOrderId(orderId);
            // 同步到DB
            try {
                //syncDB(iUserList, iProdList);
                syncDB(iUser,iProd);
            } catch (MvneException e) {
                log.error("Sync DB error! order is {}",orderId);
                log.error(iOrdOrder.toString() + "_" + e.getMessage(), e);

                distributeLock.unlock(lockKey);

                throw new MvneException(MYSQL_INSERT_FAILED_CODE, StringUtils.getExceptionText(e).substring(0, 1023));
            }
            // 同步到redis
            try {
                //syncRedis(iUserList, iProdList);
                syncRedis(iUser,iProd);
            } catch (MvneException e) {
                log.error("Sync Redis error! order is {}",orderId);
                log.error(iOrdOrder.toString() + "_" + e.getMessage(), e);

                distributeLock.unlock(lockKey);

                throw new MvneException(REDIS_LPUSH_FAILED_CODE, StringUtils.getExceptionText(e).substring(0, 1023));
            }
            distributeLock.unlock(lockKey);
        }

        return ;
    }

    /**
     * 同步到DB
     * @param iUser
     * @param iProd
     * @throws Exception
     */
    private void syncDB(IUser iUser, IProd iProd) throws MvneException{
        // 同步数据到mysql
        cmUserDetailService.insert(new CmUserDetail(iUser));
        // 这里可以调用套餐订购的方法，生成用户的免费资源以及插入套餐失效时间队列
        cmProdInsInfoService.insert(new CmProdInsInfo(iProd));
    }

    /**
     * 同步到redis
     * @param iUser
     * @param iProd
     * @throws Exception
     */
    private void syncRedis(IUser iUser, IProd iProd) throws MvneException {
        // 同步数据到redis

        String userTableKey = "UserDetail:" + iUser.getMsisdn();
        String prodTableKey = "ProdInsInfo:" + iUser.getUserId();

        CmUserDetail cmUserDetail = new CmUserDetail(iUser);
        String userTableValue = cmUserDetail.toJsonString();
        redisTemplate.opsForList().leftPush(userTableKey, userTableValue);

        CmProdInsInfo cmProdInsInfo = new CmProdInsInfo(iProd);
        String prodTableValue = cmProdInsInfo.toJsonString();
        redisTemplate.opsForList().leftPush(prodTableKey, prodTableValue);


    }
}
