/**
 * 
 */
package com.cmit.mvne.billing.rating.gprs.creditcontrol;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description 用于封装来自计费和信管侧的参数
 * @author jiangxm02
 * 
 */
@Data
public class SmsGatewayDto {
	
	@NotBlank
	private String msisdn;
	@NotBlank
	private String text;
	@NotBlank
	private String operation;
	@NotBlank
	private String reason;

	public SmsGatewayDto(String msisdn, String text, String operation, String reason) {
		this.msisdn = msisdn;
		this.text = text;
		this.operation = operation;
		this.reason = reason;
	}

	public String toJsonString() {
		return JSON.toJSONString(this);
	}
}
