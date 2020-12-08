package com.cmit.mvne.billing.user.analysis.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/6/28 9:07
 */

@Data
public class QueryCdrGprsDto {
    @NumberFormat(value = "0")
    @ExcelProperty("Rec. No.")
    private String id;

    @ExcelProperty("MSISDN")
    @NotNull
    @TableField("MSISDN")
    private String msisdn;

    @ExcelProperty("Call event start time(Icelandic time)")
    @TableField("LOCAL_EVENT_TIME_STAMP")
    private Date localEventTimeStamp;

    @ExcelProperty("File name")
    @NotNull
    @TableField("ORIGINAL_FILE")
    private String originalFile;

    @ExcelProperty("Create time")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ExcelProperty("Finish time")
    @TableField(value = "FINISH_TIME")
    private Date finishTime;

}
