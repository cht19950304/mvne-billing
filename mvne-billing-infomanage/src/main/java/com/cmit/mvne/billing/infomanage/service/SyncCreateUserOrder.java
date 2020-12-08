package com.cmit.mvne.billing.infomanage.service;

import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.infomanage.entity.IOrdOrder;
import com.cmit.mvne.billing.user.analysis.entity.*;
import com.cmit.mvne.billing.infomanage.util.StringUtils;
import com.cmit.mvne.billing.user.analysis.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.cmit.mvne.billing.infomanage.common.InfoManageErrorConstant.*;

/**
 * @Author: luxf
 * @Description: 同步开户订单信息
 * @Date: Create in 2019/12/3 15:13
 */
@Service
@Slf4j
public class SyncCreateUserOrder {

    @Autowired
    private CmUserDetailService cmUserDetailService;
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

    /**
     * 同步数据到redis和DB，保证事务一致，只有用户资料和产品信息有缓存,免费资源和余额表只同步到mysql
     *
     * @param iOrdOrder 需要同步的订单
     * @return void
     */

    @Transactional(rollbackFor = Exception.class)
    public void sync( IOrdOrder iOrdOrder,IUser iUser,IProd iProd) throws MvneException{
        // 这里可以考虑一下顺序，是不是可以放在获取锁之前？
        try {
            syncDBRedis(iOrdOrder,iUser,iProd,iOrdOrder.getFactMoney());
        } catch (Exception e) {
            log.error("SyncCreateUserOrder-sync error! order is {}",iOrdOrder.getOrderId());
            log.error("SyncCreateUserOrder-sync iOrdOrder is : {} , error message : {}" , iOrdOrder.toString() , StringUtils.getExceptionText(e).substring(0, 1023));
            //distributeLock.unlock(lockKey);
            if ( e instanceof MvneException ){
                throw new MvneException(((MvneException) e).getErrCode(),((MvneException) e).getErrDesc());
            }else if (e instanceof QueryTimeoutException) {
                log.error("SyncCreateUserOrder-sync-syncDBRedis insert timeout !");
                throw new MvneException(MYSQL_INSERT_TIMEOUT_CODE, StringUtils.getExceptionText(e).substring(0, 1023));
            } else {
                throw new MvneException(MYSQL_INSERT_FAILED_CODE,StringUtils.getExceptionText(e).substring(0, 1023));
            }
        }
    }

