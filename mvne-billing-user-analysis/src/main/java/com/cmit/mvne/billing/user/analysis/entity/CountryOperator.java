package com.cmit.mvne.billing.user.analysis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;


/**
 * <p>
 * VIEW
 * </p>
 *
 * @author Luxf
 * @since 2020-02-24
 */
@Data
@TableName("country_operator")
public class CountryOperator  {

    @TableField("OPERATOR_CODE")
    private String operatorCode;

    private String operatorName;

    @TableField("COUNTRY_ID")
    private Integer countryId;

    private String countryName;


}
