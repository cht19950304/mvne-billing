package com.cmit.mvne.billing.preparation.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class CdrErrorHistory {
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    private String originFile;

    private Long lineNumber;

    private String errorCode;

    private String status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;

    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date redoTime;

    private String cdrDetail;

    public CdrErrorHistory(Long id, String originFile, Long lineNumber, String errorCode, String status, Date receiveTime, Date createTime, Date redoTime) {
        this.id = id;
        this.originFile = originFile;
        this.lineNumber = lineNumber;
        this.errorCode = errorCode;
        this.status = status;
        this.receiveTime = receiveTime;
        this.createTime = createTime;
        this.redoTime = redoTime;
    }

    public CdrErrorHistory(Long id, String originFile, Long lineNumber, String errorCode, String status, Date receiveTime, Date createTime, Date redoTime, String cdrDetail) {
        this.id = id;
        this.originFile = originFile;
        this.lineNumber = lineNumber;
        this.errorCode = errorCode;
        this.status = status;
        this.receiveTime = receiveTime;
        this.createTime = createTime;
        this.redoTime = redoTime;
        this.cdrDetail = cdrDetail;
    }

    public CdrErrorHistory() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginFile() {
        return originFile;
    }

    public void setOriginFile(String originFile) {
        this.originFile = originFile == null ? null : originFile.trim();
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode == null ? null : errorCode.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getRedoTime() {
        return redoTime;
    }

    public void setRedoTime(Date redoTime) {
        this.redoTime = redoTime;
    }

    public String getCdrDetail() {
        return cdrDetail;
    }

    public void setCdrDetail(String cdrDetail) {
        this.cdrDetail = cdrDetail == null ? null : cdrDetail.trim();
    }
}