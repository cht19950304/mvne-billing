package com.cmit.mvne.billing.user.analysis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;


/**
 * <p>
 * 
 * </p>
 *
 * @author Luxf
 * @since 2020-01-21
 */
@Data
@TableName("sys_operator_def")
public class SysOperatorDef  {

    @TableId(value = "OPERATOR_ID", type = IdType.INPUT)
    private Integer operatorId;

    @TableField("OPERATOR_CODE")
    private String operatorCode;

    @TableField("OPERATOR_TYPE")
    private Integer operatorType;

    @TableField("NAME")
    private String name;

    @TableField("SIMPLE_NAME")
    private String simpleName;

    @TableField("SHORT_NAME")
    private String shortName;

    @TableField("REG_NO")
    private String regNo;

    @TableField("TAX_NO")
    private String taxNo;

    @TableField("LOGO")
    private String logo;

    @TableField("EMAIL")
    private String email;

    @TableField("BILL_FORMAT_ID")
    private Integer billFormatId;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("TENANT_ID")
    private Long tenantId;

    @TableField("IS_TENANT")
    private Integer isTenant;


}
