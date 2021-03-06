/**
 * 
 */
package com.cmit.mvne.billing.user.analysis.entity;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author jiangxm02
 *
 */
@Data
@JSONType(orders={"orderId","userId","rectype","productFee","productId","productInsId","validDate","expireDate"})
public class IProdDto {
	@NotNull
	private Long orderId;
	@NotNull
	private Long userId;

	private String rectype;

	@NotNull
	private BigDecimal productFee;
	@NotNull
	private Long productId;
	@NotNull
	private Long productInsId;
	@NotNull
	private Long validDate;
	@NotNull
	private Long expireDate;
	
//	public IProdDto() {
//		super();
//	}
//	
//	public IProdDto(Long orderId, Long userId, String rectype, Long productId, Long productInsId, Long validDate,
//			Long expireDate) {
//		super();
//		this.orderId = orderId;
//		this.userId = userId;
//		this.rectype = rectype;
//		this.productId = productId;
//		this.productInsId = productInsId;
//		this.validDate = validDate;
//		this.expireDate = expireDate;
//	}
//
//
//	public Long getOrderId() {
//		return orderId;
//	}
//
//	public void setOrderId(Long orderId) {
//		this.orderId = orderId;
//	}
//
//	public Long getUserId() {
//		return userId;
//	}
//
//	public void setUserId(Long userId) {
//		this.userId = userId;
//	}
//
//	public String getRectype() {
//		return rectype;
//	}
//
//	public void setRectype(String rectype) {
//		this.rectype = rectype;
//	}
//
//	public Long getProductId() {
//		return productId;
//	}
//
//	public void setProductId(Long productId) {
//		this.productId = productId;
//	}
//
//	public Long getProductInsId() {
//		return productInsId;
//	}
//
//	public void setProductInsId(Long productInsId) {
//		this.productInsId = productInsId;
//	}
//
//	public Long getValidDate() {
//		return validDate;
//	}
//
//	public void setValidDate(Long validDate) {
//		this.validDate = validDate;
//	}
//
//	public Long getExpireDate() {
//		return expireDate;
//	}
//
//	public void setExpireDate(Long expireDate) {
//		this.expireDate = expireDate;
//	}

	@Override
	public String toString() {
		return "IProdDto [orderId=" + orderId + ", userId=" + userId + ", rectype=" + rectype +", productFee" + productFee + ", productId="
				+ productId + ", productInsId=" + productInsId + ", validDate=" + validDate + ", expireDate="
				+ expireDate + "]";
	}
	
}
