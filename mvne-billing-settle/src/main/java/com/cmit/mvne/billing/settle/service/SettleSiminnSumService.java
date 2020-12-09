package com.cmit.mvne.billing.settle.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cmit.mvne.billing.settle.entity.SettleSiminnSumDay;
import com.cmit.mvne.billing.settle.entity.SettleSiminnSumMonth;
import com.cmit.mvne.billing.settle.entity.dto.SiminnSumAmountDTO;
import com.cmit.mvne.billing.settle.entity.dto.SiminnSumMonthFeeDTO;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/7
 */
public interface SettleSiminnSumService {
    List<SiminnSumMonthFeeDTO> getSettleSiminnSumFee(String startMonth, String endMonth);

    List<SiminnSumAmountDTO> getSettleSiminnSumMonth(String startMonth, String endMonth);

    List<SiminnSumAmountDTO> getSettleSiminnSumDay(Long startTime, Long endTime);

    List<SettleSiminnSumDay> sumByDay(String invoicingPeriod, String yyyyMMdd);

    List<SettleSiminnSumMonth> sumByMonth(String invoicingPeriod);
}
