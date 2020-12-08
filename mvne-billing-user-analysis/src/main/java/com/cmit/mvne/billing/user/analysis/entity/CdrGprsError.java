package com.cmit.mvne.billing.user.analysis.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/3/1
 */
@Data
@TableName("rating_cdr_gprs_error")
public class CdrGprsError {

    @NumberFormat(value = "0")
    @ExcelProperty("Rec. No.")
    @TableId(value = "TABLE_ID", type = IdType.AUTO)
    private Long tableId;

    @NumberFormat(value = "0")
    @ExcelIgnore
    @TableField(value = "ID")
    private Long id;

    /**
     * RecordType is the identifier to differentiate the service type
     * <p>
     * Conditionality:Mandatory.
     * <p>
     * Values:
     * RGP – GPRS record
     * RSMO –SMS originating record
     * RMT –SMS terminating record
     * GP –Native GPRS record (GGSN records)
     * SMS –Native SMS originating record
     */
    @ExcelIgnore
    @NotNull
    @TableField("RECORD_TYPE")
    private String recordType;

    /**
     * The MSISDN number of Roaming Subscriber
     * Conditionality: Mandatory
     */
    @ExcelIgnore
    @TableField("NUMBER_A")
    private String numberA;

    @ExcelIgnore
    @TableField("NUMBER_B")
    private String NumberB;

    /**
     * Number Dialed is the international representation of the SMSC address used
     * Conditionality:
     * Mandatory within the below record:
     * RSMO –SMS originating record
     * SMS –Native SMS originating record
     */
    @ExcelIgnore
    @TableField("NUMBER_DIALED")
    private String numberDialed;

    /**
     * The Mobile Subscriber ISDN number.
     * Conditionality:  Mandatory
     */
    @ExcelProperty("MSISDN")
    @NotNull
    @TableField("MSISDN")
    private String msisdn;

    /**
     * The identifier which uniquely identifies the subscriber who has used the network.
     * Conditionality: Mandatory
     */
    @ExcelIgnore
    @NotNull
    @TableField("IMSI")
    private String imsi;

    /**
     * The timestamp gives the start of the call event.
     * The time is given in the local time of the Sender PMN (or Serving Network).
     */
    @ExcelIgnore
    @TableField("EVENT_TIME_STAMP")
    private String eventTimeStamp;

