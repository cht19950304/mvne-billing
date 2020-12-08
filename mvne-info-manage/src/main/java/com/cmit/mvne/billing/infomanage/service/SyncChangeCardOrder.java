/**
 * 
 */
package com.cmit.mvne.billing.infomanage.service;


import static com.cmit.mvne.billing.infomanage.common.InfoManageErrorConstant.MYSQL_INSERT_FAILED_CODE;
import static com.cmit.mvne.billing.infomanage.common.InfoManageErrorConstant.REDIS_LPUSH_FAILED_CODE;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.cmit.mvne.billing.infomanage.common.DistributeLock;
import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.infomanage.entity.CmUserDetail;
import com.cmit.mvne.billing.infomanage.entity.IOrdOrder;
import com.cmit.mvne.billing.infomanage.entity.IUser;
import com.cmit.mvne.billing.infomanage.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangxm02
 * @Description: 处理补换卡业务的信息同步功能
 * @Date: Create in 2019/12/23 16:46
 */
@Service
@Slf4j
public class SyncChangeCardOrder  {
	
	@Value(value = "${yellow-mobile.redis.redisson.wait-time}")
    int waitTime;
    @Value(value = "${yellow-mobile.redis.redisson.lease-time}")
    int leaseTime;
    
    @Autowired
    private DistributeLock distributeLock;
    
    @Autowired
    private CmUserDetailService cmUserDetailService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
	
	//@Override
	public void sync(IOrdOrder iOrdOrder,IUser iUser) throws MvneException{
        System.out.println(Thread.currentThread().getName() + "-changecard- " + iOrdOrder.getOrderId());
		Long orderId = iOrdOrder.getOrderId();
		//Long userId = iOrdOrder.getUserId();
        log.info("Sync change card order : {}", orderId);
        
        // 同一个号码所有操作的key都是一样的
        String lockKey = "InfoManageKey:" + iOrdOrder.getUserId();
        // 尝试使用redisson对号码加锁操作，同步方式，公平锁，线程退出的话时间会久一点，如果影响性能可以改成一般的锁
        // 如果无法获取到锁则直接抛异常
        boolean acquiredLock = distributeLock.fairLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);
        
        if(acquiredLock) {
        	// 根据orderId查询i_user，业务逻辑上只会有一条
            //List<IUser> iUserList = iUserService.selectByOrderIdAndUserId(orderId, userId);
            try {
                syncDB(iUser);
            }catch(Exception e) {
                log.error("Sync DB error! order is {}",orderId);
                log.error(iOrdOrder.toString() + "_" + e.getMessage(), e);
                distributeLock.unlock(lockKey);
                throw new MvneException(MYSQL_INSERT_FAILED_CODE, StringUtils.getExceptionText(e).substring(0, 1023));
            }
            // 同步到redis
            try {
                syncRedis(iUser);
            }catch(Exception e) {
                log.error("Sync Redis error! order is {}",orderId);
                log.error(iOrdOrder.toString() + "_" + e.getMessage(), e);

                distributeLock.unlock(lockKey);

                throw new MvneException(REDIS_LPUSH_FAILED_CODE, StringUtils.getExceptionText(e).substring(0, 1023));
            }
            distributeLock.unlock(lockKey);
        }
        return ;
	}
	
	private void syncDB(IUser iUser) throws Exception{

	    cmUserDetailService.updateChangeCard(new CmUserDetail(iUser), iUser.getUserId());

	}
	
	//这个需要再研究一下redis相关的操作
	private void syncRedis(IUser iUser) throws Exception {
        // 同步数据到redis
        String userTableKey = "UserDetail:" + iUser.getMsisdn();

        // 在配置的地方开启事务即可，无需显式开启
        CmUserDetail cmUserDetail = new CmUserDetail(iUser);
        String userTableValue = cmUserDetail.toJsonString();
        redisTemplate.opsForList().leftPop(userTableKey);
        redisTemplate.opsForList().leftPush(userTableKey, userTableValue);
    }
	
}
