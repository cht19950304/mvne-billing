package com.cmit.mvne.billing.user.analysis.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author Luxf
 * @since 2020-06-10
 */
@Data
@TableName("sys_sms_model")
public class SysSmsModel  {
    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 信控原因
     */
    @TableField("REASON")
    private String reason;

    /**
     * 信控操作
     */
    @TableField("OPERATION")
    private String operation;

    /**
     * 信控短信内容
     */
    @TableField("TEXT")
    private String text;

    /**
     * 说明
     */
    @TableField("DESCRIPTION")
    private String description;
}
