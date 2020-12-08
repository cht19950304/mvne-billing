package com.cmit.mvne.billing.user.analysis.entity;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;

/**
 * <p>
 * Composite product offering pricing & product offering pricin
 * </p>
 *
 * @author Luxf
 * @since 2020-01-20
 */
@Data
@TableName("pm_composite_offer_price")
public class PmCompositeOfferPrice  {

    @TableId(value = "PRICING_PLAN_ID", type = IdType.INPUT)
    private Long pricingPlanId;

    @TableField("PRICE_ID")
    private Long priceId;

    @TableField("BILLING_TYPE")
    private BigDecimal billingType;

    @TableField("OFFER_STS")
    private BigDecimal offerSts;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("VALID_DATE")
    private Date validDate;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("EXPIRE_DATE")
    private Date expireDate;

    @TableField("TENANT_ID")
    private BigDecimal tenantId;

    public PmCompositeOfferPrice(Long pricingPlanId, Long priceId, BigDecimal billingType, BigDecimal offerSts, Date validDate, Date expireDate, BigDecimal tenantId) {
        this.pricingPlanId = pricingPlanId;
        this.priceId = priceId;
        this.billingType = billingType;
        this.offerSts = offerSts;
        this.validDate = validDate;
        this.expireDate = expireDate;
        this.tenantId = tenantId;
    }
}
