package com.cmit.mvne.billing.settle.service.impl;

import com.cmit.mvne.billing.settle.common.MvneCrmResponse;
import com.cmit.mvne.billing.settle.common.SettleItem;
import com.cmit.mvne.billing.settle.dao.SettleSiminnRuleMapper;
import com.cmit.mvne.billing.settle.dao.SettleSiminnSumDayMapper;
import com.cmit.mvne.billing.settle.dao.SettleSiminnSumMonthMapper;
import com.cmit.mvne.billing.settle.entity.SettleSiminnRule;
import com.cmit.mvne.billing.settle.entity.SettleSiminnSumDay;
import com.cmit.mvne.billing.settle.entity.SettleSiminnSumMonth;
import com.cmit.mvne.billing.settle.entity.dto.SettleItemGprsVolDTO;
import com.cmit.mvne.billing.settle.entity.dto.SiminnSumAmountDTO;
import com.cmit.mvne.billing.settle.entity.dto.SiminnSumMonthFeeDTO;
import com.cmit.mvne.billing.settle.remote.MvneCrmClient;
import com.cmit.mvne.billing.settle.service.*;
import com.cmit.mvne.billing.settle.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 结算数据服务类
 *
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/3/24
 */
@Slf4j
@Service
public class SettleSiminnSumServiceImpl implements SettleSiminnSumService {

    @Autowired
    private SettleSiminnSumDayMapper settleSiminnSumDayMapper;
    @Autowired
    private SettleSiminnSumMonthMapper settleSiminnSumMonthMapper;
    @Autowired
    private SettleSiminnRuleMapper settleSiminnRuleMapper;

    @Autowired
    private SettleCdrGprsService settleCdrGprsService;
    @Autowired
    private SettleSiminnRuleService settleSiminnRuleService;
    @Autowired
    private SettleSiminnSumDayService settleSiminnSumDayService;
    @Autowired
    private SettleSiminnSumMonthService settleSiminnSumMonthService;
    @Autowired
    private MvneCrmClient mvneCrmClient;

    /**
     * 根据账期按月查询结算汇总数据
     * @param startMonth 开始月份
     * @param endMonth 结束月份
     * @return 结算汇总数据
     */
    @Override
    public List<SiminnSumMonthFeeDTO> getSettleSiminnSumFee(String startMonth, String endMonth) {

        List<SiminnSumMonthFeeDTO> siminnSumMonthFeeDTOList = new ArrayList<>();

        List<String> monthList = getMonthList(startMonth, endMonth);

        for(String month : monthList) {
            List<SettleSiminnSumMonth> siminnSumMonthList = settleSiminnSumMonthMapper.selectByInvoicingPeriod(month);

            SiminnSumMonthFeeDTO siminnSumMonthFeeDTO = new SiminnSumMonthFeeDTO();
            siminnSumMonthFeeDTO.setInvoicingPeriod(month);
            BigDecimal sumChargeFee = siminnSumMonthFeeDTO.getSumChargeFee();
            for(SettleSiminnSumMonth siminnSumMonth: siminnSumMonthList) {
                if(siminnSumMonth.getItemName().equals(SettleItem.DOMESTIC_DATA.name())) {
                    siminnSumMonthFeeDTO.setDomesticDataChargeFee(siminnSumMonth.getTotalFee());
                    sumChargeFee = sumChargeFee.add(siminnSumMonth.getTotalFee());
                }
                if(siminnSumMonth.getItemName().equals(SettleItem.INTERNATIONAL_ROAMING_DATA.name())) {
                    siminnSumMonthFeeDTO.setRoamingDataChargeFee(siminnSumMonth.getTotalFee());
                    sumChargeFee = sumChargeFee.add(siminnSumMonth.getTotalFee());
                }
                if(siminnSumMonth.getItemName().equals(SettleItem.DOMESTIC_VOICE.name())) {
                    siminnSumMonthFeeDTO.setDomesticVoiceChargeFee(siminnSumMonth.getTotalFee());
                    sumChargeFee = sumChargeFee.add(siminnSumMonth.getTotalFee());
                }
                if(siminnSumMonth.getItemName().equals(SettleItem.INTERNATIONAL_ROAMING_VOICE.name())) {
                    siminnSumMonthFeeDTO.setRoamingVoiceChargeFee(siminnSumMonth.getTotalFee());
                    sumChargeFee = sumChargeFee.add(siminnSumMonth.getTotalFee());
                }
                if(siminnSumMonth.getItemName().equals(SettleItem.DOMESTIC_SMS.name())) {
                    siminnSumMonthFeeDTO.setDomesticSmsChargeFee(siminnSumMonth.getTotalFee());
                    sumChargeFee = sumChargeFee.add(siminnSumMonth.getTotalFee());
                }
                if(siminnSumMonth.getItemName().equals(SettleItem.INTERNATIONAL_ROAMING_SMS.name())) {
                    siminnSumMonthFeeDTO.setRoamingSmsChargeFee(siminnSumMonth.getTotalFee());
                    sumChargeFee = sumChargeFee.add(siminnSumMonth.getTotalFee());
                }
                if(siminnSumMonth.getItemName().equals(SettleItem.OPEN_ACCOUNT_ACTIVATION.name())) {
                    siminnSumMonthFeeDTO.setAccountOpeningActivationChargeFee(siminnSumMonth.getTotalFee());
                    sumChargeFee = sumChargeFee.add(siminnSumMonth.getTotalFee());
                }
                siminnSumMonthFeeDTO.setSumChargeFee(sumChargeFee);
            }

            siminnSumMonthFeeDTOList.add(siminnSumMonthFeeDTO);
        }
        return siminnSumMonthFeeDTOList;
    }

