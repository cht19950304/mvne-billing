package com.cmit.mvne.billing.user.analysis.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;


/**
 * <p>
 * Definition of rate curve segment.
Start from 1 and end
 * </p>
 *
 * @author Luxf
 * @since 2020-01-20
 */
@Data
@TableName("pm_rating_curve_detail")
public class PmRatingCurveDetail {

    @TableId(value = "CURVE_ID", type = IdType.INPUT)
    private Long curveId;

    @TableField("SEGMENT_ID")
    private Long segmentId;

    @TableField("START_VAL")
    private BigDecimal startVal;

    @TableField("END_VAL")
    private BigDecimal endVal;

    @TableField("BASE_VAL")
    private BigDecimal baseVal;

    @TableField("RATE_VAL")
    private BigDecimal rateVal;

    @TableField("CURVE_CYCLE_UNIT")
    private BigDecimal curveCycleUnit;

    @TableField("TENANT_ID")
    private BigDecimal tenantId;

    public PmRatingCurveDetail(Long curveId, Long segmentId, BigDecimal startVal, BigDecimal endVal, BigDecimal baseVal, BigDecimal rateVal, BigDecimal curveCycleUnit, BigDecimal tenantId) {
        this.curveId = curveId;
        this.segmentId = segmentId;
        this.startVal = startVal;
        this.endVal = endVal;
        this.baseVal = baseVal;
        this.rateVal = rateVal;
        this.curveCycleUnit = curveCycleUnit;
        this.tenantId = tenantId;
    }
}
