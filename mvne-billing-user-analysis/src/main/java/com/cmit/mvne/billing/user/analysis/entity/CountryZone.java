package com.cmit.mvne.billing.user.analysis.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author Luxf
 * @since 2020-07-30
 */
@Data
@TableName("country_zone")
public class CountryZone  {

    @TableField("COUNTRY_NAME")
    private String countryName;

    @TableField("COUNTRY_INITIALS")
    private String countryInitials;

    /**
     * 归属的漫游区域
     */
    @TableField("COUNTRY_GROUP")
    private String countryGroup;

    @TableField("STATUS")
    private String status;

    /**
     * 生效时间
     */
    @TableField("VALID_DATE")
    private Date validDate;

    /**
     * 失效时间
     */
    @TableField("EXPIRE_DATE")
    private Date expireDate;


}
