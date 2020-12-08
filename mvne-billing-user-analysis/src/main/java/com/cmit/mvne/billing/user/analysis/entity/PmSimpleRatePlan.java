package com.cmit.mvne.billing.user.analysis.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * <p>
 * Table of simple tariff pricing plan.
 * </p>
 *
 * @author Luxf
 * @since 2020-01-20
 */
@Data
@TableName("pm_simple_rate_plan")
public class PmSimpleRatePlan  {

    @TableId(value = "TARIFF_DTL_ID", type = IdType.INPUT)
    private Long tariffDtlId;

    @TableField("USAGE_ID")
    private Long usageId;

    @TableField("TENANT_ID")
    private BigDecimal tenantId;

    public PmSimpleRatePlan(Long tariffDtlId, Long usageId, BigDecimal tenantId) {
        this.tariffDtlId = tariffDtlId;
        this.usageId = usageId;
        this.tenantId = tenantId;
    }
}
