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
import com.baomidou.mybatisplus.annotation.TableName;
//import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * @author jiangxm02
 *
 */
@Data
@TableName("credit_control_sms_h")
public class CreditControlSmsH {
	
	/**
     * 
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;
    
    /**
     * @Description 发送短信的对端号码
     */
    @TableField("MSISDN")
    @NotBlank
    private String msisdn;
    
    /**
     * @Description 发送短信通知的内容
     */
    @TableField("TEXT")
    @NotBlank
    private String text;
    
    /**
     * @Description 决定是否需要
     */
    @TableField("OPERATION")
    @NotBlank
    private String operation;
    
    /**
     * @description 触发短信提醒的原因
     */
    @TableField("REASON")
    @NotBlank
    private String reason;
    
    /**
     * @Description 短信通知的处理状态
     */
    @TableField("STATUS")
    @NotBlank
    private String status;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_DATE")
    @NotNull
    private Date createDate;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("MODIFY_DATE")
    private Date modifyDate;
}
