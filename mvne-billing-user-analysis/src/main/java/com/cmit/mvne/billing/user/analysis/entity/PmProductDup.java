package com.cmit.mvne.billing.user.analysis.entity;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author Luxf
 * @since 2020-07-28
 */
@Data
@TableName("pm_product_dup")
public class PmProductDup  {
    /**
     * 产品id
     */
    @TableField("PRODUCT_ID")
    private Long productId;

    /**
     * 产品名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 产品资费，单位：欧元
     */
    @TableField("FEE")
    private BigDecimal fee;

    /**
     * 免费资源总量
     */
    @TableField("AMOUNT")
    private BigDecimal amount;

    /**
     * 免费资源单位
     */
    @TableField("MEASURE_ID")
    private BigDecimal measureId;

    /**
     * 产品资费，单位：欧元
     */
    @TableField("RATING_FEE")
    private BigDecimal ratingFee;

    /**
     * 免费资源单位
     */
    @TableField("RATING_MEASURE_ID")
    private BigDecimal ratingMeasureId;

    /**
     * 套外批价单位量
     */
    @TableField("RATING_AMOUNT")
    private BigDecimal ratingAmount;

    /**
     * 免费资源单位
     */
    @TableField("RATING_AMOUNT_MEASURE_ID")
    private BigDecimal ratingAmountMeasureId;

    /**
     * 归属的漫游区域
     */
    @TableField("COUNTRY_GROUP")
    private String countryGroup;

    /**
     * group id
     */
    @TableField("GROUP_ID")
    private Integer groupId;

    /**
     * 产品的有效期（单位：月）
     */
    @TableField("EFFECTIVE_TIME")
    private Integer effectiveTime;

    /**
     * 产品生效时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("EFFECTIVE_DATE")
    private Date effectiveDate;

    /**
     * 产品失效时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("EXPIRE_DATE")
    private Date expireDate;

    public PmProductDup(String name, BigDecimal fee, BigDecimal amount, BigDecimal measureId, BigDecimal ratingFee, BigDecimal ratingMeasureId, BigDecimal ratingAmount, BigDecimal ratingAmountMeasureId, String countryGroup, Integer groupId, Integer effectiveTime, Date effectiveDate, Date expireDate) {
        //this.productId = productId;
        this.name = name;
        this.fee = fee;
        this.amount = amount;
        this.measureId = measureId;
        this.ratingFee = ratingFee;
        this.ratingMeasureId = ratingMeasureId;
        this.ratingAmount = ratingAmount;
        this.ratingAmountMeasureId = ratingAmountMeasureId;
        this.countryGroup = countryGroup;
        this.groupId = groupId;
        this.effectiveTime = effectiveTime;
        this.effectiveDate = effectiveDate;
        this.expireDate = expireDate;
    }
}
