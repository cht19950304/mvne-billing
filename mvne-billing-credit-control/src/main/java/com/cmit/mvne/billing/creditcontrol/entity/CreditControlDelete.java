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
@TableName("credit_control_delete")
public class CreditControlDelete {
	
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
     * @Description 调用销户接口的结果
     */
    @TableField("RESULT")
    @NotBlank
    private String result;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_DATE")
    @NotNull
    private Date createDate;
}
