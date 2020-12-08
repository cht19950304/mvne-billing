package com.cmit.mvne.billing.infomanage.service;

import com.cmit.mvne.billing.infomanage.common.DistributeLock;
import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.infomanage.entity.IOrdOrder;
import com.cmit.mvne.billing.infomanage.entity.IProd;
import com.cmit.mvne.billing.infomanage.entity.IUser;
import com.cmit.mvne.billing.infomanage.util.StringUtils;

import com.cmit.mvne.billing.user.analysis.entity.FreeRes;
import com.cmit.mvne.billing.user.analysis.mapper.FreeResMapper;
import lombok.extern.slf4j.Slf4j;

//import static com.cmit.mvne.billing.infomanage.common.InfoManageErrorConstant.ITABLE_NOT_FOUND_CODE;
import static com.cmit.mvne.billing.infomanage.common.InfoManageErrorConstant.MYSQL_INSERT_FAILED_CODE;
import static com.cmit.mvne.billing.infomanage.common.InfoManageErrorConstant.REDIS_LPUSH_FAILED_CODE;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author: caikunchi
 * @Description:进行销户业务的信息同步功能
 * @Date: Create in 2019/12/3 19:20
 */

@Service
@Slf4j
public class SyncDeleteUserOrder  {
	
	@Value(value = "${yellow-mobile.redis.redisson.wait-time}")
    int waitTime;
    @Value(value = "${yellow-mobile.redis.redisson.lease-time}")
    int leaseTime;
    
    @Autowired
    private DistributeLock distributeLock;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
	private IProdService iProdService;
    @Autowired
	private FreeResMapper freeResMapper;
    
    //@Override
    public void sync(IOrdOrder iOrdOrder,IUser iUser) throws MvneException{
        System.out.println(Thread.currentThread().getName() + "-deleteuser- " + iOrdOrder.getOrderId());
    	Long orderId = iOrdOrder.getOrderId();
    	Long userId = iOrdOrder.getUserId();
        log.info("Sync delete user order : {}", orderId);
        
        // 同一个号码所有操作的key都是一样的
        String lockKey = "InfoManageKey:" + iOrdOrder.getUserId();
        // 尝试使用redisson对号码加锁操作，同步方式，公平锁，线程退出的话时间会久一点，如果影响性能可以改成一般的锁
        // 如果无法获取到锁则直接抛异常
        boolean acquiredLock = distributeLock.fairLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);
        
        if(acquiredLock) {
        		try {
        			//暂时具体的逻辑还没有确定，所以该方法暂时没有正确的实现
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
/*		for (IUser iUser : iUserList) {
			//修改cm_user_detail表的数据记录表示该用户被销户
			//cmUserDetailService.updateChangeCard(new CmUserDetail(iUser), iUser.getUserId());
			

        }*/
	}
	
	//这个需要再研究一下redis相关的操作
	private void syncRedis(IUser iUser) throws Exception {
			// 同步数据到redis，删除再redis中的相关数据
	        String userTableKey = "UserDetail:" + iUser.getMsisdn();
	        long userDetailSize = redisTemplate.opsForList().size(userTableKey);
	        while(userDetailSize != 0l) {
	        	redisTemplate.opsForList().leftPop(userTableKey);
	        	userDetailSize--;
	        }
	        
	        String prodInsInfoKey = "ProdInsInfo:" + iUser.getUserId();
	        long prodInsInfoSize = redisTemplate.opsForList().size(prodInsInfoKey);
	        while(prodInsInfoSize != 0l) {
	        	redisTemplate.opsForList().leftPop(prodInsInfoKey);
	        	prodInsInfoSize--;
	        }
	        
	        //免费资源记录：FreeRes:userId:productInsId:itemId这个也需要删除，但具体逻辑需要等待计费侧设计完成后再补充
			List<IProd> iProdList = iProdService.selectByUserId(iUser.getUserId());
	        for ( IProd iProd : iProdList)
			{
				Long product_Id = iProd.getProductId();
				List<FreeRes> freeResList = freeResMapper.selectByProductId(product_Id);
				for ( FreeRes freeRes : freeResList)
				{
					String freeResTableKey = "FreeRes:" + iUser.getUserId() + ":" + iProd.getProductInsId() + ":" + freeRes.getFreeresItem();
					redisTemplate.opsForList().leftPop(freeResTableKey);
				}
			}
	        
	        //用户余额表：BalanceFee:userId
			String balanceTableKey = "BalanceFee:" + iUser.getUserId();
	        long balanceSize = redisTemplate.opsForList().size(balanceTableKey);
	        while(balanceSize != 0l)
			{
				redisTemplate.opsForList().leftPop(balanceTableKey);
				balanceSize--;
			}
    }
    
}
