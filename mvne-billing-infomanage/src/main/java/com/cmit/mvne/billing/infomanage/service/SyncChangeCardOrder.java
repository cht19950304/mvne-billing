/**
 * 
 */
package com.cmit.mvne.billing.infomanage.service;

import java.util.Date;
import java.util.List;

import com.cmit.mvne.billing.infomanage.remote.service.CreditControlService;
import com.cmit.mvne.billing.user.analysis.service.CmUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.stereotype.Service;

import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.user.analysis.entity.CmUserDetail;
import com.cmit.mvne.billing.infomanage.entity.IOrdOrder;
import com.cmit.mvne.billing.user.analysis.entity.IUser;
import com.cmit.mvne.billing.infomanage.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import static com.cmit.mvne.billing.infomanage.common.InfoManageErrorConstant.*;

/**
 * @author jiangxm02
 * @Description: 处理补换卡业务的信息同步功能
 * @Date: Create in 2019/12/23 16:46
 */
@Service
@Slf4j
public class SyncChangeCardOrder  {
    
    @Autowired
    private CmUserDetailService cmUserDetailService;
    @Autowired
    private IOrdOrderService iOrdOrderService;
    @Autowired
    private CreditControlService creditControlService;

    @Transactional(rollbackFor = Exception.class)
    public void sync(IOrdOrder iOrdOrder,IUser iUser) throws MvneException{
        try {
            syncDBRedis(iOrdOrder,iUser);
            //查询余额，如果余额为0，则调用信控接口停机
            creditControlService.CreditControlChangeCardSms(iOrdOrder,iUser);
        }catch(Exception e) {
            log.error("SyncChangeCardOrder-sync error! order is {}",iOrdOrder.getOrderId());
            log.error("SyncChangeCardOrder-sync iOrdOrder is : {} , error message : {}" , iOrdOrder.toString() , StringUtils.getExceptionText(e).substring(0, 1023));
            //distributeLock.unlock(lockKey);
            //throw new MvneException(MYSQL_INSERT_FAILED_CODE, StringUtils.getExceptionText(e).substring(0, 1023));
            if ( e instanceof MvneException ){
                throw new MvneException(((MvneException) e).getErrCode(),((MvneException) e).getErrDesc());
            }else if (e instanceof QueryTimeoutException) {
                log.error("SyncChangeCardOrder-sync-syncDBRedis update timeout !");
                throw new MvneException(MYSQL_UPDATE_TIMEOUT_CODE, StringUtils.getExceptionText(e).substring(0, 1023));
            } else {
                throw new MvneException(MYSQL_UPDATE_FAILED_CODE,StringUtils.getExceptionText(e).substring(0, 1023));
            }
        }

    }
    private void syncDBRedis(IOrdOrder iOrdOrder,IUser iUser) throws MvneException{
        log.info("SyncChangeCardOrder-sync-syncDBRedis update start ! iUser is : {}",iUser.getUserId());
        //插入订单表
        iOrdOrderService.insert(iOrdOrder);
        //更新IMSI，修改用户状态
        List<CmUserDetail> cmUserDetailList= cmUserDetailService.updateChangeCard(iUser.getMsisdn(),new CmUserDetail(iUser));
        log.info("SyncChangeCardOrder-sync-syncDBRedis update after cmUserDetailList is {}",cmUserDetailList);
        if ( cmUserDetailList.size() > 0 ){
            log.info("SyncChangeCardOrder-sync-syncDBRedis insert CmUserDetail ! CmUserDetail is : {}",new CmUserDetail(iUser));
        }else {
            log.error("SyncChangeCardOrder-sync-syncDBRedis update expireDate failed! userId is {}",iUser.getUserId());
            throw new MvneException(MYSQL_UPDATE_FAILED_CODE,"update expireDate failed!");
        }
        log.info("SyncChangeCardOrder-sync-syncDBRedis update end ! iUser is : {}",iUser.getUserId());
    }


	
}
