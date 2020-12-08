package com.cmit.mvne.billing.user.analysis.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * <p>
 * Pricing plan table of recurring free resource. Cycle follows
 * </p>
 *
 * @author Luxf
 * @since 2020-01-20
 */
@Data
@TableName("pm_allowance_freeres_details")
public class PmAllowanceFreeresDetails  {


    @TableId(value = "PRICE_ID", type = IdType.INPUT)
    private Long priceId;

    @TableField("FREERES_ITEM")
    private Long freeresItem;

    @TableField("TITLE_ROLE_ID")
    private Long titleRoleId;

    @TableField("INVALID_PROD_USE_FLAG")
    private BigDecimal invalidProdUseFlag;

    @TableField("CYCLE_REF_MODE")
    private BigDecimal cycleRefMode;

    @TableField("CYCLE_UNIT")
    private BigDecimal cycleUnit;

    @TableField("CYCLE_TYPE")
    private BigDecimal cycleType;

    @TableField("REF_MEMBER_NUM")
    private BigDecimal refMemberNum;

    @TableField("CYCLE_NUM_FORMULA")
    private BigDecimal cycleNumFormula;

    @TableField("TENANT_ID")
    private BigDecimal tenantId;

    public PmAllowanceFreeresDetails(Long priceId, Long freeresItem, Long titleRoleId, BigDecimal invalidProdUseFlag, BigDecimal cycleRefMode, BigDecimal cycleUnit, BigDecimal cycleType, BigDecimal refMemberNum, BigDecimal cycleNumFormula, BigDecimal tenantId) {
        this.priceId = priceId;
        this.freeresItem = freeresItem;
        this.titleRoleId = titleRoleId;
        this.invalidProdUseFlag = invalidProdUseFlag;
        this.cycleRefMode = cycleRefMode;
        this.cycleUnit = cycleUnit;
        this.cycleType = cycleType;
        this.refMemberNum = refMemberNum;
        this.cycleNumFormula = cycleNumFormula;
        this.tenantId = tenantId;
    }
}
