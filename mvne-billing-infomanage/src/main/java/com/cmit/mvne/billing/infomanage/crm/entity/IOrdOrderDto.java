/**
 * 
 */
package com.cmit.mvne.billing.infomanage.crm.entity;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

//import org.springframework.format.annotation.DateTimeFormat;

//import com.fasterxml.jackson.annotation.JsonFormat;


/**
 * @author jiangxm02
 *
 */
@Data
@JSONType(orders={"orderId","orderType","busiOperCode","custId","userId","msisdn","factMoney","createDate","doneDate","xferDate"})
public class IOrdOrderDto {
	
	@NotNull
	private Long orderId;
	private String orderType;
	
	//@NotBlank
	private String busiOperCode;
	
	@NotNull
	private Long custId;
	
	@NotNull
	private Long userId;
	@NotNull
	private String msisdn;
	private BigDecimal factMoney;
	
	@NotNull
	private Long createDate;
	@NotNull
	private Long doneDate;
	@NotNull
	private Long xferDate;
	
/*	@NotBlank
	private String updateFlag;*/
	

//	public IOrdOrderDto() {
//		super();
//	}

//	public IOrdOrderDto(@NotNull Long orderId, String orderType, @NotBlank String busiOperCode, @NotNull Long custId,
//			@NotNull Long userId, BigDecimal factMoney, @NotNull Long createDate, Long doneDate, Long xferDate,
//			@NotBlank String updateFlag) {
//		super();
//		this.orderId = orderId;
//		this.orderType = orderType;
//		this.busiOperCode = busiOperCode;
//		this.custId = custId;
//		this.userId = userId;
//		this.factMoney = factMoney;
//		this.createDate = createDate;
//		this.doneDate = doneDate;
//		this.xferDate = xferDate;
//		this.updateFlag = updateFlag;
//	}



//	public Long getOrderId() {
//		return orderId;
//	}
//
//
//	public void setOrderId(Long orderId) {
//		this.orderId = orderId;
//	}
//
//
//	public String getOrderType() {
//		return orderType;
//	}
//
//
//	public void setOrderType(String orderType) {
//		this.orderType = orderType;
//	}
//
//
//	public String getBusiOperCode() {
//		return busiOperCode;
//	}
//
//
//	public void setBusiOperCode(String busiOperCode) {
//		this.busiOperCode = busiOperCode;
//	}
//
//
//	public Long getCustId() {
//		return custId;
//	}
//
//
//	public void setCustId(Long custId) {
//		this.custId = custId;
//	}
//
//
//	public Long getUserId() {
//		return userId;
//	}
//
//
//	public void setUserId(Long userId) {
//		this.userId = userId;
//	}
//
//
//	public BigDecimal getFactMoney() {
//		return factMoney;
//	}
//
//
//	public void setFactMoney(BigDecimal factMoney) {
//		this.factMoney = factMoney;
//	}
//
//
//	public Long getCreateDate() {
//		return createDate;
//	}
//
//
//	public void setCreateDate(Long createDate) {
//		this.createDate = createDate;
//	}
//
//
//	public Long getDoneDate() {
//		return doneDate;
//	}
//
//
//	public void setDoneDate(Long doneDate) {
//		this.doneDate = doneDate;
//	}
//
//
//	public Long getXferDate() {
//		return xferDate;
//	}
//
//
//	public void setXferDate(Long xferDate) {
//		this.xferDate = xferDate;
//	}
//
//
//	public String getUpdateFlag() {
//		return updateFlag;
//	}
//
//
//	public void setUpdateFlag(String updateFlag) {
//		this.updateFlag = updateFlag;
//	}


	@Override
	public String toString() {
		return "IOrdOrderDto [orderId=" + orderId + ", orderType=" + orderType + ", busiOperCode=" + busiOperCode
				+ ", custId=" + custId + ", userId=" + userId + ", msisdn="+msisdn + ", factMoney=" + factMoney + ", createDate="
				+ createDate + ", doneDate=" + doneDate + ", xferDate=" + xferDate + "]";
	}
}
