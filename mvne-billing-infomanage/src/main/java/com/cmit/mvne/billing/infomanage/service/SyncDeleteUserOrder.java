package com.cmit.mvne.billing.infomanage.service;

import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.user.analysis.entity.CmProdInsInfo;
import com.cmit.mvne.billing.infomanage.entity.IOrdOrder;
import com.cmit.mvne.billing.user.analysis.entity.CmUserDetail;
import com.cmit.mvne.billing.user.analysis.entity.IUser;
import com.cmit.mvne.billing.infomanage.util.StringUtils;

import com.cmit.mvne.billing.user.analysis.service.ApsFreeResService;
import com.cmit.mvne.billing.user.analysis.service.CmProdInsInfoService;
import com.cmit.mvne.billing.user.analysis.service.CmUserDetailService;
import com.cmit.mvne.billing.user.analysis.service.FreeResService;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cmit.mvne.billing.infomanage.common.InfoManageErrorConstant.*;

/**
 * @Author: luxf
 * @Description:进行销户业务的信息同步功能
 * @Date: Create in 2019/12/3 19:20
 */

@Service
@Slf4j
public class SyncDeleteUserOrder  {

    @Autowired
	FreeResService freeResService;
    @Autowired
	CmUserDetailService cmUserDetailService;
    @Autowired
	CmProdInsInfoService cmProdInsInfoService;
    @Autowired
	ApsFreeResService apsFreeResService;
	@Autowired
	private IOrdOrderService iOrdOrderService;

	@Transactional(rollbackFor = Exception.class)
	public void sync(IOrdOrder iOrdOrder,IUser iUser) throws MvneException{
		try {
			//暂时具体的逻辑还没有确定，所以该方法暂时没有正确的实现
			syncDBRedis(iOrdOrder,iUser);
		}catch(Exception e) {
			log.error("SyncDeleteUserOrder-sync error! order is {}",iOrdOrder.getOrderId());
			log.error("SyncDeleteUserOrder-sync iOrdOrder is : {} , error message : {}" , iOrdOrder.toString() , StringUtils.getExceptionText(e).substring(0, 1023));
			//distributeLock.unlock(lockKey);
			//throw new MvneException(MYSQL_INSERT_FAILED_CODE, StringUtils.getExceptionText(e).substring(0, 1023));
			if ( e instanceof MvneException ){
				throw new MvneException(((MvneException) e).getErrCode(),((MvneException) e).getErrDesc());
			}else if (e instanceof QueryTimeoutException) {
				log.error("SyncDeleteUserOrder-sync update timeout !");
				throw new MvneException(MYSQL_UPDATE_TIMEOUT_CODE, StringUtils.getExceptionText(e).substring(0, 1023));
			} else {
				throw new MvneException(MYSQL_UPDATE_FAILED_CODE,StringUtils.getExceptionText(e).substring(0, 1023));
			}
		}
	}
	private void syncDBRedis(IOrdOrder iOrdOrder,IUser iUser) throws MvneException{
		log.info("SyncDeleteUserOrder-sync-syncDBRedis start ! iUser is : {}",iUser.getUserId());
		log.info("SyncDeleteUserOrder-sync-syncDBRedis update user prod freeRes table expireDate ! iUser is : {}",iUser.getUserId());
		//插入订单表
		iOrdOrderService.insert(iOrdOrder);

		//获取用户信息，查看是否存在正常的用户信息
		List<CmUserDetail> cmUserDetailList1 = cmUserDetailService.findAllByMsisdn(iUser.getMsisdn());
		if ( cmUserDetailList1.size() == 0 ){
			log.error("SyncDeleteUserOrder-sync-syncDBRedis delete user is not exist ! cmUserDetailList1 is {}",cmUserDetailList1);
			throw new MvneException(MYSQL_SELECT_FAILED_CODE,"delete user is not exist!");
		}
		List<CmProdInsInfo> cmProdInsInfoList1 = cmProdInsInfoService.findAllByUserId(iUser.getUserId());
		if ( cmProdInsInfoList1.size() == 0 ){
			log.info("SyncDeleteUserOrder-sync-syncDBRedis delete user prod is not order ! cmProdInsInfoList1 is {}",cmProdInsInfoList1);
			List<CmUserDetail> cmUserDetailList2 = cmUserDetailService.updateDeleteUserDetail(iUser.getMsisdn(),new CmUserDetail(iUser));
			if ( cmUserDetailList2.size() > 0  )
			{
				log.info("SyncDeleteUserOrder-sync-syncDBRedis Update Delete User Success ! ");
			}else
			{
				log.error("SyncDeleteUserOrder-sync-syncDBRedis Update Delete User failed ! ");
				throw new MvneException(MYSQL_UPDATE_FAILED_CODE,"Update Delete User failed!");
			}
		}else {
			List<CmUserDetail> cmUserDetailList2 = cmUserDetailService.updateDeleteUserDetail(iUser.getMsisdn(),new CmUserDetail(iUser));
			//将用户的失效时间至失效
			List<CmProdInsInfo> cmProdInsInfoList2 = cmProdInsInfoService.updateDeleteProdInsInfo(iUser.getUserId(),iUser.getValidDate());
			//apsBalanceFeeMapper.updateBalanceFee(iUser.getUserId(),iUser.getValidDate());
			int freeCount = apsFreeResService.updateDeleteFreeRes(iUser.getUserId(),iUser.getValidDate());
			log.info("SyncDeleteUserOrder-sync-syncDBRedis update after cmUserDetailList2 is {} , cmProdInsInfoList2 is {} , freeCount is {}",cmUserDetailList2,cmProdInsInfoList2,freeCount);
			if ( cmUserDetailList2.size() > 0 &&  cmProdInsInfoList2.size() > 0 && freeCount > 0 )
			{
				log.info("SyncDeleteUserOrder-sync-syncDBRedis Update Delete User Success ! ");
			}else
			{
				log.error("SyncDeleteUserOrder-sync-syncDBRedis Update Delete User failed ! ");
				throw new MvneException(MYSQL_UPDATE_FAILED_CODE,"Update Delete User failed!");
			}
		}

		log.info("SyncDeleteUserOrder-sync-syncDBRedis end ! iUser is : {}",iUser.getUserId());

	}
    
}
