/**
 * 
 */
package com.cmit.mvne.billing.creditcontrol.entity;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
//import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * @author jiangxm02
 *
 */
@Data
@TableName("credit_control_sms_err")
public class CreditControlSmsErr {

	@TableId(value = "ID", type = IdType.AUTO)
	private long id;
	
	@TableField("MSISDN")
    @NotBlank
	private String msisdn;
	
	@TableField("TEXT")
    @NotBlank
	private String text;
	
	@TableField("OPERATION")
	@NotBlank
	private String operation;
	
	@TableField("REASON")
    @NotBlank
	private String reason;
	
	@TableField("ERROR_INFO")
    @NotBlank
	private String error_info;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField("CREATE_DATE")
	@NotNull
	private Date createDate;
}
