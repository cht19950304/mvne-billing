package com.cmit.mvne.billing.settle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.mvne.billing.settle.dao.SettleSiminnRuleMapper;
import com.cmit.mvne.billing.settle.entity.SettleSiminnRule;
import com.cmit.mvne.billing.settle.service.SettleSiminnRuleService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/13
 */
@Service
public class SettleSiminnRuleServiceImpl extends ServiceImpl<SettleSiminnRuleMapper, SettleSiminnRule> implements SettleSiminnRuleService {
    @Override
    public List<SettleSiminnRule> selectNotExpireByItemName(String itemName) {
        return getBaseMapper().selectNotExpireByItemName(itemName);
    }
}
