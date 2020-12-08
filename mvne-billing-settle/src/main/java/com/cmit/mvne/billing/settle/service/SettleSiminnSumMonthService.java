package com.cmit.mvne.billing.settle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.mvne.billing.settle.entity.SettleSiminnSumMonth;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/13
 */
public interface SettleSiminnSumMonthService extends IService<SettleSiminnSumMonth> {
    List<SettleSiminnSumMonth> findAllByInvoicingPeriod();
}
