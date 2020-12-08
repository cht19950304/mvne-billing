package com.cmit.mvne.billing.user.analysis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
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
@TableName("rs_sys_intl_oper_info")
public class RsSysIntlOperInfo  {

    @TableField("ID")
    private Integer id;

    @TableId(value = "OPERATOR_ID", type = IdType.INPUT)
    private Integer operatorId;

    @TableField("IMSI_HEAD")
    private String imsiHead;

    @TableField("ENGLISH_NAME")
    private String englishName;

    @TableField("COUNTRY_ID")
    private Integer countryId;

    @TableField("PROV_CODE")
    private Integer provCode;

    @TableField("TAP_VERSION")
    private Integer tapVersion;

    @TableField("TAP_RELEASE")
    private Integer tapRelease;

    @TableField("TAP_DECIMAL_PLACES")
    private Integer tapDecimalPlaces;

    @TableField("TAX_TREATMENT")
    private Integer taxTreatment;

    @TableField("TAX_RATE")
    private Long taxRate;

    @TableField("MEASURE_TYPE_ID")
    private Integer measureTypeId;

    @TableField("REPORT_ERROR_TYPE")
    private Integer reportErrorType;

    @TableField("NETWORK_STATEMENT")
    private String networkStatement;

    @TableField("VALID_DATE")
    private Date validDate;

    @TableField("EXPIRE_DATE")
    private Date expireDate;

    @TableField("MODIFY_DATE")
    private Date modifyDate;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("TENANT_ID")
    private Long tenantId;


}
