package com.cmit.mvne.billing.user.analysis.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * <p>
 * Product offering pricing, associated with all pricing inform
 * </p>
 *
 * @author Luxf
 * @since 2020-01-20
 */
@Data
@TableName("pm_component_prodoffer_price")
public class PmComponentProdofferPrice  {

    @TableId(value = "PRICE_ID", type = IdType.INPUT)
    private Long priceId;

    @TableField("NAME")
    private String name;

    @TableField("PRICE_TYPE")
    private BigDecimal priceType;

    @TableField("SERVICE_SPEC_ID")
    private BigDecimal serviceSpecId;

    @TableField("TAX_INCLUDED")
    private BigDecimal taxIncluded;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("TENANT_ID")
    private BigDecimal tenantId;

    public PmComponentProdofferPrice(Long priceId, String name, BigDecimal priceType, BigDecimal serviceSpecId, BigDecimal taxIncluded, String description, BigDecimal tenantId) {
        this.priceId = priceId;
        this.name = name;
        this.priceType = priceType;
        this.serviceSpecId = serviceSpecId;
        this.taxIncluded = taxIncluded;
        this.description = description;
        this.tenantId = tenantId;
    }
}