    private List<String> getMonthList(String startMonth, String endMonth) {

        DateTimeFormatter yearMonthFormatter = DateTimeFormatter.ofPattern("yyyyMM");

        List<String> yearMonthList = new ArrayList<>();
        YearMonth startYearMonth = YearMonth.parse(startMonth, yearMonthFormatter);
        YearMonth endYearMonth = YearMonth.parse(endMonth, yearMonthFormatter);
        while(startYearMonth.isBefore(endYearMonth)) {
            yearMonthList.add(startYearMonth.format(yearMonthFormatter));
            startYearMonth = startYearMonth.plusMonths(1);
        }
        if(startYearMonth.equals(endYearMonth)) {
            yearMonthList.add(startYearMonth.format(yearMonthFormatter));
        }
        return yearMonthList;
    }


    @Override
    public List<SiminnSumAmountDTO> getSettleSiminnSumMonth(String startMonth, String endMonth) {

        List<SiminnSumAmountDTO> siminnSumAmountDTOList = new ArrayList<>();

        List<String> monthList = getMonthList(startMonth, endMonth);
        for(String month : monthList) {
            List<SettleSiminnSumMonth> siminnSumMonthList = settleSiminnSumMonthMapper.selectByInvoicingPeriod(month);

            SiminnSumAmountDTO siminnSumAmountDTO = new SiminnSumAmountDTO();
            siminnSumAmountDTO.setInvoicingPeriod(month);
            for(SettleSiminnSumMonth siminnSumMonth: siminnSumMonthList) {
                SettleSiminnRule siminnRule = settleSiminnRuleMapper.selectNotExpireByItemName(siminnSumMonth.getItemName()).get(0);
                // 如果是本地Gprs，填入汇总
                if(siminnRule.getItemName().equals(SettleItem.DOMESTIC_DATA.name())) {
                    siminnSumAmountDTO.setDomesticDataAmount(siminnSumMonth.getTotalValue().divide(new BigDecimal("1024"), 2, RoundingMode.HALF_UP));
                }
                if(siminnRule.getItemName().equals(SettleItem.INTERNATIONAL_ROAMING_DATA.name())) {
                    siminnSumAmountDTO.setRoamingDataAmount(siminnSumMonth.getTotalValue().divide(new BigDecimal("1024"), 2, RoundingMode.HALF_UP));
                }
                if(siminnRule.getItemName().equals(SettleItem.DOMESTIC_VOICE.name())) {
                    siminnSumAmountDTO.setDomesticVoiceAmount(siminnSumMonth.getTotalValue());
                }
                if(siminnRule.getItemName().equals(SettleItem.INTERNATIONAL_ROAMING_VOICE.name())) {
                    siminnSumAmountDTO.setRoamingVoiceAmount(siminnSumMonth.getTotalValue());
                }
                if(siminnRule.getItemName().equals(SettleItem.DOMESTIC_SMS.name())) {
                    siminnSumAmountDTO.setDomesticSmsAmount(siminnSumMonth.getTotalValue());
                }
                if(siminnRule.getItemName().equals(SettleItem.INTERNATIONAL_ROAMING_SMS.name())) {
                    siminnSumAmountDTO.setRoamingSmsAmount(siminnSumMonth.getTotalValue());
                }
                if(siminnRule.getItemName().equals(SettleItem.OPEN_ACCOUNT_ACTIVATION.name())) {
                    siminnSumAmountDTO.setAccountOpeningActivationAmount(siminnSumMonth.getTotalValue());
                }
            }

            siminnSumAmountDTOList.add(siminnSumAmountDTO);
        }
        return siminnSumAmountDTOList;
    }


