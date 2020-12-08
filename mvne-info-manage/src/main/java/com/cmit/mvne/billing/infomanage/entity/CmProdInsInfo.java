package com.cmit.mvne.billing.infomanage.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author Luxf
 * @since 2019-12-03
 */
@Data
@TableName("cm_prod_ins_info")
@JSONType(orders={"userId","rectype","productId","productInsId","validDate","expireDate"})
public class CmProdInsInfo {

    /**
     * 用户Id（唯一）
     */
    @TableId(value = "USER_ID", type = IdType.INPUT)
    private Long userId;

    /**
     * 业务类型
     */
    @TableField("RECTYPE")
    private String rectype;

    /**
     * 产品资费，单位：欧分
     */
    @TableField("PRODUCT_FEE")
    private BigDecimal productFee;

    /**
     * 产品id
     */
    @TableField("PRODUCT_ID")
    private Long productId;

    /**
     * 产品实例id
     */
    @TableField("PRODUCT_INS_ID")
    private Long productInsId;

    /**
     * 生效时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @TableField("VALID_DATE")
    private Date validDate;

    /**
     * 失效时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @TableField("EXPIRE_DATE")
    private Date expireDate;

    /**
     * 免费资源用完时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @TableField("OVER_DATE")
    private Date overDate;

    public CmProdInsInfo(IProd iProd) {
        this.userId = iProd.getUserId();
        this.productId = iProd.getProductId();
        this.productInsId = iProd.getProductInsId();
        this.rectype = iProd.getRectype();
        this.productFee = iProd.getProductFee();
        this.validDate = iProd.getValidDate();
        this.expireDate = iProd.getExpireDate();
        this.overDate = iProd.getExpireDate();
    }

    public CmProdInsInfo(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        this.userId = jsonObject.getLong("userId");
        this.productId = jsonObject.getLong("imsi");
        this.productInsId = jsonObject.getLong("channelCode");
        this.rectype = jsonObject.getString("rectype");
        this.productFee = jsonObject.getBigDecimal("productFee");
        this.validDate = jsonObject.getDate("validDate");
        this.expireDate = jsonObject.getDate("expireDate");
        this.overDate = jsonObject.getDate("overDate");
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }


}
