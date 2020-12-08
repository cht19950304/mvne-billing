package com.cmit.mvne.billing.user.analysis.entity;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author Luxf
 * @since 2020-02-18
 */
@Data
//@TableName("err_cdr_gprs_yyyymm")
public class ErrCdrGprs {
    /**
     * 话单的唯一标识
     */
    @TableField("ID")
    private String id;

    /**
     * 话单类型
     */
    @TableField("RECORD_TYPE")
    private String recordType;

    /**
     * 主叫话单主叫号码，被叫话单被叫号码
     */
    @TableField("NUMBER_A")
    private String numberA;

    /**
     * 流量无
     */
    @TableField("NUMBER_B")
    private Integer numberB;

    /**
     * 流量无
     */
    @TableField("NUMBER_DIALED")
    private Integer numberDialed;

    /**
     * 用户号码
     */
    @TableField("MSISDN")
    private String msisdn;

    /**
     * imsi
     */
    @TableField("IMSI")
    private String imsi;

    /**
     * 通话开始时间
     */
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("EVENT_TIME_STAMP")
    private Date eventTimeStamp;

    /**
     * 流量无
     */
    @TableField("EVENT_DURATION")
    private Integer eventDuration;

    /**
     * 下行流量
     */
    @TableField("DOWNLOAD_VOL")
    private BigDecimal downloadVol;

    /**
     * 上行流量
     */
    @TableField("UPLOAD_VOL")
    private BigDecimal uploadVol;

    /**
     * 国际运营商代码
     */
    @TableField("OPERATOR_CODE")
    private String operatorCode;

    /**
     * 不明
     */
    @TableField("PRERATED_AMOUNT")
    private Integer preratedAmount;

    /**
     * 接入点名称
     */
    @TableField("APN")
    private String apn;

    /**
     * 不明
     */
    @TableField("NULLI")
    private String nulli;

    /**
     * 不明
     */
    @TableField("BROAD_WORKS")
    private Integer broadWorks;

    /**
     * 是否国际长途
     */
    @TableField("TELESERVICE_CODE")
    private Integer teleserviceCode;

    /**
     * 不明
     */
    @TableField("BEARERSERVICE_CODE")
    private Integer bearerserviceCode;

    /**
     * 是否国际漫游
     */
    @TableField("OVERSEAS_CODE")
    private Integer overseasCode;

    /**
     * 不明
     */
    @TableField("VIDEO_INDICATOR")
    private Integer videoIndicator;

    /**
     * 不明
     */
    @TableField("SOURCE")
    private Integer source;

    /**
     * 不明
     */
    @TableField("SERVICE_ID")
    private Integer serviceId;

    /**
     * 不明
     */
    @TableField("QUANTITY")
    private Integer quantity;

    /**
     * 不明
     */
    @TableField("CUST_NUMBER")
    private Integer custNumber;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 不明
     */
    @TableField("CALL_IDENTIFICATION")
    private Integer callIdentification;

    /**
     * 原始话单文件名
     */
    @TableField("ORIGINAL_FILE")
    private String originalFile;

    /**
     * 话单错误码
     */
    @TableField("ERR_CODE")
    private String errCode;

    /**
     * 话单错误描述
     */
    @TableField("ERR_DESC")
    private String errDesc;


    public ErrCdrGprs(Cdr cdr, String fileName, String errorCode, String errorDesc) {
        this.apn = cdr.getApn();
        this.bearerserviceCode = cdr.getBearerserviceCode();
        this.broadWorks = cdr.getBroadWorks();
        this.callIdentification = cdr.getCallIdentification();
        this.custNumber = cdr.getCustNumber();
        this.description = cdr.getDescription();
        this.downloadVol = cdr.getDownloadVol();
        this.eventDuration = cdr.getEventDuration();
        this.eventTimeStamp = cdr.getEventTimeStamp();
        this.id = cdr.getId();
        this.imsi = cdr.getImsi();
        this.msisdn = cdr.getMsisdn();
        this.nulli = cdr.getNulli();
        this.numberA = cdr.getNumberA();
        this.numberB = cdr.getNumberB();
        this.numberDialed = cdr.getNumberDialed();
        this.operatorCode = cdr.getOperatorCode();
        this.originalFile = fileName;
        this.overseasCode = cdr.getOverseasCode();
        this.preratedAmount = cdr.getPreratedAmount();
        this.quantity = cdr.getQuantity();
        this.recordType = cdr.getRecordType();
        this.serviceId = cdr.getServiceId();
        this.source = cdr.getSource();
        this.teleserviceCode = cdr.getTeleserviceCode();
        this.uploadVol = cdr.getUploadVol();
        this.videoIndicator = cdr.getVideoIndicator();
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }
}
