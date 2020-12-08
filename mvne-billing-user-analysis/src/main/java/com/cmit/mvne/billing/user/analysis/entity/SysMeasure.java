package com.cmit.mvne.billing.user.analysis.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * <p>
 * Definition table of global measurement, including currency a
 * </p>
 *
 * @author Luxf
 * @since 2020-01-20
 */
@Data
@TableName("sys_measure")
public class SysMeasure  {

    @TableId(value = "MEASURE_ID", type = IdType.INPUT)
    private BigDecimal measureId;

    @TableField("MEASURE_TYPE_ID")
    private BigDecimal measureTypeId;

    @TableField("NAME")
    private String name;

    @TableField("MEASURE_LEVEL")
    private BigDecimal measureLevel;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("PRECISION_FLAG")
    private BigDecimal precisionFlag;

    @TableField("IS_DISPLAY")
    private BigDecimal isDisplay;

    @TableField("TENANT_ID")
    private BigDecimal tenantId;


}