    private void syncDBRedis(IOrdOrder iOrdOrder,IUser iUser, IProd iProd ,BigDecimal factMoney) throws MvneException {
        log.info("SyncCreateUserOrder-sync-syncDBRedis start ! iUser is : {} , factMoney is {} ", iUser.getUserId(), factMoney);

        //插入订单表
        iOrdOrderService.insert(iOrdOrder);
        // 同步用户数据到mysql,根据key从数据库中查询出全部对象存入缓存
        List<CmUserDetail> cmUserDetailList = cmUserDetailService.insert(iUser.getMsisdn(), new CmUserDetail(iUser));
        log.info("SyncCreateUserOrder-sync-syncDBRedis insert CmUserDetail ! CmUserDetail is : {}", new CmUserDetail(iUser));
        log.info("SyncCreateUserOrder-sync-syncDBRedis insert after cmUserDetailList is : {}", cmUserDetailList);

        if (cmUserDetailList.size() > 0) {
            log.info("SyncCreateUserOrder-sync-syncDBRedis insert CmUserDetail success !");
        } else {
            log.error("SyncCreateUserOrder-sync-syncDBRedis insert CmUserDetail failed ! find Valid User Info !");
            throw new MvneException(MYSQL_INSERT_FAILED_CODE, "insert CmUserDetail failed !");
        }

        //判断是否存在产品信息，如有则同步
        //if ( ! org.apache.commons.lang.StringUtils.isBlank(iProd.toString()) )
        if (null != iProd)
        {
            //免费资源同步，只同步到mysql
            List<ApsFreeRes> apsFreeResList = apsFreeResService.findValidFreeRes(iProd.getUserId());
            if ( apsFreeResList.size() > 0 ){
                log.error("SyncCreateUserOrder-sync-syncDBRedis have valid apsFreeResList , apsFreeResList is {}",apsFreeResList);
                throw new MvneException(MYSQL_SELECT_FAILED_CODE,"have valid apsFreeResList");
            }else {
                List<FreeRes> freeResList = freeResService.selectProduct(iProd.getProductId());
                log.info("SyncCreateUserOrder-sync-syncDBRedis freeResList is {}",freeResList);
                if ( freeResList.size() > 0 ){
                    for(FreeRes freeRes : freeResList) {
                        apsFreeResService.insert(new ApsFreeRes(freeRes,iProd.getProductInsId(),iProd.getUserId(),iProd.getValidDate(),iProd.getExpireDate()));
                    }
                }else {
                    log.error("SyncCreateUserOrder-sync-syncDBRedis select freeRes is null !");
                    throw new MvneException(MYSQL_SELECT_FAILED_CODE,"select freeRes is null !");
                }
            }

            //产品数据同步到mysql,根据key从数据库中查询出全部对象存入缓存
            List<CmProdInsInfo> cmProdInsInfoList = cmProdInsInfoService.insertCreate(iUser.getUserId(),new CmProdInsInfo(iProd));
            log.info("SyncCreateUserOrder-sync-syncDBRedis insert CmProdInsInfo ! CmProdInsInfo is : {}",new CmProdInsInfo(iProd));
            log.info("SyncCreateUserOrder-sync-syncDBRedis insert after cmProdInsInfoList is : {}",cmProdInsInfoList);
            if ( cmProdInsInfoList.size() > 0 ){
                log.info("SyncCreateUserOrder-sync-syncDBRedis insert CmProdInsInfo success !");
            }else {
                log.error("SyncCreateUserOrder-sync-syncDBRedis insert CmProdInsInfo failed ! find Valid iProd Info !");
                throw new MvneException(MYSQL_INSERT_FAILED_CODE,"insert CmProdInsInfo failed !");
            }

        }

        //同步余额信息,只同步到mysql
        //String balanceTableKey = "BalanceFee:" + iProd.getUserId();
        //Map balanceFeeMap=redisTemplate.opsForHash().entries(balanceTableKey);
        //log.info("SyncCreateUserOrder-sync-syncDBRedis balanceTableKey is {} , balanceFeeMap is {}", balanceTableKey,balanceFeeMap);

        ApsBalanceFee apsBalanceFee1 = apsBalanceFeeService.selectByUserId(iUser.getUserId());
        log.info("SyncCreateUserOrder-sync-syncDBRedis before apsBalanceFee1  is {} ",apsBalanceFee1);
        //充值金额,单位由欧元转为欧分
        BigDecimal balanceFee = new BigDecimal("0");
        if (null != iProd) {
                balanceFee = factMoney.subtract(iProd.getProductFee()).multiply(new BigDecimal(100));
        }else {
            if (null != factMoney ){
                balanceFee = factMoney.multiply(new BigDecimal(100));
            }
        }
        if ( apsBalanceFee1 == null ) {
            ApsBalanceFee apsBalanceFee2 = new ApsBalanceFee(iUser.getUserId(),balanceFee, new BigDecimal("10301"),new Date());
            apsBalanceFeeService.insert(apsBalanceFee2);
            log.info("SyncCreateUserOrder-sync-syncDBRedis insert apsBalanceFee ! apsBalanceFee is {}",apsBalanceFee2);
        }else
        {
            log.error("SyncCreateUserOrder-sync-syncDBRedis user is exist ! balance insert failed ! userId is {} ",iProd.getUserId());
            throw new MvneException(MYSQL_SELECT_FAILED_CODE,"userId is exist ! balance insert failed !");
        }
        log.info("SyncCreateUserOrder-sync-syncDBRedis end ! iUser is : {}",iUser.getUserId());
    }



}
