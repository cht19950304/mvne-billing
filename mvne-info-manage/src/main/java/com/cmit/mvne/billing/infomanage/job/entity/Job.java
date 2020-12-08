package com.cmit.mvne.billing.infomanage.job.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author MrBird
 */
@Data
@TableName("t_job")
public class Job implements Serializable {

    private static final long serialVersionUID = 400066840871805700L;

    /**
     * 任务调度参数 key
     */
    public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";

    public enum ScheduleStatus {
        /**
         * 正常
         */
        NORMAL("0"),
        /**
         * 暂停
         */
        PAUSE("1");

        private String value;

        ScheduleStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @TableId(value = "JOB_ID", type = IdType.AUTO)
    private Long jobId;

    @TableField("bean_name")
    @NotBlank(message = "{required}")
    @Size(max = 50, message = "{noMoreThan}")
    private String beanName;

    @TableField("method_name")
    @NotBlank(message = "{required}")
    @Size(max = 50, message = "{noMoreThan}")
    private String methodName;

    @TableField("params")
    @Size(max = 50, message = "{noMoreThan}")
    private String params;

    @TableField("cron_expression")
    @NotBlank(message = "{required}")
    private String cronExpression;

    @TableField("status")
    private String status;

    @TableField("remark")
    @Size(max = 100, message = "{noMoreThan}")
    private String remark;

    @TableField("create_time")
    private Date createTime;

    private transient String createTimeFrom;
    private transient String createTimeTo;

}
