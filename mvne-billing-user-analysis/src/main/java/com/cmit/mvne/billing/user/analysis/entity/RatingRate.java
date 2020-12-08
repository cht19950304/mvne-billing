package com.cmit.mvne.billing.user.analysis.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author Luxf
 * @since 2020-01-21
 */
@Data
@TableName("rating_rate")
public class RatingRate {

    @TableField("PRODUCT_ID")
    private Long productId;

    @TableField("ITEM_ID")
    private Long itemId;

    @TableField("CYCLE_UNIT")
    private BigDecimal cycleUnit;

    @TableField("MEASURE_ID")
    private BigDecimal measureId;

    @TableField("RATE_VAL")
    private BigDecimal rateVal;

    @TableField("MEASURE_MONEY")
    private BigDecimal measureMoney;


}
