package com.cmit.mvne.billing.user.analysis.entity;

import java.math.BigDecimal;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author Luxf
 * @since 2020-06-03
 */
@Data
@TableName("rating_cdr_gprs_rerat")
public class RatingCdrGprsRerat  {

    @ExcelProperty("Rec. No.")
    @NumberFormat(value = "0")
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    @ExcelIgnore
    private String numberB;

    @ExcelIgnore
    private String apn;

    @ExcelIgnore
    private String bearerServiceCode;

    @ExcelIgnore
    private String broadWorks;

    @ExcelIgnore
    private String callIdentification;

    @ExcelProperty("Create time")
    private Date createTime;

    @ExcelIgnore
    private String custNumber;

    @ExcelIgnore
    private String description;

    @ExcelIgnore
    private BigDecimal downloadVol;

    @ExcelIgnore
    private Integer eventDuration;

    @ExcelIgnore
    private String eventTimeStamp;

    @ExcelProperty("Finish time")
    private Date finishTime;

    @ExcelIgnore
    private String imsi;

    @ExcelProperty("Call event start time(Icelandic time)")
    private Date localEventTimeStamp;

    @ExcelProperty("MSISDN")
    private String msisdn;

    @ExcelIgnore
    private String nulli;

    @ExcelIgnore
    private String numberA;

    @ExcelIgnore
    private String numberDialed;

    @ExcelIgnore
    private String operatorCode;

    @ExcelIgnore
    private String overseasCode;

    @ExcelIgnore
    private String preratedAmount;

    @ExcelIgnore
    private String quantity;

    @ExcelIgnore
    private String recordType;

    @ExcelIgnore
    private String serviceId;

    @ExcelIgnore
    private String source;

    @ExcelIgnore
    private String teleServiceCode;

    @ExcelIgnore
    private BigDecimal uploadVol;

    @ExcelIgnore
    private String videoIndicator;

    @ExcelProperty("File name")
    private String originalFile;

    @ExcelIgnore
    private Long userId;

    @ExcelIgnore
    private Long productId;

    /**
     * 产品实例id
     */
    @ExcelIgnore
    private Long productInsId;

    /**
     * 批价科目id
     */
    @ExcelIgnore
    private Long itemId;

    @ExcelIgnore
    private BigDecimal deductFreeres;

    @ExcelIgnore
    private BigDecimal ratingValue;

    @ExcelProperty("Original charge")
    private BigDecimal fee1;

    @ExcelProperty("Re-rating charge")
    private BigDecimal fee2;

    @ExcelIgnore
    private BigDecimal fee3;

    @ExcelIgnore
    private BigDecimal fee4;

    @ExcelIgnore
    private Date receiveTime;

    /**
     * the time of rerat rating
     */
    @ExcelProperty("Re-create time")
    @TableField(value = "RERAT_TIME")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date reratTime;

    /**
     * 重处理标志
     */
    @ExcelIgnore
    private String redoFlag;

    /**
     * 重批标志
     */
    @ExcelProperty("Re-rating status")
    private String reratFlag;

    @ExcelIgnore
    private String tailNumber;

    @ExcelIgnore
    private Long lineNumber;


}