    /**
     * The timestamp gives the start of the call event.
     * Transformed to TimeZone.getTimeZone
     */
    @ExcelProperty("Call event start time(Icelandic time)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("LOCAL_EVENT_TIME_STAMP")
    private Date localEventTimeStamp;

    /**
     * The item contains the actual total duration of a call event as a number of seconds.
     * Conditionality:Mandatory
     * Format:> or =  0
     */
    @ExcelIgnore
    @NotNull
    @TableField("EVENT_DURATION")
    private Integer eventDuration;

    /**
     * The item identifies the number of incoming Volumn
     * Conditionality:Mandatory within GPRS record
     * Values:> or = 0 (zero)
     */
    @ExcelIgnore
    @TableField("DOWNLOAD_VOL")
    private BigDecimal downloadVol;

    /**
     * The item identifies the number of outgoing Volumn
     * Conditionality:Mandatory within GPRS record
     * Values:> or = 0 (zero)
     */
    @ExcelIgnore
    @TableField("UPLOAD_VOL")
    private BigDecimal uploadVol;

    /**
     * A unique identifier used to determine the network of roaming partner.
     * The full list of codes in use is given in TADIG PRD TD.13
     * <p>
     * Conditionality:
     * Mandatory within the below record:
     * RGP – GPRS record
     * RSMO –SMS originating record
     * RMT –SMS terminating record
     * <p>
     * Example content:SWEHU GBRCN HUNH2
     */
    @ExcelIgnore
    @NotNull
    @TableField("OPERATOR_CODE")
    private String operatorCode;

    /**
     * The charge for the Prepaid serivce
     * Conditionality: Not mandatory
     * Values:> or = 0 (zero)
     */
    @ExcelIgnore
    @TableField("PRERATED_AMOUNT")
    private String preratedAmount;

    /**
     * The Network Identifier part of the Access Point Name (APN)
     * Conditionality:Mandatory within GPRS record
     */
    @ExcelIgnore
    @TableField("APN")
    private String apn;

    @ExcelIgnore
    @TableField("NULLI")
    private String nulli;

    @ExcelIgnore
    @TableField("BROAD_WORKS")
    private String broadWorks;

    /**
     * A code which uniquely defines a TeleService.
     * <p>
     * Conditionality: Not mandatory
     */
    @ExcelIgnore
    @TableField("TELE_SERVICE_CODE")
    private String teleServiceCode;

    /**
     * A code which uniquely defines a Bearer Service.
     * <p>
     * Conditionality:mandatory
     */
    @ExcelIgnore
    @TableField("BEARER_SERVICE_CODE")
    private String bearerServiceCode;

    @ExcelIgnore
    @TableField("OVERSEAS_CODE")
    private String overseasCode;

    /**
     * A code is the identifier to differentiate the service is whether or not a video call.
     * <p>
     * Conditionality:Not mandatory
     */
    @ExcelIgnore
    @TableField("VIDEO_INDICATOR")
    private String videoIndicator;

    /**
     * A code is the identifier of the network element resourse who will generate the record.
     * <p>
     * Conditionality:Mandatory
     */
    @ExcelIgnore
    @NotNull
    @TableField("SOURCE")
    private String source;

    /**
     * A code is the identifier for the service type
     * Conditionality:Not mandatory
     */
    @ExcelIgnore
    @TableField("SERVICE_ID")
    private String serviceId;

    /**
     * A code is the identifier for the service Quantity
     * <p>
     * Conditionality:Not mandatory
     */
    @ExcelIgnore
    @TableField("QUANTITY")
    private String quantity;

    @ExcelIgnore
    @TableField("CUST_NUMBER")
    private String custNumber;

    @ExcelIgnore
    @TableField("DESCRIPTION")
    private String description;

    /**
     * A code is the record unique identifier
     * <p>
     * Conditionality: Mandatory
     */
    @ExcelIgnore
    @TableField("CALL_IDENTIFICATION")
    private String callIdentification;

    /**
     * Cdr file name
     */
    @ExcelProperty("File name")
    @NotNull
    @TableField("ORIGINAL_FILE")
    private String originalFile;

    /**
     * 尾号
     */
    @ExcelIgnore
    @TableField("TAIL_NUMBER")
    private String tailNumber;

    /**
     * 行号
     */
    @ExcelIgnore
    @TableField("LINE_NUMBER")
    private Long lineNumber;

    /**
     * 用户Id
     */
    @ExcelIgnore
    @TableField("USER_ID")
    private Long userId;

    /**
     * 产品Id
     */
    @ExcelIgnore
    @TableField("PRODUCT_ID")
    private Long productId;

    /**
     * 产品实例Id
     */
    @ExcelIgnore
    @TableField("PRODUCT_INS_ID")
    private Long productInsId;

    /**
     * 批价科目Id
     */
    @ExcelIgnore
    @TableField("ITEM_ID")
    private Long itemId;

    //************************* rating 字段 *****************************//
    /**
     * 话单错误码
     */
    @ExcelProperty("Error Code")
    @TableField("ERR_CODE")
    private String errCode;

    /**
     * 话单错误描述
     */
    @ExcelIgnore
    @TableField("ERR_DESC")
    private String errDesc;

    //************************* rating 字段 *****************************//

    /**
     * MVNO BOSS收到Siminn发送文件时间
     * 取得时Siminn将文件放在ftp服务器上的时间
     */
    @ExcelIgnore
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("RECEIVE_TIME")
    private Date receiveTime;

    /**
     * the time of insert into database
     */
    @ExcelIgnore
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * the time of finish rating or settle
     */
    @ExcelProperty("Error Creation time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "FINISH_TIME")
    private Date finishTime;

    /**
     * the time of redo
     */
    @ExcelProperty("Re-processing time")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "REDO_TIME")
    private Date redoTime;

    /**
     * redo flag
     */
    @ExcelProperty("Reprocess status")
    @TableField("REDO_FLAG")
    private String redoFlag;

    /**
     * rerat flag
     */
    @ExcelIgnore
    @TableField("RERAT_FLAG")
    private String reratFlag;

    public CdrGprsError(CdrGprs cdrGprs) {
        BeanUtils.copyProperties(cdrGprs, this);
    }


    public String getDupKey() {
        StringBuilder sb = new StringBuilder();
        return sb.append(recordType).append(msisdn).append(imsi)
                .append(eventTimeStamp).append(callIdentification).toString();
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }

}