    @Override
    public List<SiminnSumAmountDTO> getSettleSiminnSumDay(Long startTime, Long endTime) {
        List<SiminnSumAmountDTO> siminnSumAmountDTOList = new ArrayList<>();

        List<String> dayList = getDayList(startTime, endTime);
        for(String billDay : dayList) {
            List<SettleSiminnSumDay> settleSiminnSumDayList = settleSiminnSumDayMapper.selectByBillDay(billDay);

            SiminnSumAmountDTO settleSumAmountDTO = new SiminnSumAmountDTO();
            settleSumAmountDTO.setInvoicingPeriod(billDay);
            for(SettleSiminnSumDay settleSiminnSumDay : settleSiminnSumDayList) {
                if(settleSiminnSumDay.getItemName().equals(SettleItem.DOMESTIC_DATA.name())) {
                    settleSumAmountDTO.setDomesticDataAmount(settleSiminnSumDay.getTotalValue());
                }
                if(settleSiminnSumDay.getItemName().equals(SettleItem.INTERNATIONAL_ROAMING_DATA.name())) {
                    settleSumAmountDTO.setRoamingDataAmount(settleSiminnSumDay.getTotalValue());
                }
                if(settleSiminnSumDay.getItemName().equals(SettleItem.DOMESTIC_VOICE.name())) {
                    settleSumAmountDTO.setDomesticVoiceAmount(settleSiminnSumDay.getTotalValue());
                }
                if(settleSiminnSumDay.getItemName().equals(SettleItem.INTERNATIONAL_ROAMING_VOICE.name())) {
                    settleSumAmountDTO.setRoamingVoiceAmount(settleSiminnSumDay.getTotalValue());
                }
                if(settleSiminnSumDay.getItemName().equals(SettleItem.DOMESTIC_SMS.name())) {
                    settleSumAmountDTO.setDomesticSmsAmount(settleSiminnSumDay.getTotalValue());
                }
                if(settleSiminnSumDay.getItemName().equals(SettleItem.OPEN_ACCOUNT_ACTIVATION.name())) {
                    settleSumAmountDTO.setAccountOpeningActivationAmount(settleSiminnSumDay.getTotalValue());
                }
            }
            siminnSumAmountDTOList.add(settleSumAmountDTO);
        }
        return siminnSumAmountDTOList;
    }

