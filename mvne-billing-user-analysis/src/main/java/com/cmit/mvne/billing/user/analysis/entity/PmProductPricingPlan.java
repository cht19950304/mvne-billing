package com.cmit.mvne.billing.user.analysis.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;


/**
 * <p>
 * Relationship table of product offering pricing plan.
T
 * </p>
 *
 * @author Luxf
 * @since 2020-01-20
 */
@Data
@TableName("pm_product_pricing_plan")
public class PmProductPricingPlan  {

    @TableId(value = "PRODUCT_OFFERING_ID", type = IdType.INPUT)
    private Long productOfferingId;

    @TableField("POLICY_ID")
    private Long policyId;

    @TableField("PRIORITY")
    private BigDecimal priority;

    @TableField("MAIN_PROMOTION")
    private BigDecimal mainPromotion;

    @TableField("DISP_FLAG")
    private BigDecimal dispFlag;

    @TableField("TENANT_ID")
    private BigDecimal tenantId;

    @TableField("PRICING_PLAN_ID")
    private Long pricingPlanId;


    public PmProductPricingPlan(Long productOfferingId, Long policyId, BigDecimal priority, BigDecimal mainPromotion, BigDecimal dispFlag, BigDecimal tenantId, Long pricingPlanId) {
        this.productOfferingId = productOfferingId;
        this.policyId = policyId;
        this.priority = priority;
        this.mainPromotion = mainPromotion;
        this.dispFlag = dispFlag;
        this.tenantId = tenantId;
        this.pricingPlanId = pricingPlanId;
    }
}
