package com.cmit.mvne.billing.creditcontrol.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.mvne.billing.creditcontrol.entity.CreditControlSms;
import com.cmit.mvne.billing.creditcontrol.entity.CreditControlSmsTemp;

/**
 *  Service接口
 *
 * @author jiangxm
 * @date 2020-03-06 14:52:06
 */
public interface ICreditControlSmsTempService extends IService<CreditControlSmsTemp> {
	
    /**
     * @Description 查询是否存在对应号码和对应原因的短信通知临时类型，用于控制是否是只发送一次
     * @param msisdn 号码
     * @param reason 触发短信提醒的原因
     * @return
     * @throws Exception
     */
    CreditControlSmsTemp findCrediControlSmsTemp(String msisdn,String reason) throws Exception;
    
    /**
     * @Description 查询是否存在对应号码和对应原因列表的短信通知临时类型，用于控制是否只发送一次
     * @param msisdn
     * @param reasonList
     * @return
     * @throws Exception
     */
    CreditControlSmsTemp findCreditControlSmsTempByList(String msisdn,List<String> reasonList) throws Exception;
    
    /**
     * @Description 删除指定号码和触发短信通知原因的限制条件
     * @param msisdn 指定号码
     * @param reason 指定短信通知限制条件
     * @throws Exception
     */
    void deleteCreditControlSmsTemp(String msisdn,String reason) throws Exception;
    
    /**
     * @Description 再credit_control_sms_temp表中插入数据
     * @param creditControlSms
     * @throws Exception
     */
    void createCreditControlSmsTemp(CreditControlSms creditControlSms) throws Exception;
}
