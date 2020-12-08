package com.cmit.mvne.billing.user.analysis.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;


/**
 * <p>
 * 
 * </p>
 *
 * @author Luxf
 * @since 2020-01-20
 */
@Data
//@TableName("cdr_voice_yyyymm")
public class CdrVoice {


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
     * 呼叫类型（主叫、被叫、呼转）
     */
    @TableField("CALL_TYPE")
    private String callType;

    /**
     * 主叫话单主叫号码，被叫话单被叫号码
     */
    @TableField("NUMBER_A")
    private String numberA;

    /**
     * 主叫话单被叫号码，被叫话单主叫号码
     */
    @TableField("NUMBER_B")
    private String numberB;

    /**
     * 被叫号码
     */
    @TableField("NUMBER_DIALED")
    private String numberDialed;

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
     * 通话时长
     */
    @TableField("EVENT_DURATION")
    private Integer eventDuration;

    /**
     * 短信无
     */
    @TableField("DOWNLOAD_VOL")
    private Integer downloadVol;

    /**
     * 短信无
     */
    @TableField("UPLOAD_VOL")
    private Integer uploadVol;

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
    private Integer nulli;

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
    @TableField("CALL_INDENTIFICATION")
    private Integer callIndentification;

    /**
     * 产品id
     */
    @TableField("PRODUCT_ID")
    private Long productId;

    /**
     * 批价费用，单位：欧分
     */
    @TableField("FEE1")
    private Integer fee1;

    /**
     * 保留
     */
    @TableField("FEE2")
    private Integer fee2;

    /**
     * 保留
     */
    @TableField("FEE3")
    private Integer fee3;

    /**
     * 保留
     */
    @TableField("FEE4")
    private Integer fee4;


}
