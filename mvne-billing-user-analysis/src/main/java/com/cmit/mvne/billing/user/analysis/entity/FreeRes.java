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
 * @since 2020-01-20
 */
@Data
@TableName("free_res")
public class FreeRes {

    @TableField("PRODUCT_ID")
    private Long productId;

    @TableField("FREERES_ITEM")
    private Long freeresItem;

    @TableField("AMOUNT")
    private BigDecimal amount;

    @TableField("MEASURE_ID")
    private BigDecimal measureId;

    public FreeRes() {
        this.productId = 999999999L;
        this.freeresItem = 66020001L;
        this.amount = new BigDecimal(0);
        this.measureId = new BigDecimal(105);
    }
}
