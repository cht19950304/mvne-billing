/**
 * 
 */
package com.cmit.mvne.billing.infomanage.crm.service.impl;

import com.cmit.mvne.billing.infomanage.common.DistributeLock;
import com.cmit.mvne.billing.infomanage.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.infomanage.crm.service.InfoManageCrmService;
import com.cmit.mvne.billing.infomanage.entity.IOrdOrder;
import com.cmit.mvne.billing.user.analysis.entity.IProd;
import com.cmit.mvne.billing.user.analysis.entity.IUser;

import com.cmit.mvne.billing.infomanage.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author jiangxm02
 * @Description: 接受来自CRM侧的调用，完成CRM侧订单/用户/产品信息入中间表
 * @Date:Create in 2019/12/23 16:48
 */
@Service
@Slf4j
public class InfoManageCrmServiceImpl implements InfoManageCrmService {

	@Autowired
	SyncCreateUserOrder syncCreateUserOrder;
	@Autowired
	SyncStopOrder syncStopOrder;
	@Autowired
	SyncStartOrder syncStartOrder;
	@Autowired
	SyncChargeOrder syncChargeOrder;
	@Autowired
	SyncChangeOfferOrder syncChangeOfferOrder;
	@Autowired
	SyncChangeCardOrder syncChangeCardOrder;
	@Autowired
	SyncDeleteUserOrder syncDeleteUserOrder;
	@Autowired
	private DistributeLock distributeLock;

	@Value(value = "${yellow-mobile.redis.redisson.wait-time}")
	int waitTime;
	@Value(value = "${yellow-mobile.redis.redisson.lease-time}")
	int leaseTime;

	@Override
	//@Transactional(rollbackFor = Exception.class)
	public void createUser(IOrdOrder iOrdOrder, IProd iProd, IUser iUser) throws MvneException {
		
		try {
			log.info("InfoManageCrmServiceImpl-createUser iOrdOrder is {} , iProd is {} , iUser is {} ", iOrdOrder, iProd, iUser);
			log.info("InfoManageCrmServiceImpl-createUser create user order : {}", iOrdOrder.getOrderId());

			// 同一个号码所有操作的key都是一样的
			String lockKey = "InfoManageKey:" + iOrdOrder.getMsisdn();
			// 尝试使用redisson对号码加锁操作，同步方式，公平锁，线程退出的话时间会久一点，如果影响性能可以改成一般的锁
			// 如果无法获取到锁则直接抛异常
			//boolean acquiredLock = distributeLock.fairLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);
			//log.info("InfoManageCrmServiceImpl-createUser lockKey is : {} , acquiredLock status is : {}",lockKey,acquiredLock);
			//if (acquiredLock) {
			syncCreateUserOrder.sync(iOrdOrder, iUser, iProd);

			//}

			//}catch(Exception e) {
		}catch(MvneException e) {
			log.error("InfoManageCrmServiceImpl-createUser user in infoManage has exception : {}", StringUtils.getExceptionText(e));
			//throw new MvneException("500",StringUtils.getExceptionText(e));
			throw new MvneException(e.getErrCode(),e.getErrDesc());

		}
	}

	@Override
	//@Transactional(rollbackFor = Exception.class)
	public void changeOffer(IOrdOrder iOrdOrder, IProd iProd) throws MvneException {
		try {
			/*iOrdOrderMapper.insert(iOrdOrder);
			iProdMapper.insert(iProd);*/
			log.info("InfoManageCrmServiceImpl-changeOffer iOrdOrder is {} , iProd is {} ",iOrdOrder,iProd);
			log.info("InfoManageCrmServiceImpl-changeOffer charge offer user order : {}", iOrdOrder.getOrderId());

			// 同一个号码所有操作的key都是一样的
			//String lockKey = "InfoManageKey:" + iOrdOrder.getMsisdn();
			// 尝试使用redisson对号码加锁操作，同步方式，公平锁，线程退出的话时间会久一点，如果影响性能可以改成一般的锁
			//boolean acquiredLock = distributeLock.fairLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);
			//log.info("InfoManageCrmServiceImpl-changeOffer lockKey is : {} , acquiredLock status is : {}",lockKey,acquiredLock);

			//if (acquiredLock) {
			syncChangeOfferOrder.sync(iOrdOrder,iProd);
			//}

		}catch(MvneException e) {
			log.error("InfoManageCrmServiceImpl-changeOffer user in infoManage has exception : {}", StringUtils.getExceptionText(e));
			//throw new MvneException("500",StringUtils.getExceptionText(e));
			throw new MvneException(e.getErrCode(),e.getErrDesc());
		}
	}

