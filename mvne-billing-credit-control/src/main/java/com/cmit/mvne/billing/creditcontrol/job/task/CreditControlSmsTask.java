/**
 * 
 */
package com.cmit.mvne.billing.creditcontrol.job.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cmit.mvne.billing.creditcontrol.service.ICreditControlSmsService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangxm02
 * @description 定时处理CREDIT_CONTROL_SMS表数据
 * @description 还需要在t_job表中进行配置
 */
@Slf4j
@Component
public class CreditControlSmsTask {
	
	@Autowired
	private ICreditControlSmsService creditControlSmsServiceImpl;
	
	//需要将这个方法配置到t_job表中，以及一个参数，就是表示每次需要处理的数据记录的条数
	public void creditControlSmsCrm(String params) {
		log.info("#####当前配置处理短信通知的定时任务每次处理的数据是:"+Integer.valueOf(params)+"条#####");
		try {
			creditControlSmsServiceImpl.dealCreditControlSmsAndCrm(Integer.valueOf(params));
		} catch (Exception e) {
			log.error("定时处理credit_control_sms表发送短信提醒并调用CRM停复机接口出现异常:",e);
		}
	}
	
	public void creditControlSms(String params) {
		log.info("@@@@@当前配置处理短信通知的定时任务每次处理的数据是:"+Integer.valueOf(params)+"条@@@@@");
		try {
			creditControlSmsServiceImpl.dealCreditControlSms(Integer.valueOf(params));
		} catch (Exception e) {
			log.error("定时处理credit_control_sms表发送短信提醒出现异常:",e);
		}
	}
	
	public void creditControlDataTransfer() {
		try {
			creditControlSmsServiceImpl.dataDelete();
		} catch (Exception e) {
			log.error("定时删除CREDIT_CONTROL_SMS表处理过的数据出现异常:",e);
		}
	}
	
	public void creditControlDeleteUser(String params) {
		try {
			creditControlSmsServiceImpl.deleteUser(Integer.valueOf(params));
		} catch (Exception e) {
			log.error("定时执行扫描CM_USER_DETAIL表处理自动销户任务出现异常:",e);
		}
	}
}
