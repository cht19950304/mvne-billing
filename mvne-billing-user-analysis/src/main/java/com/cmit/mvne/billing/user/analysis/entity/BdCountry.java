package com.cmit.mvne.billing.user.analysis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("bd_country")
public class BdCountry {

    /**
     * 唯一的国家码
     */
    @TableField("ISO")
    private String iso;

    /**
     * 国家英文全称
     */
    @TableField("COUNTRY_NAME")
    private String countryName;

    /**
     * 国家的描述，比如中文名等
     */
    @TableField("COUNTRY_DESCRIPTION")
    private String countryDescription;


}