    @Override
    public List<SettleSiminnSumDay> sumByDay(String invoicingPeriod, String yyyyMMdd) {
        List<SettleSiminnSumDay> settleSiminnSumDayList = new ArrayList<>();
        // 汇总激活号码数
        sumActivationNumberByDay(invoicingPeriod, yyyyMMdd, settleSiminnSumDayList);
        // 汇总流量相关科目
        List<SettleItemGprsVolDTO> itemGprsVolDTOList = settleCdrGprsService.findSettleItmeGprsVolByDay(yyyyMMdd);
        for(SettleItemGprsVolDTO itemGprsVolDTO : itemGprsVolDTOList) {

            SettleSiminnSumDay settleSiminnSumDay = new SettleSiminnSumDay();
            List<SettleSiminnRule> settleSiminnRuleList = settleSiminnRuleService.selectNotExpireByItemName(itemGprsVolDTO.getSettleItem());

            if(settleSiminnRuleList.size()>1) {
                log.error("Error,more than one siminn rules exists!");
            }
            if (settleSiminnRuleList.size() == 0) {
                log.error("There are no effective rules under item: {}! ", itemGprsVolDTO.getSettleItem());
                throw new RuntimeException("settle_siminn_rule do not have any effective rules");
            }
            SettleSiminnRule settleSiminnRule = settleSiminnRuleList.get(0);

            settleSiminnSumDay.setInvoicingPeriod(invoicingPeriod);
            settleSiminnSumDay.setBillDay(yyyyMMdd);
            settleSiminnSumDay.setChargeFee(settleSiminnRule.getChargeFee());
            settleSiminnSumDay.setChargeMeasure(settleSiminnRule.getChargeMeasure());
            settleSiminnSumDay.setItemName(settleSiminnRule.getItemName());
            settleSiminnSumDay.setItemMeasure(settleSiminnRule.getItemMeasure());

            BigDecimal totalDownloadVolValue = new BigDecimal(String.valueOf(itemGprsVolDTO.getDownloadVolSum()));
            BigDecimal totalUploadVolValue = new BigDecimal(String.valueOf(itemGprsVolDTO.getUploadVolSum()));
            // byte转换为kb时会向上取整 div 1024
            BigDecimal totalValue = totalDownloadVolValue.add(totalUploadVolValue).divide(new BigDecimal("1024"), 0, RoundingMode.CEILING);
            settleSiminnSumDay.setTotalValue(totalValue);

            // 总价=totalValue*chargeFee / 100
            BigDecimal totalFee = totalValue.multiply(settleSiminnRule.getChargeFee()).divide(new BigDecimal("100"), 2, RoundingMode.CEILING);
            settleSiminnSumDay.setTotalFee(totalFee);
            settleSiminnSumDayList.add(settleSiminnSumDay);
        }

        // 入库
        settleSiminnSumDayService.saveBatch(settleSiminnSumDayList);
        return settleSiminnSumDayList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<SettleSiminnSumMonth> sumByMonth(String invoicingPeriod) {
        List<SettleSiminnSumMonth> settleSiminnSumMonthList = new ArrayList<>();

        // 汇总激活号码数
        sumActivationNumberByMonth(invoicingPeriod, settleSiminnSumMonthList);

        // 汇总本地流量话单和漫游流量话单
        sumGprsByMonth(invoicingPeriod, settleSiminnSumMonthList);

        // 入库
        settleSiminnSumMonthService.saveBatch(settleSiminnSumMonthList);

        return settleSiminnSumMonthList;

    }



    private List<String> getDayList(Long startTime, Long endTime) {

        DateTimeFormatter dayFormater = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime localStartTime = DateTimeUtil.getDateTimeofTimestamp(startTime);
        LocalDateTime localEndTime = DateTimeUtil.getDateTimeofTimestamp(endTime);

        List<String> dayList = new ArrayList<>();
        while(localStartTime.isBefore(localEndTime)) {
            dayList.add(localStartTime.format(dayFormater));
            localStartTime = localStartTime.plusDays(1);
        }
        if(localStartTime.equals(localEndTime)) {
            dayList.add(localStartTime.format(dayFormater));
        }
        return dayList;
    }

    private void sumActivationNumberByDay(String invoicingPeriod, String yyyyMMdd, List<SettleSiminnSumDay> settleSiminnSumDayList) {

        SettleSiminnRule rule = getSettleSiminnRule(SettleItem.OPEN_ACCOUNT_ACTIVATION.name());

        SettleSiminnSumDay settleSiminnSumDay = new SettleSiminnSumDay();
        settleSiminnSumDay.setInvoicingPeriod(invoicingPeriod);
        settleSiminnSumDay.setBillDay(yyyyMMdd);
        settleSiminnSumDay.setChargeFee(rule.getChargeFee());
        settleSiminnSumDay.setChargeMeasure(rule.getChargeMeasure());
        settleSiminnSumDay.setItemName(rule.getItemName());
        settleSiminnSumDay.setItemMeasure(rule.getItemMeasure());

        MvneCrmResponse crmResponse = mvneCrmClient.queryCreateUserOrdersByDay(yyyyMMdd);
        if (crmResponse.get("code").equals(200)) {
            settleSiminnSumDay.setTotalValue(new BigDecimal(crmResponse.get("data").toString()));
        } else {
            throw new RuntimeException("data: " + crmResponse.get("data").toString());
        }

        BigDecimal totalFee = settleSiminnSumDay.getTotalValue().multiply(settleSiminnSumDay.getChargeFee()).divide(new BigDecimal("100"), 2, RoundingMode.CEILING);
        settleSiminnSumDay.setTotalFee(totalFee);

        settleSiminnSumDayList.add(settleSiminnSumDay);
    }

    private SettleSiminnRule getSettleSiminnRule(String itemName) {
        List<SettleSiminnRule> settleSiminnRuleList = settleSiminnRuleService.selectNotExpireByItemName(itemName);
        if(settleSiminnRuleList.size()>1) {
            log.error("error,Two effective siminn rule exists!");
        }
        if (settleSiminnRuleList.size() == 0) {
            log.error("settle_siminn_rule do not have any rules for {} ! ", itemName);
            throw new RuntimeException("settle_siminn_rule do not have any rules");
        }
        return settleSiminnRuleList.get(0);
    }

    /**
     * 开户号码按月汇总
     * @param invoicingPeriod
     * @param settleSiminnSumMonthList
     */
    private void sumActivationNumberByMonth(String invoicingPeriod, List<SettleSiminnSumMonth> settleSiminnSumMonthList) {

        String itemName = SettleItem.OPEN_ACCOUNT_ACTIVATION.name();
        SettleSiminnRule rule = getSettleSiminnRule(itemName);

        SettleSiminnSumMonth settleSiminnSumMonth = new SettleSiminnSumMonth();
        settleSiminnSumMonth.setInvoicingPeriod(invoicingPeriod);

        settleSiminnSumMonth.setItemName(rule.getItemName());
        settleSiminnSumMonth.setItemMeasure(rule.getItemMeasure());
        settleSiminnSumMonth.setChargeFee(rule.getChargeFee());
        settleSiminnSumMonth.setChargeMeasure(rule.getChargeMeasure());

        MvneCrmResponse crmResponse = mvneCrmClient.queryCreateUserOrdersByMonth(invoicingPeriod);
        if (crmResponse.get("code").equals(200)) {
            settleSiminnSumMonth.setTotalValue(new BigDecimal(crmResponse.get("data").toString()));
        } else {
            throw new RuntimeException("Data: " + crmResponse.get("data").toString());
        }
        BigDecimal totalFee = settleSiminnSumMonth.getTotalValue().multiply(settleSiminnSumMonth.getChargeFee()).divide(new BigDecimal("100"), 2, RoundingMode.CEILING);
        settleSiminnSumMonth.setTotalFee(totalFee);
        settleSiminnSumMonthList.add(settleSiminnSumMonth);

    }

    private void sumGprsByMonth(String invoicingPeriod, List<SettleSiminnSumMonth> settleSiminnSumMonthList) {

        List<String> daysOfMonth = getDaysByInvoicingPeriod(invoicingPeriod);
        Map<String, SettleSiminnSumMonth> sumMonthMap = new HashMap<>();

        for(String yyyyMMdd : daysOfMonth) {

            // 处理详单表中各类科目汇总
            List<SettleItemGprsVolDTO> itemGprsVolDTOList = settleCdrGprsService.findSettleItmeGprsVolByDay(yyyyMMdd);
            for(SettleItemGprsVolDTO itemGprsVolDTO : itemGprsVolDTOList) {

                SettleSiminnRule rule = getSettleSiminnRule(itemGprsVolDTO.getSettleItem());

                BigDecimal totalDownloadVolValue = new BigDecimal(String.valueOf(itemGprsVolDTO.getDownloadVolSum()));
                BigDecimal totalUploadVolValue = new BigDecimal(String.valueOf(itemGprsVolDTO.getUploadVolSum()));
                BigDecimal totalValue = totalDownloadVolValue.add(totalUploadVolValue);

                // 处理月汇总
                if (sumMonthMap.get(rule.getItemName()) == null) {
                    SettleSiminnSumMonth siminnSumMonth = new SettleSiminnSumMonth();
                    siminnSumMonth.setInvoicingPeriod(invoicingPeriod);
                    siminnSumMonth.setItemName(rule.getItemName());
                    siminnSumMonth.setItemMeasure(rule.getItemMeasure());
                    siminnSumMonth.setChargeFee(rule.getChargeFee());
                    siminnSumMonth.setChargeMeasure(rule.getChargeMeasure());
                    siminnSumMonth.setTotalValue(totalValue);
                    sumMonthMap.put(itemGprsVolDTO.getSettleItem(), siminnSumMonth);
                } else {
                    SettleSiminnSumMonth siminnSumMonth = sumMonthMap.get(itemGprsVolDTO.getSettleItem());
                    // 累加每天的总量
                    BigDecimal addedTotalValue = siminnSumMonth.getTotalValue().add(totalValue);
                    siminnSumMonth.setTotalValue(addedTotalValue);
                    sumMonthMap.put(itemGprsVolDTO.getSettleItem(), siminnSumMonth);
                }
            }
        }

        // 将Map转换为List，对总量进行单位转换，Byte 转换为Kb
        for(Map.Entry<String, SettleSiminnSumMonth> entry : sumMonthMap.entrySet()) {
            SettleSiminnSumMonth settleSiminnSumMonth = entry.getValue();

            BigDecimal totalValue = settleSiminnSumMonth.getTotalValue();
            // Byte转换为kb
            totalValue = totalValue.divide(new BigDecimal("1024"), 2, RoundingMode.CEILING);
            // 月总价=totalValue*chargeFee/100
            BigDecimal totalFee = totalValue.multiply(settleSiminnSumMonth.getChargeFee()).divide(new BigDecimal("100"), 2, RoundingMode.CEILING);
            settleSiminnSumMonth.setTotalValue(totalValue);
            settleSiminnSumMonth.setTotalFee(totalFee);

            settleSiminnSumMonthList.add(settleSiminnSumMonth);
        }
    }

    /**
     * 根据结算周期获取该周期中的每一天
     * @param invoicingPeriod
     * @return
     */
    private List<String> getDaysByInvoicingPeriod(String invoicingPeriod) {
        List<String> daysOfMonth = new ArrayList<>();
        LocalDate localDateStart = LocalDate.parse(invoicingPeriod + "01", DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate localDateEnd = localDateStart.plusMonths(1);
        while (localDateStart.isBefore(localDateEnd)) {
            daysOfMonth.add(localDateStart.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            localDateStart = localDateStart.plusDays(1);
        }
        return daysOfMonth;
    }

    public static void main(String[] args) {

//        parseToMonthList("201910", "202003");

//        List<String> dayList = getDayList(DateTimeUtil.strToDate("20200420000000"), new Date());
//        for(String day : dayList) {
//            System.out.println(day);
//        }

//        List<String> daysByInvoicingPeriod = getDaysByInvoicingPeriod("202005");
//        for(String day : daysByInvoicingPeriod) {
//            System.out.println(day);
//        }


    }
}
