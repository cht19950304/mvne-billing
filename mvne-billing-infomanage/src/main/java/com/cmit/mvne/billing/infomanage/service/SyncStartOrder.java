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
 * @Description:
 * @Date: Create in 2019/12/18 19:23
 */

@Service
@Slf4j
public class SyncStartOrder {

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
            log.error("SyncStartOrder-sync error! order is {}",iOrdOrder.getOrderId());
            log.error("SyncStartOrder-sync iOrdOrder is : {} , error message : {}" , iOrdOrder.toString() , StringUtils.getExceptionText(e).substring(0, 1023));
            //distributeLock.unlock(lockKey);
            //throw new MvneException(MYSQL_INSERT_FAILED_CODE, StringUtils.getExceptionText(e).substring(0, 1023));
            if ( e instanceof MvneException ){
                throw new MvneException(((MvneException) e).getErrCode(),((MvneException) e).getErrDesc());
            }else if (e instanceof QueryTimeoutException) {
                log.error("SyncStartOrder-sync update timeout !");
                throw new MvneException(MYSQL_UPDATE_TIMEOUT_CODE, StringUtils.getExceptionText(e).substring(0, 1023));
            } else {
                throw new MvneException(MYSQL_UPDATE_FAILED_CODE,StringUtils.getExceptionText(e).substring(0, 1023));
            }
        }

    }

    private void syncDBRedis(IOrdOrder iOrdOrder,IUser iUser) throws Exception {
        log.info("SyncStartOrder-sync-syncDBRedis start ! iUser is : {}",iUser.getUserId());
        //???????????????
        iOrdOrderService.insert(iOrdOrder);
        //?????????????????????????????????
        List<CmUserDetail> cmUserDetailList = cmUserDetailService.updateByMsisdn(iUser.getMsisdn(),new CmUserDetail(iUser));
        log.info("SyncStartOrder-sync-syncDBRedis update after cmUserDetailList is {}",cmUserDetailList);
        if ( cmUserDetailList.size() > 0 )
        {
            //??????????????????????????????????????????????????????????????????"2099-12-31 23:59:59"???user_status?????????
            //cmUserDetailService.insert(iUser.getMsisdn(),new CmUserDetail(iUser));
            log.info("SyncStartOrder-sync-syncDBRedis insert CmUserDetail ! CmUserDetail is : {}",new CmUserDetail(iUser));
        }else {
            log.error("SyncStartOrder-sync-syncDBRedis start user update expireDate failed! userId is {}",iUser.getUserId());
            throw new MvneException(MYSQL_UPDATE_FAILED_CODE,"start user update expireDate failed!");
        }

        log.info("SyncStartOrder-sync-syncDBRedis end ! iUser is : {}",iUser.getUserId());
    }

}
