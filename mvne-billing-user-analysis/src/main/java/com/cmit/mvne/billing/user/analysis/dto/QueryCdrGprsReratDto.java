package com.cmit.mvne.billing.user.analysis.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/6/28 9:08
 */

@Data
@TableName("rating_cdr_gprs_rerat")
public class QueryCdrGprsReratDto {
    @ExcelProperty("Rec. No.")
    @NumberFormat(value = "0")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ExcelProperty("Create time")
    private Date createTime;

    @ExcelProperty("Finish time")
    private Date finishTime;

    @ExcelProperty("Call event start time(Icelandic time)")
    private Date localEventTimeStamp;

    @ExcelProperty("MSISDN")
    private String msisdn;

    @ExcelProperty("File name")
    private String originalFile;

    @ExcelProperty("Original charge")
    private BigDecimal fee1;

    @ExcelProperty("Re-rating charge")
    private BigDecimal fee2;

    @ExcelProperty("Re-create time")
    @TableField(value = "RERAT_TIME")
    private Date reratTime;

    @ExcelProperty("Re-rating status")
    private String reratFlag;
}
