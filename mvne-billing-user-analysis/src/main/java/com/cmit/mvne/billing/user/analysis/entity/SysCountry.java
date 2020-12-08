package com.cmit.mvne.billing.user.analysis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Luxf
 * @since 2020-01-21
 */
@Data
@TableName("sys_country")
public class SysCountry {

    @TableField("COUNTRY_ID")
    private Integer countryId;

    @TableField("NAME")
    private String name;

    @TableField("NAME_ABBREVIATION")
    private String nameAbbreviation;

    @TableField("COUNTRY_CODE")
    private String countryCode;

    @TableField("GEOGRAPHIC_AREA_ID")
    private Integer geographicAreaId;

    @TableField("TYPE")
    private Integer type;

    @TableField("LANGUAGE_ID")
    private Integer languageId;

    @TableField("TIME_ZONE_ID")
    private Integer timeZoneId;

    @TableField("TIME_ZONE")
    private String timeZone;

    @TableField("TENANT_ID")
    private Long tenantId;


}
