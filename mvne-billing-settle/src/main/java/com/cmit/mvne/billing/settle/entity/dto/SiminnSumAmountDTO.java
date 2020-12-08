package com.cmit.mvne.billing.settle.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/3/26
 */
@Data
public class SiminnSumAmountDTO {

    @ExcelProperty(value = "Invoicing Period")
    private String invoicingPeriod;
    @ExcelProperty(value = "Domestic Data Amount")
    private BigDecimal domesticDataAmount = new BigDecimal("0");
    @ExcelProperty(value = "Roaming Data Amount")
    private BigDecimal roamingDataAmount = new BigDecimal("0");
    @ExcelProperty(value = "Domestic Voice Amount")
    private BigDecimal domesticVoiceAmount = new BigDecimal("0");
    @ExcelProperty(value = "Roaming Voice Amount")
    private BigDecimal roamingVoiceAmount =  new BigDecimal("0");
    @ExcelProperty(value = "Domestic SMS Amount")
    private BigDecimal domesticSmsAmount =  new BigDecimal("0");
    @ExcelProperty(value = "Roaming SMS Amount")
    private BigDecimal roamingSmsAmount = new BigDecimal("0");
    @ExcelProperty(value = "Account Opening Activation Amount")
    private BigDecimal accountOpeningActivationAmount = new BigDecimal("0");


}
