package com.cmit.mvne.billing.settle.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.annotations.Update;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

public class SettleSiminnSumDay {
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    private String invoicingPeriod;

    private String billDay;

    private BigDecimal chargeFee;

    private String chargeMeasure;

    private BigDecimal totalFee;

    private BigDecimal totalValue;

    private String itemName;

    private String itemMeasure;

    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(update = "now()")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public SettleSiminnSumDay(Long id, String invoicingPeriod, String billDay, BigDecimal chargeFee, String chargeMeasure, BigDecimal totalFee, BigDecimal totalValue, String itemName, String itemMeasure, Date createTime, Date updateTime) {
        this.id = id;
        this.invoicingPeriod = invoicingPeriod;
        this.billDay = billDay;
        this.chargeFee = chargeFee;
        this.chargeMeasure = chargeMeasure;
        this.totalFee = totalFee;
        this.totalValue = totalValue;
        this.itemName = itemName;
        this.itemMeasure = itemMeasure;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public SettleSiminnSumDay() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoicingPeriod() {
        return invoicingPeriod;
    }

    public void setInvoicingPeriod(String invoicingPeriod) {
        this.invoicingPeriod = invoicingPeriod == null ? null : invoicingPeriod.trim();
    }

    public String getBillDay() {
        return billDay;
    }

    public void setBillDay(String billDay) {
        this.billDay = billDay == null ? null : billDay.trim();
    }

    public BigDecimal getChargeFee() {
        return chargeFee;
    }

    public void setChargeFee(BigDecimal chargeFee) {
        this.chargeFee = chargeFee;
    }

    public String getChargeMeasure() {
        return chargeMeasure;
    }

    public void setChargeMeasure(String chargeMeasure) {
        this.chargeMeasure = chargeMeasure == null ? null : chargeMeasure.trim();
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName == null ? null : itemName.trim();
    }

    public String getItemMeasure() {
        return itemMeasure;
    }

    public void setItemMeasure(String itemMeasure) {
        this.itemMeasure = itemMeasure == null ? null : itemMeasure.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}