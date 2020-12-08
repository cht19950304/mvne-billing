package com.cmit.mvne.billing.user.analysis.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;


/**
 * <p>
 * Rating tariff definition table

 * </p>
 *
 * @author Luxf
 * @since 2020-01-20
 */
@Data
@TableName("pm_rating_rate")
public class PmRatingRate  {

    @TableId(value = "RATE_ID", type = IdType.INPUT)
    private Long rateId;

    @TableField("NAME")
    private String name;

    @TableField("CURVE_ID")
    private Long curveId;

    @TableField("NEGATIVE_FLAG")
    private BigDecimal negativeFlag;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("TENANT_ID")
    private BigDecimal tenantId;

    @TableField("MEASURE_ID")
    private BigDecimal measureId;

    public PmRatingRate(Long rateId, String name, Long curveId, BigDecimal negativeFlag, String description, BigDecimal tenantId, BigDecimal measureId) {
        this.rateId = rateId;
        this.name = name;
        this.curveId = curveId;
        this.negativeFlag = negativeFlag;
        this.description = description;
        this.tenantId = tenantId;
        this.measureId = measureId;
    }
}