	@Override
	//@Transactional(rollbackFor = Exception.class)
	public void stop(IOrdOrder iOrdOrder, IUser iUser) throws MvneException {
		try {
			/*iOrdOrderMapper.insert(iOrdOrder);
			iUserMapper.insert(iUser);*/
			log.info("InfoManageCrmServiceImpl-stop iOrdOrder is {} , iUser is {}",iOrdOrder,iUser);
			log.info("InfoManageCrmServiceImpl-stop stop user order : {}", iOrdOrder.getOrderId());

			// 同一个号码所有操作的key都是一样的
			String lockKey = "InfoManageKey:" + iOrdOrder.getMsisdn();
			// 尝试使用redisson对号码加锁操作，同步方式，公平锁，线程退出的话时间会久一点，如果影响性能可以改成一般的锁
			boolean acquiredLock = distributeLock.fairLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);
			log.info("InfoManageCrmServiceImpl-stop lockKey is : {} , acquiredLock status is {}",lockKey,acquiredLock);
			if (acquiredLock) {
				syncStopOrder.sync(iOrdOrder,iUser);
			}

		}catch(MvneException e) {
			log.error("InfoManageCrmServiceImpl-stop user in infoManage has exception : {}", StringUtils.getExceptionText(e));
			//throw new MvneException("500",StringUtils.getExceptionText(e));
			throw new MvneException(e.getErrCode(),e.getErrDesc());
		}
	}

	@Override
	//@Transactional(rollbackFor = Exception.class)
	public void start(IOrdOrder iOrdOrder, IUser iUser) throws MvneException {
		try {
			/*iOrdOrderMapper.insert(iOrdOrder);
			iUserMapper.insert(iUser);*/
			log.info("InfoManageCrmServiceImpl-start iOrdOrder is {} , iUser is {}",iOrdOrder,iUser);
			log.info("InfoManageCrmServiceImpl-start start user order : {}", iOrdOrder.getOrderId());

			// 同一个号码所有操作的key都是一样的
			String lockKey= "InfoManageKey:" + iOrdOrder.getMsisdn();
			// 尝试使用redisson对号码加锁操作，同步方式，公平锁，线程退出的话时间会久一点，如果影响性能可以改成一般的锁
			boolean acquiredLock = distributeLock.fairLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);
			log.info("InfoManageCrmServiceImpl-start lockKey is : {} , acquiredLock status is : {}",lockKey,acquiredLock);
			if (acquiredLock) {
				syncStartOrder.sync(iOrdOrder,iUser);
			}
		}catch(MvneException e) {
			log.error("InfoManageCrmServiceImpl-start user in infoManage has exception : {}", StringUtils.getExceptionText(e));
			//throw new MvneException("500",StringUtils.getExceptionText(e));
			throw new MvneException(e.getErrCode(),e.getErrDesc());
		}
	}

	@Override
	//@Transactional(rollbackFor = Exception.class)
	public void recharge(IOrdOrder iOrdOrder) throws MvneException {
		try {
			//iOrdOrderMapper.insert(iOrdOrder);
			log.info("InfoManageCrmServiceImpl-recharge iOrdOrder is {} ",iOrdOrder);
			log.info("InfoManageCrmServiceImpl-recharge charge user order : {}", iOrdOrder.getOrderId());

			// 同一个号码所有操作的key都是一样的
			//String lockKey = "InfoManageKey:" + iOrdOrder.getMsisdn();
			// 尝试使用redisson对号码加锁操作，同步方式，公平锁，线程退出的话时间会久一点，如果影响性能可以改成一般的锁
			//boolean acquiredLock = distributeLock.fairLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);
			//log.info("InfoManageCrmServiceImpl-recharge lockKey is : {} , acquiredLock status is : {}",lockKey,acquiredLock);
			//if (acquiredLock) {
			syncChargeOrder.sync(iOrdOrder);
			//}
		}catch(MvneException e) {
			log.error("InfoManageCrmServiceImpl-recharge user in infoManage has exception : {}",StringUtils.getExceptionText(e));
			//throw new MvneException("500",StringUtils.getExceptionText(e));
			throw new MvneException(e.getErrCode(),e.getErrDesc());
		}
	}

	@Override
	//@Transactional(rollbackFor = Exception.class)
	public void deleteUser(IOrdOrder iOrdOrder, IUser iUser) throws MvneException {
		try {
			/*iOrdOrderMapper.insert(iOrdOrder);
			iUserMapper.insert(iUser);*/
			log.info("InfoManageCrmServiceImpl-deleteUser iOrdOrder is {} , iUser is {}",iOrdOrder,iUser);
			log.info("InfoManageCrmServiceImpl-deleteUser delete user order : {}", iOrdOrder.getOrderId());

			// 同一个号码所有操作的key都是一样的
			String lockKey = "InfoManageKey:" + iOrdOrder.getMsisdn();
			// 尝试使用redisson对号码加锁操作，同步方式，公平锁，线程退出的话时间会久一点，如果影响性能可以改成一般的锁
			// 如果无法获取到锁则直接抛异常
			boolean acquiredLock = distributeLock.fairLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);
			log.info("InfoManageCrmServiceImpl-deleteUser lockKey is : {} , acquiredLock status is : {}",lockKey,acquiredLock);
			if(acquiredLock) {
				syncDeleteUserOrder.sync(iOrdOrder,iUser);
			}
		}catch(MvneException e) {
			log.error("InfoManageCrmServiceImpl-deleteUser user in infoManage has exception : {}", StringUtils.getExceptionText(e));
			//throw new MvneException("500",StringUtils.getExceptionText(e));
			throw new MvneException(e.getErrCode(),e.getErrDesc());
		}
	}

	@Override
	//@Transactional(rollbackFor = Exception.class)
	public void changeCard(IOrdOrder iOrdOrder, IUser iUser) throws MvneException {
		try {
			/*iOrdOrderMapper.insert(iOrdOrder);
			iUserMapper.insert(iUser);*/
			log.info("InfoManageCrmServiceImpl-changeCard iOrdOrder is {} , iUser is {}",iOrdOrder,iUser);
			log.info("InfoManageCrmServiceImpl-changeCard change card user order : {}", iOrdOrder.getOrderId());

			// 同一个号码所有操作的key都是一样的
			String lockKey = "InfoManageKey:" + iOrdOrder.getMsisdn();
			// 尝试使用redisson对号码加锁操作，同步方式，公平锁，线程退出的话时间会久一点，如果影响性能可以改成一般的锁
			// 如果无法获取到锁则直接抛异常
			boolean acquiredLock = distributeLock.fairLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);
			log.info("InfoManageCrmServiceImpl-changeCard lockKey is : {} , acquiredLock status is {}",lockKey,acquiredLock);
			if(acquiredLock) {
				syncChangeCardOrder.sync(iOrdOrder,iUser);
			}
		}catch(MvneException e) {
			log.error("InfoManageCrmServiceImpl-changeCard user in infoManage has exception : {}", StringUtils.getExceptionText(e));
			//throw new MvneException("500",StringUtils.getExceptionText(e));
			throw new MvneException(e.getErrCode(),e.getErrDesc());
		}
	}

}
