package com.cmit.mvne.billing.settle.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/3/26
 */
@Data
public class SiminnSumMonthFeeDTO {

    @ExcelProperty(value = "Invoicing Period")
    private String invoicingPeriod;
    @ExcelProperty(value = "Domestic Data")
    @NumberFormat(value = "#.##")
    private BigDecimal domesticDataChargeFee = new BigDecimal("0");
    @ExcelProperty(value = "Roaming Data")
    @NumberFormat(value = "#.##")
    private BigDecimal roamingDataChargeFee = new BigDecimal("0");
    @ExcelProperty(value = "Domestic Voice")
    @NumberFormat(value = "#.##")
    private BigDecimal domesticVoiceChargeFee = new BigDecimal("0");
    @ExcelProperty(value = "Roaming Voice")
    @NumberFormat(value = "#.##")
    private BigDecimal roamingVoiceChargeFee =  new BigDecimal("0");
    @ExcelProperty(value = "Domestic SMS")
    @NumberFormat(value = "#.##")
    private BigDecimal domesticSmsChargeFee =  new BigDecimal("0");
    @ExcelProperty(value = "Roaming SMS")
    @NumberFormat(value = "#.##")
    private BigDecimal roamingSmsChargeFee = new BigDecimal("0");
    @ExcelProperty(value = "Account Opening Activition")
    @NumberFormat(value = "#.##")
    private BigDecimal accountOpeningActivationChargeFee = new BigDecimal("0");
    @ExcelProperty(value = "Sum Charge Fee")
    @NumberFormat(value = "#.##")
    private BigDecimal sumChargeFee =  new BigDecimal("0");


}
