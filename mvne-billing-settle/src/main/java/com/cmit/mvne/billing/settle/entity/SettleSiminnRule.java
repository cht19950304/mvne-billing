package com.cmit.mvne.billing.settle.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

public class SettleSiminnRule {
    @TableId(value = "ITEM_CODE", type = IdType.AUTO)
    private Long itemCode;

    private BigDecimal chargeFee;

    private String chargeMeasure;

    private String itemName;

    private String itemMeasure;

    private BigDecimal taxRate;

    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectiveTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

    public SettleSiminnRule(Long itemCode, BigDecimal chargeFee, String chargeMeasure, String itemName, String itemMeasure, BigDecimal taxRate, Date createTime, Date effectiveTime, Date expireTime) {
        this.itemCode = itemCode;
        this.chargeFee = chargeFee;
        this.chargeMeasure = chargeMeasure;
        this.itemName = itemName;
        this.itemMeasure = itemMeasure;
        this.taxRate = taxRate;
        this.createTime = createTime;
        this.effectiveTime = effectiveTime;
        this.expireTime = expireTime;
    }

    public SettleSiminnRule() {
        super();
    }

    public Long getItemCode() {
        return itemCode;
    }

    public void setItemCode(Long itemCode) {
        this.itemCode = itemCode;
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

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}