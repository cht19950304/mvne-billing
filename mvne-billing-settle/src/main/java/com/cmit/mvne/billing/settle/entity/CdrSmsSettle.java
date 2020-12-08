package com.cmit.mvne.billing.settle.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

public class CdrSmsSettle {
    private Long id;

    private String recordType;

    private String numberA;

    private String numberB;

    private String numberDialed;

    private String msisdn;

    private String imsi;

    private String eventTimeStamp;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date localEventTimeStamp;

    private Integer eventDuration;

    private BigDecimal downloadVol;

    private BigDecimal uploadVol;

    private String operatorCode;

    private String preratedAmount;

    private String apn;

    private String nulli;

    private String broadWorks;

    private String teleServiceCode;

    private String bearerServiceCode;

    private String overseasCode;

    private String videoIndicator;

    private String source;

    private String serviceId;

    private String quantity;

    private String custNumber;

    private String description;

    private String callIdentification;

    private String tailNumber;

    private String originalFile;

    private Long lineNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

    public CdrSmsSettle(Long id, String recordType, String numberA, String numberB, String numberDialed, String msisdn, String imsi, String eventTimeStamp, Date localEventTimeStamp, Integer eventDuration, BigDecimal downloadVol, BigDecimal uploadVol, String operatorCode, String preratedAmount, String apn, String nulli, String broadWorks, String teleServiceCode, String bearerServiceCode, String overseasCode, String videoIndicator, String source, String serviceId, String quantity, String custNumber, String description, String callIdentification, String tailNumber, String originalFile, Long lineNumber, Date createTime, Date receiveTime, Date finishTime) {
        this.id = id;
        this.recordType = recordType;
        this.numberA = numberA;
        this.numberB = numberB;
        this.numberDialed = numberDialed;
        this.msisdn = msisdn;
        this.imsi = imsi;
        this.eventTimeStamp = eventTimeStamp;
        this.localEventTimeStamp = localEventTimeStamp;
        this.eventDuration = eventDuration;
        this.downloadVol = downloadVol;
        this.uploadVol = uploadVol;
        this.operatorCode = operatorCode;
        this.preratedAmount = preratedAmount;
        this.apn = apn;
        this.nulli = nulli;
        this.broadWorks = broadWorks;
        this.teleServiceCode = teleServiceCode;
        this.bearerServiceCode = bearerServiceCode;
        this.overseasCode = overseasCode;
        this.videoIndicator = videoIndicator;
        this.source = source;
        this.serviceId = serviceId;
        this.quantity = quantity;
        this.custNumber = custNumber;
        this.description = description;
        this.callIdentification = callIdentification;
        this.tailNumber = tailNumber;
        this.originalFile = originalFile;
        this.lineNumber = lineNumber;
        this.createTime = createTime;
        this.receiveTime = receiveTime;
        this.finishTime = finishTime;
    }

    public CdrSmsSettle() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType == null ? null : recordType.trim();
    }

    public String getNumberA() {
        return numberA;
    }

    public void setNumberA(String numberA) {
        this.numberA = numberA == null ? null : numberA.trim();
    }

    public String getNumberB() {
        return numberB;
    }

    public void setNumberB(String numberB) {
        this.numberB = numberB == null ? null : numberB.trim();
    }

    public String getNumberDialed() {
        return numberDialed;
    }

    public void setNumberDialed(String numberDialed) {
        this.numberDialed = numberDialed == null ? null : numberDialed.trim();
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn == null ? null : msisdn.trim();
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi == null ? null : imsi.trim();
    }

    public String getEventTimeStamp() {
        return eventTimeStamp;
    }

    public void setEventTimeStamp(String eventTimeStamp) {
        this.eventTimeStamp = eventTimeStamp == null ? null : eventTimeStamp.trim();
    }

    public Date getLocalEventTimeStamp() {
        return localEventTimeStamp;
    }

    public void setLocalEventTimeStamp(Date localEventTimeStamp) {
        this.localEventTimeStamp = localEventTimeStamp;
    }

    public Integer getEventDuration() {
        return eventDuration;
    }

    public void setEventDuration(Integer eventDuration) {
        this.eventDuration = eventDuration;
    }

    public BigDecimal getDownloadVol() {
        return downloadVol;
    }

    public void setDownloadVol(BigDecimal downloadVol) {
        this.downloadVol = downloadVol;
    }

    public BigDecimal getUploadVol() {
        return uploadVol;
    }

    public void setUploadVol(BigDecimal uploadVol) {
        this.uploadVol = uploadVol;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode == null ? null : operatorCode.trim();
    }

    public String getPreratedAmount() {
        return preratedAmount;
    }

    public void setPreratedAmount(String preratedAmount) {
        this.preratedAmount = preratedAmount == null ? null : preratedAmount.trim();
    }

    public String getApn() {
        return apn;
    }

    public void setApn(String apn) {
        this.apn = apn == null ? null : apn.trim();
    }

    public String getNulli() {
        return nulli;
    }

    public void setNulli(String nulli) {
        this.nulli = nulli == null ? null : nulli.trim();
    }

    public String getBroadWorks() {
        return broadWorks;
    }

    public void setBroadWorks(String broadWorks) {
        this.broadWorks = broadWorks == null ? null : broadWorks.trim();
    }

    public String getTeleServiceCode() {
        return teleServiceCode;
    }

    public void setTeleServiceCode(String teleServiceCode) {
        this.teleServiceCode = teleServiceCode == null ? null : teleServiceCode.trim();
    }

    public String getBearerServiceCode() {
        return bearerServiceCode;
    }

    public void setBearerServiceCode(String bearerServiceCode) {
        this.bearerServiceCode = bearerServiceCode == null ? null : bearerServiceCode.trim();
    }

    public String getOverseasCode() {
        return overseasCode;
    }

    public void setOverseasCode(String overseasCode) {
        this.overseasCode = overseasCode == null ? null : overseasCode.trim();
    }

    public String getVideoIndicator() {
        return videoIndicator;
    }

    public void setVideoIndicator(String videoIndicator) {
        this.videoIndicator = videoIndicator == null ? null : videoIndicator.trim();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId == null ? null : serviceId.trim();
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity == null ? null : quantity.trim();
    }

    public String getCustNumber() {
        return custNumber;
    }

    public void setCustNumber(String custNumber) {
        this.custNumber = custNumber == null ? null : custNumber.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getCallIdentification() {
        return callIdentification;
    }

    public void setCallIdentification(String callIdentification) {
        this.callIdentification = callIdentification == null ? null : callIdentification.trim();
    }

    public String getTailNumber() {
        return tailNumber;
    }

    public void setTailNumber(String tailNumber) {
        this.tailNumber = tailNumber == null ? null : tailNumber.trim();
    }

    public String getOriginalFile() {
        return originalFile;
    }

    public void setOriginalFile(String originalFile) {
        this.originalFile = originalFile == null ? null : originalFile.trim();
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }
}