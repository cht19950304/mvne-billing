package com.cmit.mvne.billing.user.analysis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author Luxf
 * @since 2020-01-20
 */
@Data
@TableName("sys_measure_unit_exchange")
public class SysMeasureUnitExchange  {

    @TableId(value = "MEASURE_ID", type = IdType.INPUT)
    private BigDecimal measureId;

    @TableField("DEST_MEASURE_ID")
    private BigDecimal destMeasureId;

    @TableField("MEASURE_EXCHANGE_ID")
    private BigDecimal measureExchangeId;

    @TableField("EXCHANGE_NUMERATOR")
    private BigDecimal exchangeNumerator;

    @TableField("EXCHANGE_DENOMINATOR")
    private BigDecimal exchangeDenominator;

    @TableField("TENANT_ID")
    private Long tenantId;


}
