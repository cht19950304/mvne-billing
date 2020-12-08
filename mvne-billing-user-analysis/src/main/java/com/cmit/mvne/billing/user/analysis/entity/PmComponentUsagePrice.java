package com.cmit.mvne.billing.user.analysis.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;


/**
 * <p>
 * This measurement ID is mainly used to limit rating mode of s
 * </p>
 *
 * @author Luxf
 * @since 2020-01-20
 */
@Data
@TableName("pm_component_usage_price")
public class PmComponentUsagePrice {

    @TableId(value = "USAGE_ID", type = IdType.INPUT)
    private Long usageId;

    @TableField("CURVE_ATTRIBUTE_ID")
    private Long curveAttributeId;

    @TableField("USAGE_CLASS")
    private BigDecimal usageClass;

    @TableField("TENANT_ID")
    private BigDecimal tenantId;

    public PmComponentUsagePrice(Long usageId, Long curveAttributeId, BigDecimal usageClass, BigDecimal tenantId) {
        this.usageId = usageId;
        this.curveAttributeId = curveAttributeId;
        this.usageClass = usageClass;
        this.tenantId = tenantId;
    }
}
