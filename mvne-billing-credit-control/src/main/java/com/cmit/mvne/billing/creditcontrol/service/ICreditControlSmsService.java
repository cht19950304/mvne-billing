package com.cmit.mvne.billing.creditcontrol.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.mvne.billing.creditcontrol.entity.CreditControlSms;
import com.cmit.mvne.billing.creditcontrol.entity.MvneCrmResponse;
import com.cmit.mvne.billing.creditcontrol.remote.entity.StopAndStartDTO;

/**
 *  Service接口
 *
 * @author jiangxm
 * @date 2020-02-11 14:52:06
 */
public interface ICreditControlSmsService extends IService<CreditControlSms> {

    /**
     * @Description 新增
     * @param CreditControlSms creditControlSms
     * @throws Exception 
     */
    void createCreditControlSms(CreditControlSms creditControlSms) throws Exception;
    
    /**
     * @Description 处理CREDIT_CONTROL_SMS表中的数据，完成向短信网关发送短信，并根据条件调用CRM停复机接口
     * @param params 定义每次从数据库中取出多少条数据
     * @throws Exception 
     */
    void dealCreditControlSmsAndCrm(int params) throws Exception;
    
    /**
     * @Description 处理CREDIT_CONTROL_SMS表中的数据，完成向短信网关发送短信
     * @param params
     * @throws Exception 
     */
    void dealCreditControlSms(int params) throws Exception;
    
    /**
     * @throws Exception 
     * @Description 将状态为“2”和“3”的记录删除
     */
    void dataDelete() throws Exception;
    
    /**
     * @throws Exception 
     * @Description 定时执行任务对停机超过13个月的用户调用CRM销户接口进行自动销户
     */
    void deleteUser(int params) throws Exception;
    
//    public MvneCrmResponse stopAndStart(StopAndStartDTO stopAndStartDto) throws Exception;
}
