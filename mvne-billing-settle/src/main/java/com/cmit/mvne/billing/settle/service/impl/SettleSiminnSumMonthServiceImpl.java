package com.cmit.mvne.billing.settle.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.mvne.billing.settle.dao.SettleSiminnSumMonthMapper;
import com.cmit.mvne.billing.settle.entity.SettleSiminnSumMonth;
import com.cmit.mvne.billing.settle.service.SettleSiminnSumMonthService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/13
 */
@Service
public class SettleSiminnSumMonthServiceImpl extends ServiceImpl<SettleSiminnSumMonthMapper, SettleSiminnSumMonth> implements SettleSiminnSumMonthService {
    @Override
    public List<SettleSiminnSumMonth> findAllByInvoicingPeriod() {
        return null;
    }
}
