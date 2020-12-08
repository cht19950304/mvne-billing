package com.cmit.mvne.billing.user.analysis.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * <p>
 * Rating tariff curve attribute
 * </p>
 *
 * @author Luxf
 * @since 2020-01-20
 */
@Data
@TableName("pm_rating_curve_attribute")
public class PmRatingCurveAttribute {

    @TableId(value = "CURVE_ATTRIBUTE_ID", type = IdType.INPUT)
    private Long curveAttributeId;

    @TableField("NAME")
    private String name;

    @TableField("CYCLE_UNIT")
    private BigDecimal cycleUnit;

    @TableField("PRECISION_ROUND")
    private BigDecimal precisionRound;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("CYCLE_UNIT_FLAG")
    private BigDecimal cycleUnitFlag;

    @TableField("TENANT_ID")
    private BigDecimal tenantId;

    @TableField("MEASURE_ID")
    private BigDecimal measureId;

    public PmRatingCurveAttribute(Long curveAttributeId, String name, BigDecimal cycleUnit, BigDecimal precisionRound, String description, BigDecimal cycleUnitFlag, BigDecimal tenantId, BigDecimal measureId) {
        this.curveAttributeId = curveAttributeId;
        this.name = name;
        this.cycleUnit = cycleUnit;
        this.precisionRound = precisionRound;
        this.description = description;
        this.cycleUnitFlag = cycleUnitFlag;
        this.tenantId = tenantId;
        this.measureId = measureId;
    }
}
