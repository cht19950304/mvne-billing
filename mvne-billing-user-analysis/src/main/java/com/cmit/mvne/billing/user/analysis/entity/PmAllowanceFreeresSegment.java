package com.cmit.mvne.billing.user.analysis.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * <p>
 * Segment definition of free resource reward.
 * </p>
 *
 * @author Luxf
 * @since 2020-01-20
 */
@Data
@TableName("pm_allowance_freeres_segment")
public class PmAllowanceFreeresSegment  {

    @TableId(value = "PRICE_ID", type = IdType.INPUT)
    private Long priceId;

    @TableField("START_VALUE")
    private BigDecimal startValue;

    @TableField("END_VALUE")
    private BigDecimal endValue;

    @TableField("AMOUNT")
    private BigDecimal amount;

    @TableField("FORMULA_ID")
    private BigDecimal formulaId;

    @TableField("LIMIT_AMOUNT")
    private BigDecimal limitAmount;

    @TableField("TENANT_ID")
    private BigDecimal tenantId;

    public PmAllowanceFreeresSegment(Long priceId, BigDecimal startValue, BigDecimal endValue, BigDecimal amount, BigDecimal formulaId, BigDecimal limitAmount, BigDecimal tenantId) {
        this.priceId = priceId;
        this.startValue = startValue;
        this.endValue = endValue;
        this.amount = amount;
        this.formulaId = formulaId;
        this.limitAmount = limitAmount;
        this.tenantId = tenantId;
    }
}
