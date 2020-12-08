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
 * @since 2020-01-21
 */
@Data
@TableName("zone_group")
public class ZoneGroup  {

    @TableField("COUNTRY_ID")
    private Integer countryId;

    @TableField("NAME")
    private String name;

    private String simpleName;

    /**
     * 唯一的区域码
     */
    @TableField("ISO")
    private String iso;

    /**
     * 归属的漫游区域
     */
    @TableField("COUNTRY_ZONE")
    private String countryZone;

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
