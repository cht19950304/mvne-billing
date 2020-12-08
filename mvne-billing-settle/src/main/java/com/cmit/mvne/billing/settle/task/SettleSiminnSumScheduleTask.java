package com.cmit.mvne.billing.settle.task;

import com.cmit.mvne.billing.settle.common.MvneCrmResponse;
import com.cmit.mvne.billing.settle.common.SettleItem;
import com.cmit.mvne.billing.settle.entity.SettleSiminnRule;
import com.cmit.mvne.billing.settle.entity.SettleSiminnSumDay;
import com.cmit.mvne.billing.settle.entity.SettleSiminnSumMonth;
import com.cmit.mvne.billing.settle.entity.dto.SettleItemGprsVolDTO;
import com.cmit.mvne.billing.settle.remote.MvneCrmClient;
import com.cmit.mvne.billing.settle.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 结算汇总定时任务
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/3/21
 */
@Slf4j
@Component
public class SettleSiminnSumScheduleTask {

    @Autowired
    private SettleSiminnSumService settleSiminnSumService;

    /**
     * 按天汇总
     */
    @Scheduled(cron = "${settle.sumByDay}")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void sumByDay() {
        
        log.info("Starting sum by day!");
        
        // todo 上下发平衡检查
        // todo 错单文件检查

        String invoicingPeriod = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMM"));
        String yyyyMMdd = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        settleSiminnSumService.sumByDay(invoicingPeriod, yyyyMMdd);
    }

    /**
     * 按月汇总,自动汇总
     * 先按日汇总
     * 然后按月汇总
     * 生成日汇总表、月汇总表
     */
    @Scheduled(cron = "${settle.sumByMonth}")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void sumByMonth() {

        log.info("Starting sum gprs by month!");

        // todo 上下发平衡检查
        // todo 错单文件检查

        String invoicingPeriod = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM"));

        settleSiminnSumService.sumByMonth(invoicingPeriod);
    }

}
