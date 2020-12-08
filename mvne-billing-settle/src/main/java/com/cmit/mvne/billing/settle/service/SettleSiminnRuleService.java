package com.cmit.mvne.billing.settle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.mvne.billing.settle.entity.SettleSiminnRule;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/13
 */
public interface SettleSiminnRuleService extends IService<SettleSiminnRule> {

    List<SettleSiminnRule> selectNotExpireByItemName(String itemName);
}
