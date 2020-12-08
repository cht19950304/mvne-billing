/**
 * 
 */
package com.cmit.mvne.billing.infomanage.crm.service.impl;

import com.cmit.mvne.billing.infomanage.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmit.mvne.billing.infomanage.common.MvneException;
import com.cmit.mvne.billing.infomanage.crm.service.InfoManageCrmService;
import com.cmit.mvne.billing.infomanage.entity.IOrdOrder;
import com.cmit.mvne.billing.infomanage.entity.IProd;
import com.cmit.mvne.billing.infomanage.entity.IUser;
import com.cmit.mvne.billing.infomanage.mapper.IOrdOrderMapper;
import com.cmit.mvne.billing.infomanage.mapper.IProdMapper;
import com.cmit.mvne.billing.infomanage.mapper.IUserMapper;

import com.cmit.mvne.billing.infomanage.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangxm02
 * @Description: 接受来自CRM侧的调用，完成CRM侧订单/用户/产品信息入中间表
 * @Date:Create in 2019/12/23 16:48
 */
@Service
@Slf4j
public class InfoManageCrmServiceImpl implements InfoManageCrmService {
	
	@Autowired
    private IOrdOrderMapper iOrdOrderMapper;
	
	@Autowired
	private IProdMapper iProdMapper;
	
	@Autowired
	private IUserMapper iUserMapper;

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


	@Override
	@Transactional(rollbackFor = Exception.class)
	public void createUser(IOrdOrder iOrdOrder, IProd iProd, IUser iUser) throws MvneException {
		
		try {
			iOrdOrderMapper.insert(iOrdOrder);
			iProdMapper.insert(iProd);
			iUserMapper.insert(iUser);
			syncCreateUserOrder.sync(iOrdOrder,iProd,iUser);
		}catch(Exception e) {
			log.error("Create User in InfoManage has Excetion:", e);
			throw new MvneException("500",StringUtils.getExceptionText(e));
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void changeOffer(IOrdOrder iOrdOrder, IProd iProd) throws MvneException {
		try {
			iOrdOrderMapper.insert(iOrdOrder);
			iProdMapper.insert(iProd);
			syncChangeOfferOrder.sync(iOrdOrder,iProd);
		}catch(Exception e) {
			log.error("Change Offer in InfoManage has Excetion:", e);
			throw new MvneException("500",StringUtils.getExceptionText(e));
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void stop(IOrdOrder iOrdOrder, IUser iUser) throws MvneException {
		try {
			iOrdOrderMapper.insert(iOrdOrder);
			iUserMapper.insert(iUser);
			syncStopOrder.sync(iOrdOrder,iUser);
		}catch(Exception e) {
			log.error("Stop and start user in InfoManage has Excetion:", e);
			throw new MvneException("500",StringUtils.getExceptionText(e));
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void start(IOrdOrder iOrdOrder, IUser iUser) throws MvneException {
		try {
			iOrdOrderMapper.insert(iOrdOrder);
			iUserMapper.insert(iUser);
			syncStartOrder.sync(iOrdOrder,iUser);
		}catch(Exception e) {
			log.error("Stop and start user in InfoManage has Excetion:", e);
			throw new MvneException("500",StringUtils.getExceptionText(e));
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void recharge(IOrdOrder iOrdOrder) throws MvneException {
		try {
			iOrdOrderMapper.insert(iOrdOrder);
			syncChargeOrder.sync(iOrdOrder);
		}catch(Exception e) {
			log.error("User recharge in InfoManage has Excetion:", e);
			throw new MvneException("500",StringUtils.getExceptionText(e));
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteUser(IOrdOrder iOrdOrder, IUser iUser) throws MvneException {
		try {
			iOrdOrderMapper.insert(iOrdOrder);
			iUserMapper.insert(iUser);
			syncDeleteUserOrder.sync(iOrdOrder,iUser);
		}catch(Exception e) {
			log.error("Delete user in InfoManage has Excetion:", e);
			throw new MvneException("500",StringUtils.getExceptionText(e));
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void changeCard(IOrdOrder iOrdOrder, IUser iUser) throws MvneException {
		try {
			iOrdOrderMapper.insert(iOrdOrder);
			iUserMapper.insert(iUser);
			syncChangeCardOrder.sync(iOrdOrder,iUser);
		}catch(Exception e) {
			log.error("User change card in InfoManage has Excetion:", e);
			throw new MvneException("500",StringUtils.getExceptionText(e));
		}
	}

}
