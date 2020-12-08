package com.cmit.mvne.billing.infomanage.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cmit.mvne.billing.infomanage.common.DistributeLock;
import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.infomanage.entity.*;
import com.cmit.mvne.billing.infomanage.util.StringUtils;
import com.cmit.mvne.billing.user.analysis.entity.ApsBalanceFee;
import com.cmit.mvne.billing.user.analysis.entity.ApsFreeRes;
import com.cmit.mvne.billing.user.analysis.entity.FreeRes;
import com.cmit.mvne.billing.user.analysis.mapper.ApsBalanceFeeMapper;
import com.cmit.mvne.billing.user.analysis.mapper.ApsFreeResMapper;
import com.cmit.mvne.billing.user.analysis.mapper.FreeResMapper;
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
 * @Date: Create in 2019/12/23 14:42
 */

@Service
@Slf4j
public class SyncChangeOfferOrder  {

    @Value(value = "${yellow-mobile.redis.redisson.wait-time}")
    int waitTime;
    @Value(value = "${yellow-mobile.redis.redisson.lease-time}")
    int leaseTime;

    @Autowired
    private DistributeLock distributeLock;
    @Autowired
    private CmProdInsInfoService cmProdInsInfoService;
    @Autowired
    private ApsFreeResMapper apsFreeResMapper;
    @Autowired
    private FreeResMapper freeResMapper;
    @Autowired
    private ApsBalanceFeeMapper apsBalanceFeeMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    //@Override
    public void sync(IOrdOrder iOrdOrder,IProd iProd) throws MvneException {
        System.out.println(Thread.currentThread().getName() + "-changeoffer- " + iOrdOrder.getOrderId());

        Long orderId = iOrdOrder.getOrderId();
        log.info("Sync charge offer user order : {}", orderId);

        // 同一个号码所有操作的key都是一样的
        String lockKey = "InfoManageKey:" + iOrdOrder.getUserId();
        // 尝试使用redisson对号码加锁操作，同步方式，公平锁，线程退出的话时间会久一点，如果影响性能可以改成一般的锁
        boolean acquiredLock = distributeLock.fairLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);
        if (acquiredLock) {
            //List<IProd> iProdList = iProdService.selectByOrderId(orderId);
            try {
                //syncDB(iUserList, iProdList);
                syncDB(iProd);
            } catch (MvneException e) {
                log.error("Sync DB error! order is {}",orderId);
                log.error(iOrdOrder.toString() + "_" + e.getMessage(), e);

                distributeLock.unlock(lockKey);

                throw new MvneException(MYSQL_INSERT_FAILED_CODE, StringUtils.getExceptionText(e).substring(0, 1023));
            }
            // ͬ同步redis
            try {
                syncRedis(iProd);
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
     * 同步到DB
     * @param iProd
     * @throws Exception
     */
    private void syncDB(IProd iProd) throws MvneException{
        // 同步数据到mysql
        //订购产品同步
        cmProdInsInfoService.insert(new CmProdInsInfo(iProd));

        //免费资源同步
        //List<FreeRes> freeResList = (List<FreeRes>) freeResMapper.selectById(iProd.getProductId());
        List<FreeRes> freeResList = freeResMapper.selectByProductId(iProd.getProductId());
        for(FreeRes freeRes : freeResList) {
            apsFreeResMapper.insert(new ApsFreeRes(freeRes,iProd.getUserId(),iProd.getValidDate(),iProd.getExpireDate()));
        }

        //更新余额
        QueryWrapper<ApsBalanceFee> queryWrapper = new  QueryWrapper<>();
        queryWrapper.eq("USER_ID",iProd.getUserId());
        int userIdCount = apsBalanceFeeMapper.selectCount(queryWrapper);
        System.out.println("userIdCount:"+userIdCount);

        if ( userIdCount == 0 ) {
            ApsBalanceFee apsBalanceFee = new ApsBalanceFee(iProd.getUserId(),iProd.getProductFee().negate(), new BigDecimal("10403"),new Date());
            apsBalanceFeeMapper.insert(apsBalanceFee);
        }else if( userIdCount > 0)
        {
            ApsBalanceFee apsBalanceFeeOld = apsBalanceFeeMapper.selectByUserId(iProd.getUserId());
            BigDecimal remain_FeeOld = apsBalanceFeeOld.getRemainFee();
            BigDecimal remain_FeeNew = remain_FeeOld.subtract(iProd.getProductFee());
            ApsBalanceFee apsBalanceFeeNew = new ApsBalanceFee(iProd.getUserId(),remain_FeeNew,apsBalanceFeeOld.getMesureId(),new Date());
            apsBalanceFeeMapper.insert(apsBalanceFeeNew);
        }



    }
    private void syncRedis(IProd iProd) throws MvneException{
        String prodTableKey = "ProdInsInfo:" + iProd.getUserId();

        //插入ProdInsInfo:UserId
        CmProdInsInfo cmProdInsInfo = new CmProdInsInfo(iProd);
        String prodTableValue = cmProdInsInfo.toJsonString();
        redisTemplate.opsForList().leftPush(prodTableKey, prodTableValue);

        //生成用户的免费资源记录到FreeRes:userId:productInsId:itemId
        List<FreeRes> freeResList = freeResMapper.selectByProductId(iProd.getProductId());
        for(FreeRes freeRes : freeResList) {
            String freeReskey = "FreeRes:" +  iProd.getUserId() + ":" + iProd.getProductInsId() + ":" + freeRes.getFreeresItem();
            ApsFreeRes apsFreeRes = new ApsFreeRes(freeRes,iProd.getUserId(),iProd.getValidDate(),iProd.getExpireDate());
            String freeResTableValue= apsFreeRes.toJsonString();
            redisTemplate.opsForList().leftPush(freeReskey,freeResTableValue);

        }

        //更新余额
        // ͬ获取key
        String userTableKey = "BalanceFee:" + iProd.getUserId();
        //获取最新记录
        List<ApsBalanceFee> apsBalanceFeeList= JSONObject.parseArray(redisTemplate.opsForList().range(userTableKey,0,0).toString(),ApsBalanceFee.class);
        System.out.println(apsBalanceFeeList.toString()+apsBalanceFeeList.size());
        if( apsBalanceFeeList.size() == 0 )
        {
            ApsBalanceFee apsBalanceFee=new ApsBalanceFee(iProd.getUserId(),iProd.getProductFee().negate(),new BigDecimal("10403"),new Date());
            redisTemplate.opsForList().leftPush(userTableKey, apsBalanceFee.toJsonString());
        }else if( apsBalanceFeeList.size() > 0 ){
            Boolean updateStatus = updateBalance(apsBalanceFeeList, iProd.getUserId(), iProd.getProductFee(), userTableKey);
            if (updateStatus) {
                log.info("Sync update balanceFee success! userId is {}",iProd.getUserId());
            } else {
                log.error("Sync update balanceFee failed! userId is {}",iProd.getUserId());
                throw new MvneException(MYSQL_UPDATE_FAILED_CODE,"update balanceFee failed!");
            }
        }


    }

    public Boolean updateBalance(List<ApsBalanceFee> apsBalanceFeeList,long userId,BigDecimal productFee,String userTableKey) throws MvneException{

        for(ApsBalanceFee apsBalanceFee:apsBalanceFeeList){
            if(apsBalanceFee.getUserId() == userId){
                BigDecimal remainFeeOld = apsBalanceFee.getRemainFee() ;
                BigDecimal remainFeeNew = remainFeeOld.subtract(productFee);
                System.out.println("remainFeeNew:"+remainFeeNew);
                apsBalanceFee.setRemainFee(remainFeeNew);
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
