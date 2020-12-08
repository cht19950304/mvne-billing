package com.cmit.mvne.billing.user.analysis.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * <p>
 * Atomic rating identifier table.
 * </p>
 *
 * @author Luxf
 * @since 2020-01-20
 */
@Data
@TableName("pm_rating_tariff_dtl")
public class PmRatingTariffDtl  {

    @TableField("TARIFF_DTL_ID")
    private Long tariffDtlId;

    @TableId(value = "PRICE_ID", type = IdType.INPUT)
    private Long priceId;

    @TableField("ITEM_ID")
    private Long itemId;

    @TableField("USAGE_PRICE_TYPE")
    private BigDecimal usagePriceType;

    @TableField("CHARGE_REF_FLAG")
    private BigDecimal chargeRefFlag;

    @TableField("TENANT_ID")
    private BigDecimal tenantId;

    public PmRatingTariffDtl(Long tariffDtlId, Long priceId, Long itemId, BigDecimal usagePriceType, BigDecimal chargeRefFlag, BigDecimal tenantId) {
        this.tariffDtlId = tariffDtlId;
        this.priceId = priceId;
        this.itemId = itemId;
        this.usagePriceType = usagePriceType;
        this.chargeRefFlag = chargeRefFlag;
        this.tenantId = tenantId;
    }
}
