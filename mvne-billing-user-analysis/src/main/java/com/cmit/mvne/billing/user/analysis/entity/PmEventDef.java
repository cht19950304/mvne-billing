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
 * @since 2020-01-20
 */
@Data
@TableName("pm_event_def")
public class PmEventDef {

    /**
     * 场景id
     */
    @TableField("EVENT_ID")
    private String eventId;

    /**
     * 业务类型
     */
    @TableField("SERVICE_SPEC_ID")
    private Integer serviceSpecId;


}
