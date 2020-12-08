/**
 * 
 */
package com.cmit.mvne.billing.creditcontrol.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.mvne.billing.creditcontrol.entity.CreditControlSms;
import com.cmit.mvne.billing.creditcontrol.entity.CreditControlSmsTemp;
import com.cmit.mvne.billing.creditcontrol.mapper.CreditControlSmsTempMapper;
import com.cmit.mvne.billing.creditcontrol.service.ICreditControlSmsTempService;

/**
 * @author jiangxm02
 *
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CreditControlSmsTempServiceImpl extends ServiceImpl<CreditControlSmsTempMapper, CreditControlSmsTemp> 
implements ICreditControlSmsTempService {
	@Autowired
	private CreditControlSmsTempMapper creditControlSmsTempMapper;

	@Override
	public CreditControlSmsTemp findCrediControlSmsTemp(String msisdn, String reason) throws Exception {
		LambdaQueryWrapper<CreditControlSmsTemp> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(CreditControlSmsTemp::getMsisdn, msisdn);
		queryWrapper.eq(CreditControlSmsTemp::getReason, reason);
		CreditControlSmsTemp creditControlSmsTemp = creditControlSmsTempMapper.selectOne(queryWrapper);
		return creditControlSmsTemp;
	}

	@Override
	public void deleteCreditControlSmsTemp(String msisdn, String reason) throws Exception {
		LambdaQueryWrapper<CreditControlSmsTemp> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(CreditControlSmsTemp::getMsisdn, msisdn);
		queryWrapper.eq(CreditControlSmsTemp::getReason, reason);
		creditControlSmsTempMapper.delete(queryWrapper);
	}

	@Override
	public void createCreditControlSmsTemp(CreditControlSms creditControlSms) throws Exception {
		CreditControlSmsTemp creditControlSmsTemp = new CreditControlSmsTemp();
		creditControlSmsTemp.setMsisdn(creditControlSms.getMsisdn());
		creditControlSmsTemp.setReason(creditControlSms.getReason());
		creditControlSmsTemp.setCreateDate(new Date());
		creditControlSmsTempMapper.insert(creditControlSmsTemp);
	}

	@Override
	public CreditControlSmsTemp findCreditControlSmsTempByList(String msisdn, List<String> reasonList)
			throws Exception {
		LambdaQueryWrapper<CreditControlSmsTemp> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(CreditControlSmsTemp::getMsisdn, msisdn);
		queryWrapper.in(CreditControlSmsTemp::getReason, reasonList);
		CreditControlSmsTemp creditControlSmsTemp = creditControlSmsTempMapper.selectOne(queryWrapper);
		return creditControlSmsTemp;
	}

}
