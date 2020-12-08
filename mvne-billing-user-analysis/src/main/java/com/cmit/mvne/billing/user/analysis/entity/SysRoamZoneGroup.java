package com.cmit.mvne.billing.user.analysis.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
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
@TableName("sys_roam_zone_group")
public class SysRoamZoneGroup  {

    /**
     * 区域id，对应sys_country的country_id
     */
    @TableField("COUNTRY_ID")
    private Integer countryId;

    /**
     * 唯一的区域码
     */
    @TableField("ISO")
    private String iso;

    /**
     * 国家英文全称
     */
    @TableField("COUNTRY_NAME")
    private String countryName;

    /**
     * 归属的漫游区域
     */
    @TableField("COUNTRY_GROUP")
    private String countryGroup;

    /**
     * group id
     */
    @TableField("GROUP_ID")
    private Integer groupId;

    /**
     * group name
     */
    @TableField("GROUP_NAME")
    private String groupName;

    /**
     * 生效时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("VALID_DATE")
    private Date validDate;

    /**
     * 失效时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("EXPIRE_DATE")
    private Date expireDate;


}
