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

import lombok.Data;

/**
 * @author jiangxm02
 *
 */
@Data
@TableName("credit_control_sms_temp")
public class CreditControlSmsTemp {
	
	@TableId(value = "ID", type = IdType.AUTO)
    private Long id;
	/**
     * @Description 发送短信的对端号码
     */
    @TableField("MSISDN")
    @NotBlank
    private String msisdn;
    /**
     * @description 触发短信提醒的原因
     */
    @TableField("REASON")
    @NotBlank
    private String reason;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_DATE")
    @NotNull
    private Date createDate;
}
