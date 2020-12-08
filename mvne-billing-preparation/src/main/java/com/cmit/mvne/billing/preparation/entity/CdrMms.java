//package com.cmit.mvne.billing.preparation.entity;
//
//import lombok.Data;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotNull;
//import java.math.BigDecimal;
//import java.util.Date;
//
///**
// * 彩信话单，超类，包含彩信话单公共字段
// *
// * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
// * @since 2019/12/16
// */
//@Data
//@MappedSuperclass
//public class CdrMms {
//
//
//    /**
//     * RecordType is the identifier to differentiate the service type
//     *
//     * Conditionality:Mandatory.
//     *
//     * Values:
//     * RGP – GPRS record
//     * RSMO –SMS originating record
//     * RMT –SMS terminating record
//     * GP –Native GPRS record (GGSN records)
//     * SMS –Native SMS originating record
//     */
//    @NotNull
//    @Column(name = "RECORD_TYPE", nullable = false)
//    private String recordType;
//
//    /**
//     * The MSISDN number of Roaming Subscriber
//     * Conditionality: Mandatory
//     */
//    @Column(name="NUMBER_A")
//    private String numberA;
//
//    @Column(name="NUMBER_B")
//    private String numberB;
//
//    /**
//     * Number Dialed is the international representation of the SMSC address used
//     * Conditionality:
//     * Mandatory within the below record:
//     * RSMO –SMS originating record
//     * SMS –Native SMS originating record
//     */
//    @Column(name="NUMBER_DIALED")
//    private String numberDialed ;
//
//    /**
//     * The Mobile Subscriber ISDN number.
//     * Conditionality:  Mandatory
//     */
//    @NotNull
//    @Column(name="MSISDN", nullable = false)
//    private String msisdn;
//
//    /**
//     * The identifier which uniquely identifies the subscriber who has used the network.
//     * Conditionality: Mandatory
//     */
//    @NotNull
//    @Column(name="IMSI", nullable = false)
//    private String imsi;
//
//    /**
//     * The timestamp gives the start of the call event.
//     * The time is given in the local time of the Sender PMN (or Serving Network).
//     *
//     */
//    @Column(name="EVENT_TIME_STAMP")
//    private String eventTimestamp;
//
//    /**
//     * The timestamp gives the start of the call event.
//     * Transformed to TimeZone.getTimeZone
//     */
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name="LOCAL_EVENT_TIME_STAMP")
//    private Date localEventTimestamp;
//
//    /**
//     * The item contains the actual total duration of a call event as a number of seconds.
//     * Conditionality:Mandatory
//     * Format:> or =  0
//     */
//    @NotNull
//    @Column(name="EVENT_DURATION", nullable = false)
//    private Integer eventDuration ;
//
//    /**
//     * The item identifies the number of incoming Volumn
//     * Conditionality:Mandatory within GPRS record
//     * Values:> or = 0 (zero)
//     */
//    @Column(name = "DOWNLOAD_VOL")
//    private BigDecimal downloadVol;
//
//    /**
//     * The item identifies the number of outgoing Volumn
//     * Conditionality:Mandatory within GPRS record
//     * Values:> or = 0 (zero)
//     */
//    @Column(name = "UPLOAD_VOL")
//    private BigDecimal uploadVol;
//
//    /**
//     * A unique identifier used to determine the network of roaming partner.
//     * The full list of codes in use is given in TADIG PRD TD.13
//     *
//     * Conditionality:
//     * Mandatory within the below record:
//     * RGP – GPRS record
//     * RSMO –SMS originating record
//     * RMT –SMS terminating record
//     *
//     * Example content:SWEHU GBRCN HUNH2
//     */
//    @NotNull
//    @Column(name="OPERATOR_CODE", nullable = false)
//    private String operatorCode;
//
//    /**
//     * The charge for the Prepaid serivce
//     * Conditionality: Not mandatory
//     * Values:> or = 0 (zero)
//     */
//    @Column(name = "PRERATED_AMOUNT")
//    private String preratedAmount;
//
//    /**
//     * The Network Identifier part of the Access Point Name (APN)
//     * Conditionality:Mandatory within GPRS record
//     */
//    @Column(name = "APN")
//    private String apn;
//
//    @Column(name = "NULLI")
//    private String nulli;
//
//    @Column(name = "BROAD_WORKS")
//    private String broadWorks;
//
//    /**
//     * A code which uniquely defines a TeleService.
//     *
//     * Conditionality: Not mandatory
//     */
//    @Column(name = "TELE_SERVICE_CODE")
//    private String teleServiceCode;
//
//    /**
//     * A code which uniquely defines a Bearer Service.
//     *
//     * Conditionality:mandatory
//     */
//    @Column(name = "BEARER_SERVICE_CODE")
//    private String bearerServiceCode;
//
//    @Column(name = "OVERSEAS_CODE")
//    private String overseasCode;
//
//    /**
//     * A code is the identifier to differentiate the service is whether or not a video call.
//     *
//     * Conditionality:Not mandatory
//     */
//    @Column(name = "VIDEO_INDICATOR")
//    private String videoIndicator;
//
//    /**
//     * A code is the identifier of the network element resourse who will generate the record.
//     *
//     * Conditionality:Mandatory
//     */
//    @NotNull
//    @Column(name = "SOURCE", nullable = false)
//    private String source;
//
//    /**
//     * A code is the identifier for the service type
//     * Conditionality:Not mandatory
//     */
//    @Column(name = "SERVICE_ID")
//    private String serviceId ;
//
//    /**
//     * A code is the identifier for the service Quantity
//     *
//     * Conditionality:Not mandatory
//     */
//    @Column(name = "QUANTITY")
//    private String quantity ;
//
//    @Column(name = "CUST_NUMBER")
//    private String custNumber;
//
//    @Column(name = "DESCRIPTION")
//    private String description;
//
//    /**
//     * A code is the record unique identifier
//     *
//     * Conditionality: Mandatory
//     */
//    @Column(name = "CALL_IDENTIFICATION")
//    private String callIdentification;
//
//    /**
//     * Cdr file name
//     */
//    @NotNull
//    @Column(name = "ORIGINAL_FILE", nullable = false)
//    private String originalFile;
//
//    /**
//     * MVNO BOSS收到Siminn发送文件时间
//     * 取得时Siminn将文件放在ftp服务器上的时间
//     */
//    @NotNull
//    @Column(name = "RECEIVE_TIME")
//    private Date receiveTime;
//
//    /**
//     * the time of insert into database
//     */
//    @org.hibernate.annotations.CreationTimestamp
//    @Column(name = "CREATE_TIME")
//    private Date createTime;
//
//    /**
//     * the time of finish rating or settle
//     */
//    @Column(name = "FINISH_TIME")
//    private Date finishTime;
//
//    /**
//     * 行号
//     */
//    @Transient
//    private int lineNum;
//
//    @Override
//    public String toString() {
//        final StringBuffer sb = new StringBuffer();
//        sb.append(recordType).append("|");
//        sb.append(numberA).append("|");
//        sb.append(numberB).append("|");
//        sb.append(numberDialed).append("|");
//        sb.append(msisdn).append("|");
//        sb.append(imsi).append("|");
//        sb.append(eventTimestamp).append("|");
//        sb.append(eventDuration).append("|");
//        sb.append(downloadVol).append("|");
//        sb.append(uploadVol).append("|");
//        sb.append(operatorCode).append("|");
//        sb.append(preratedAmount).append("|");
//        sb.append(apn).append("|");
//        sb.append(nulli).append("|");
//        sb.append(broadWorks).append("|");
//        sb.append(teleServiceCode).append("|");
//        sb.append(bearerServiceCode).append("|");
//        sb.append(overseasCode).append("|");
//        sb.append(videoIndicator).append("|");
//        sb.append(source).append("|");
//        sb.append(serviceId).append("|");
//        sb.append(quantity).append("|");
//        sb.append(custNumber).append("|");
//        sb.append(description).append("|");
//        sb.append(callIdentification).append("|");
//        return sb.toString();
//    }
//
//
//    public String getDupKey() {
//        StringBuilder sb = new StringBuilder();
//        return sb.append(recordType).append(msisdn).append(imsi)
//                .append(eventTimestamp).append(callIdentification).toString();
//    }
//}
