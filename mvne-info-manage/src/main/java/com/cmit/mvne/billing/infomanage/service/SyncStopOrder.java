package com.cmit.mvne.billing.infomanage.service;


import com.alibaba.fastjson.JSONObject;
import com.cmit.mvne.billing.infomanage.common.DistributeLock;
import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.infomanage.entity.CmUserDetail;
import com.cmit.mvne.billing.infomanage.entity.IOrdOrder;
import com.cmit.mvne.billing.infomanage.entity.IUser;
import com.cmit.mvne.billing.infomanage.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.cmit.mvne.billing.infomanage.common.InfoManageErrorConstant.*;
//import static com.cmit.mvne.billing.infomanage.common.InfoManageErrorConstant.ITABLE_NOT_FOUND_DESC;

/**
 * @Author: luxf
 * @Description:同步用户停机订单信息
 * @Date: Create in 2019/12/17 10:54
 */

@Service
@Slf4j
public class SyncStopOrder {

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
        System.out.println(Thread.currentThread().getName() + "-stop- " + iOrdOrder.getOrderId());

        Long orderId = iOrdOrder.getOrderId();
        log.info("Sync stop user order : {}", orderId);

        // 同一个号码所有操作的key都是一样的
        String lockKey = "InfoManageKey:" + iOrdOrder.getUserId();

        // 尝试使用redisson对号码加锁操作，同步方式，公平锁，线程退出的话时间会久一点，如果影响性能可以改成一般的锁
        boolean acquiredLock = distributeLock.fairLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);
        if (acquiredLock) {
                //同步DB数据库
                try {
                    syncDB(iUser);
                }catch (Exception e)
                {
                    log.error("Sync DB error! order is {}",orderId);
                    log.error(iOrdOrder.toString() + "_" + e.getMessage(), e);

                    distributeLock.unlock(lockKey);

                    throw new MvneException(MYSQL_INSERT_FAILED_CODE, StringUtils.getExceptionText(e).substring(0, 1023));
                }
                // 同步到redis
                try {
                    syncRedis(iUser);
                } catch (Exception e) {
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
     * 同步停机到DB
     * @param iUser
     * @throws Exception
     */
    public void syncDB(IUser iUser) throws MvneException{

        //修改用户资料的失效时间修改为当前时间
        int updateStatus=cmUserDetailService.updateExpire(new CmUserDetail(iUser).getUserId(), iUser.getMsisdn(),iUser.getValidDate(), "1");
        if(updateStatus>=1)
        {
            //增加一个值的记录，生效时间为当前，失效时间为"2099-12-31 00:00:00"，user_status为停机
            cmUserDetailService.insert(new CmUserDetail(iUser));
        }else {
            log.error("Sync update expireDate failed! userId is {}",iUser.getUserId());
            throw new MvneException(MYSQL_UPDATE_FAILED_CODE,"update expireDate failed!");
        }
    }
    /**
     * 同步停机信息到Redis
     * @param iUser
     * @throws Exception
     */
    public void syncRedis(IUser iUser) throws MvneException, ParseException {
        //获取用户资料key
        String userTableKey = "UserDetail:" + iUser.getMsisdn();

        // 在配置的地方开启事务即可，无需显式开启
        //获取最新的用户资料记录
        List<CmUserDetail> cmUserDetailList=JSONObject.parseArray(redisTemplate.opsForList().range(userTableKey,0,0).toString(), CmUserDetail.class);
        if (cmUserDetailList.size()>0){
            //更新失效时间
            Boolean updateStatus=updateExpireDate(cmUserDetailList,iUser.getUserId(),iUser.getMsisdn(),iUser.getValidDate(),userTableKey);
            if(updateStatus){
                //增加一个停机的记录,user_status为停机
                String userTableValue = new CmUserDetail(iUser).toJsonString();
                redisTemplate.opsForList().leftPush(userTableKey, userTableValue);
            }else {
                log.error("Sync update expireDate failed! userId is {}",iUser.getUserId());
                throw new MvneException(MYSQL_UPDATE_FAILED_CODE,"update expireDate failed!");
            }
        }else {
            log.error("Sync redis userId is not exit! userId is {}",iUser.getUserId());
            throw new MvneException();
        }

    }


    public Boolean updateExpireDate(List<CmUserDetail> cmUserDetailList,Long userId,String msisdn,Date validDate,String userTableKey) throws ParseException {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date expireDate=simpleDateFormat.parse("2099-12-31 00:00:00");

        for (CmUserDetail cmUserDetail : cmUserDetailList) {

            if (cmUserDetail.getUserId().equals(userId) && cmUserDetail.getMsisdn().equals(msisdn) && simpleDateFormat.format(cmUserDetail.getExpireDate()).equals(simpleDateFormat.format(expireDate)) && cmUserDetail.getUserStatus().equals("1")) {
                //更新失效时间
                cmUserDetail.setExpireDate(validDate);
                //弹出记录
                redisTemplate.opsForList().leftPop(userTableKey);
                //插入失效记录
                String cmUserDetailValue = cmUserDetail.toJsonString();
                redisTemplate.opsForList().leftPush(userTableKey, cmUserDetailValue);
                return true;
            }
        }
        return false;
    }

}
