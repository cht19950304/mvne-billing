package com.cmit.mvne.billing.user.analysis.entity;

import com.alibaba.fastjson.annotation.JSONField;
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
 * @since 2020-01-20
 */
@Data
@TableName("pm_service_spec")
public class PmServiceSpec {

    @TableId(value = "SERVICE_SPEC_ID", type = IdType.INPUT)
    private Integer serviceSpecId;

    @TableField("NAME")
    private String name;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("VALID_DATE")
    private Date validDate;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("EXPIRE_DATE")
    private Date expireDate;

    @TableField("DESCRIPTION")
    private String description;

    @TableField("TENANT_ID")
    private Long tenantId;


}
