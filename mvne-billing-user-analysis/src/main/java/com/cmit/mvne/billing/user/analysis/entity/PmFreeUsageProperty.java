package com.cmit.mvne.billing.user.analysis.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * <p>
 * Usage attribute of free resource.
This table defines h
 * </p>
 *
 * @author Luxf
 * @since 2020-01-20
 */
@Data
@TableName("pm_free_usage_property")
public class PmFreeUsageProperty  {

    @TableId(value = "ASSET_ITEM_ID", type = IdType.INPUT)
    private Long assetItemId;

    @TableField("MEASURE_ID")
    private BigDecimal measureId;

    @TableField("CARRY_FORWARD_RULE_ID")
    private Long carryForwardRuleId;

    @TableField("CONSUME_FLAG")
    private BigDecimal consumeFlag;

    @TableField("BILLING_TYPE")
    private BigDecimal billingType;

    @TableField("BONUS_UNIT_CYCLE")
    private BigDecimal bonusUnitCycle;

    @TableField("TENANT_ID")
    private BigDecimal tenantId;

    public PmFreeUsageProperty(Long assetItemId, BigDecimal measureId, Long carryForwardRuleId, BigDecimal consumeFlag, BigDecimal billingType, BigDecimal bonusUnitCycle, BigDecimal tenantId) {
        this.assetItemId = assetItemId;
        this.measureId = measureId;
        this.carryForwardRuleId = carryForwardRuleId;
        this.consumeFlag = consumeFlag;
        this.billingType = billingType;
        this.bonusUnitCycle = bonusUnitCycle;
        this.tenantId = tenantId;
    }
}
