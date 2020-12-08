package com.cmit.mvne.billing.infomanage.job.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author MrBird
 */
@Data
@TableName("t_job_log")
public class JobLog implements Serializable {

    private static final long serialVersionUID = -7114915445674333148L;
    // 任务执行成功
    public static final String JOB_SUCCESS = "0";
    // 任务执行失败
    public static final String JOB_FAIL = "1";

    @TableId(value = "LOG_ID", type = IdType.AUTO)
    private Long logId;

    @TableField("job_id")
    private Long jobId;

    @TableField("bean_name")
    private String beanName;

    @TableField("method_name")
    private String methodName;

    @TableField("params")
    private String params;

    @TableField("status")
    private String status;

    @TableField("error")
    private String error;

    @TableField("times")
    private Long times;

    @TableField("create_time")
    private Date createTime;

    private transient String createTimeFrom;
    private transient String createTimeTo;

}
