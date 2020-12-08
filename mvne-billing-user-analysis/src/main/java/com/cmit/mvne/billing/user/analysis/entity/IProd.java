package com.cmit.mvne.billing.user.analysis.entity;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

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
 * @since 2019-12-10
 */
@Data
@TableName("i_prod")
public class IProd {

    /**
     * 订单Id（唯一）
     */
    @TableId(value = "ORDER_ID", type = IdType.INPUT)
    private Long orderId;

    /**
     * 用户Id
     */
    @TableField("USER_ID")
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
     * 产品Id
     */
    @TableField("PRODUCT_ID")
    private Long productId;

    /**
     * 产品实例Id
     */
    @TableField("PRODUCT_INS_ID")
    private Long productInsId;

    /**
     * 生效时间
     */
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("VALID_DATE")
    private Date validDate;

    /**
     * 失效时间
     */
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("EXPIRE_DATE")
    private Date expireDate;

    public IProd(IProd iProd) {
        this.orderId = iProd.getOrderId();
        this.userId = iProd.getUserId();
        this.rectype = iProd.getRectype();
        this.productFee=iProd.getProductFee();
        this.productId = iProd.getProductId();
        this.productInsId = iProd.getProductInsId();
        this.validDate = iProd.getValidDate();
        this.expireDate = iProd.getExpireDate();
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }

    public IProd(Long orderId, Long userId, String rectype,BigDecimal productFee, Long productId, Long productInsId, Date validDate,
			Date expireDate) {
		super();
		this.orderId = orderId;
		this.userId = userId;
		this.rectype = rectype;
		this.productFee=productFee;
		this.productId = productId;
		this.productInsId = productInsId;
		this.validDate = validDate;
		this.expireDate = expireDate;
	}

	public IProd(IProdDto iProdDto){
        this.orderId = iProdDto.getOrderId();
        this.userId = iProdDto.getUserId();
        this.rectype = iProdDto.getRectype();
        this.productFee=iProdDto.getProductFee();
        this.productId = iProdDto.getProductId();
        this.productInsId = iProdDto.getProductInsId();
        Date prod_validateDate = new Date();
        prod_validateDate.setTime(iProdDto.getValidDate());
        Date prod_expireDate = new Date();
        prod_expireDate.setTime(iProdDto.getExpireDate());
        this.validDate = prod_validateDate;
        this.expireDate = prod_expireDate;
    }
	@Override
	public String toString() {
		return "IProd [orderId=" + orderId + ", userId=" + userId + ", rectype=" + rectype + ",productFee" + productFee+ ", productId=" + productId
				+ ", productInsId=" + productInsId + ", validDate=" + validDate + ", expireDate=" + expireDate + "]";
	}
}
