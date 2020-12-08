package com.cmit.mvne.billing.user.analysis.dto;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/6/28 9:07
 */

@Data
public class QueryCdrGprsErrDto {
    @NumberFormat(value = "0")
    @ExcelProperty("Rec. No.")
    private String tableId;

    @ExcelProperty("MSISDN")
    private String msisdn;

    @ExcelProperty("Call event start time(Icelandic time)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date localEventTimeStamp;

    @ExcelProperty("File name")
    private String originalFile;

    @ExcelProperty("Error Code")
    private String errCode;

    @ExcelProperty("Error Creation time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

    @ExcelProperty("Re-processing time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date redoTime;

    @ExcelProperty("Reprocess status")
    private String redoFlag;
}
