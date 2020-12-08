package com.cmit.mvne.billing.infomanage.service;

import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.user.analysis.entity.CmUserDetail;
import com.cmit.mvne.billing.infomanage.entity.IOrdOrder;
import com.cmit.mvne.billing.user.analysis.entity.IUser;
import com.cmit.mvne.billing.infomanage.util.StringUtils;
import com.cmit.mvne.billing.user.analysis.service.CmUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.cmit.mvne.billing.infomanage.common.InfoManageErrorConstant.*;

/**
 * @Author: luxf
 * @Description:同步用户停机订单信息
 * @Date: Create in 2019/12/17 10:54
 */

@Service
@Slf4j
public class SyncStopOrder {

    @Autowired
    private CmUserDetailService cmUserDetailService;
    @Autowired
    private IOrdOrderService iOrdOrderService;

    @Transactional(rollbackFor = Exception.class)
    public void sync(IOrdOrder iOrdOrder,IUser iUser) throws MvneException{
        try {
            syncDBRedis(iOrdOrder,iUser);
        }catch (Exception e)
        {
            log.error("SyncStopOrder-sync error! order is {}",iOrdOrder.getOrderId());
            log.error("SyncStopOrder-sync iOrdOrder is : {} , error message : {}" , iOrdOrder.toString() , StringUtils.getExceptionText(e).substring(0, 1023));
            //distributeLock.unlock(lockKey);
            //throw new MvneException(MYSQL_INSERT_FAILED_CODE, StringUtils.getExceptionText(e).substring(0, 1023));
            if ( e instanceof MvneException ){
                throw new MvneException(((MvneException) e).getErrCode(),((MvneException) e).getErrDesc());
            }else if (e instanceof QueryTimeoutException) {
                log.error("SyncStopOrder-sync update timeout !");
                throw new MvneException(MYSQL_UPDATE_TIMEOUT_CODE, StringUtils.getExceptionText(e).substring(0, 1023));
            } else {
                throw new MvneException(MYSQL_UPDATE_FAILED_CODE,StringUtils.getExceptionText(e).substring(0, 1023));
            }
        }

    }

    private void syncDBRedis(IOrdOrder iOrdOrder,IUser iUser) throws MvneException{
        log.info("SyncStopOrder-sync-syncDBRedis start ! iUser is : {}",iUser.getUserId());
        //插入订单表
        iOrdOrderService.insert(iOrdOrder);
        //修改用户资料的失效时间
        List<CmUserDetail> cmUserDetailList = cmUserDetailService.updateByMsisdn(iUser.getMsisdn(),new CmUserDetail(iUser));
        log.info("SyncStopOrder-sync-syncDBRedis update after cmUserDetailList is {}",cmUserDetailList);
        if ( cmUserDetailList.size() > 0 ){
            //增加一个值的记录，生效时间为当前，失效时间为"2099-12-31 23:59:59"，user_status为停机
            //cmUserDetailService.insert(iUser.getMsisdn(),new CmUserDetail(iUser));
            log.info("SyncStopOrder-sync-syncDBRedis insert CmUserDetail ! CmUserDetail is : {}",new CmUserDetail(iUser));
        }else {
            log.error("SyncStopOrder-sync-syncDBRedis stop user update expireDate failed! userId is {}",iUser.getUserId());
            throw new MvneException(MYSQL_UPDATE_FAILED_CODE,"stop user update expireDate failed!");
        }

        log.info("SyncStopOrder-sync-syncDBRedis end ! iUser is : {}",iUser.getUserId());
    }


}
