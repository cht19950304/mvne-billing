package com.cmit.mvne.billing.preparation.entity.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 错误话单或者错误文件vo对象
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/29
 */
@Data
public class RejectedDTO {
    @ExcelProperty("Rec No.")
    private Long id;
    @ExcelProperty("Error Type")
    private String errorType;
    @NotNull(message = "filename cannot be null!")
    @ExcelProperty("File Name")
    private String filename;
    @ExcelProperty("Error Code")
    private String errorCode;
    @NotNull(message = "recordNumber cannot be null!")
    @ExcelIgnore
    private String recordNumber;
    @ExcelProperty("File Receive Time")
    private Date fileReceiveTime;
    @ExcelProperty("Error Creation Time")
    private Date errorCreationTime;
    @ExcelProperty("Reprocess Time")
    private Date reProcessTime;
    @ExcelProperty("Reprocess Status")
    private String status;

}
