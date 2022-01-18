/**
 * 
 */
package com.cmit.mvne.billing.user.analysis.entity;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author jiangxm02
 *
 */
@Data
@JSONType(orders={"orderId","userId","acctId","custId","msisdn","imsi","channelCode","validDate","expireDate","userStatus"})
public class IUserDto {
	@NotNull
	private Long orderId;
	@NotNull
	private Long userId;
	private Long acctId;
	private Long custId;
	@NotNull
	private String msisdn;
	@NotNull
	private String imsi;
	@NotNull
	private String channelCode;
	@NotNull
	private Long validDate;
	@NotNull
	private Long expireDate;
	@NotNull
	private String userStatus;
	
	
//	public IUserDto() {
//		super();
//	}
//	
//	public IUserDto(Long orderId, Long userId, Long acctId, Long custId, String msisdn, String imsi, String channelCode,
//			Long validDate, Long expireDate, String userStatus) {
//		super();
//		this.orderId = orderId;
//		this.userId = userId;
//		this.acctId = acctId;
//		this.custId = custId;
//		this.msisdn = msisdn;
//		this.imsi = imsi;
//		this.channelCode = channelCode;
//		this.validDate = validDate;
//		this.expireDate = expireDate;
//		this.userStatus = userStatus;
//	}
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
//	public Long getAcctId() {
//		return acctId;
//	}
//
//	public void setAcctId(Long acctId) {
//		this.acctId = acctId;
//	}
//
//	public Long getCustId() {
//		return custId;
//	}
//
//	public void setCustId(Long custId) {
//		this.custId = custId;
//	}
//
//	public String getMsisdn() {
//		return msisdn;
//	}
//
//	public void setMsisdn(String msisdn) {
//		this.msisdn = msisdn;
//	}
//
//	public String getImsi() {
//		return imsi;
//	}
//
//	public void setImsi(String imsi) {
//		this.imsi = imsi;
//	}
//
//	public String getChannelCode() {
//		return channelCode;
//	}
//
//	public void setChannelCode(String channelCode) {
//		this.channelCode = channelCode;
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
//
//	public String getUserStatus() {
//		return userStatus;
//	}
//
//	public void setUserStatus(String userStatus) {
//		this.userStatus = userStatus;
//	}

	@Override
	public String toString() {
		return "IUserDto [orderId=" + orderId + ", userId=" + userId + ", acctId=" + acctId + ", custId=" + custId
				+ ", msisdn=" + msisdn + ", imsi=" + imsi + ", channelCode=" + channelCode + ", validDate=" + validDate
				+ ", expireDate=" + expireDate + ", userStatus=" + userStatus + "]";
	}
	
	
}
