/**
 * 
 */
package com.cmit.mvne.billing.creditcontrol.entity;

import javax.validation.constraints.NotBlank;

import lombok.Data;

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
}
