package com.cmit.mvne.billing.user.analysis.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;


/**
 * <p>
 * Component tariff package, including <common tariff> and <cyc
 * </p>
 *
 * @author Luxf
 * @since 2020-01-20
 */
@Data
@TableName("pm_component_simple_price")
public class PmComponentSimplePrice  {

    @TableId(value = "USAGE_ID", type = IdType.INPUT)
    private Long usageId;

    @TableField("RATE_ID")
    private Long rateId;

    @TableField("USAGE_CLASS")
    private BigDecimal usageClass;

    @TableField("TENANT_ID")
    private BigDecimal tenantId;

    public PmComponentSimplePrice(Long usageId, Long rateId, BigDecimal usageClass, BigDecimal tenantId) {
        this.usageId = usageId;
        this.rateId = rateId;
        this.usageClass = usageClass;
        this.tenantId = tenantId;
    }
}
