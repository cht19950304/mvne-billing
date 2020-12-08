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
 * @since 2020-01-20
 */
@Data
@TableName("pm_event_item_rel")
public class PmEventItemRel  {

    @TableField("EVENT_ID")
    private String eventId;

    @TableField("ITEM_ID")
    private Long itemId;

    @TableField("DECRIPTION")
    private String decription;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("VALID_DATE")
    private Date validDate;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("EXPIRE_DATE")
    private Date expireDate;

    @TableField("TENANT_ID")
    private Integer tenantId;


}
