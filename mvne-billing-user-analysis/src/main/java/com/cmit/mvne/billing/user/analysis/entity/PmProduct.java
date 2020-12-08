package com.cmit.mvne.billing.user.analysis.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author Luxf
 * @since 2020-01-20
 */
@Data
@TableName("pm_product")
public class PmProduct {

    /**
     * 销售品ID
     */
    @TableId(value = "PRODUCT_OFFERING_ID", type = IdType.INPUT)
    private Long productOfferingId;

    /**
     * 产品资费，单位：欧分
     */
    @TableField("FEE")
    private BigDecimal fee;

    /**
     * 适用区域
     */
    @TableField("ZONE_GROUP")
    private String zoneGroup;

    /**
     * group id
     */
    @TableField("GROUP_ID")
    private Integer groupId;

    /**
     * 产品名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 产品的有效期（单位：月）
     */
    @TableField("EFFECTIVE_TIME")
    private Integer effectiveTime;

    /**
     * 产品生效时间
     */
    @TableField("EFFECTIVE_DATE")
    private Date effectiveDate;

    /**
     * 产品失效时间
     */
    @TableField("EXPIRE_DATE")
    private Date expireDate;

    /**
     * 描述
     */
    @TableField("PROD_DESC")
    private String prodDesc;


    public PmProduct(String productName, BigDecimal productFee, String zone, int effectiveTime, Date effectiveDate, Date expireDate) {
        this.name = productName;
        this.fee = productFee;
        this.zoneGroup = zone;
        this.effectiveTime = effectiveTime;
        this.effectiveDate = effectiveDate;
        this.expireDate = expireDate;
    }

    public PmProduct(Long productId, Long expireDate) {
        this.setProductOfferingId(productId);
        this.setExpireDate(new Date(expireDate));
    }
}
