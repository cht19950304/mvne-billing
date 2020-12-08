package com.cmit.mvne.billing.preparation.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * A unique identifier used to determine the network of roaming partner.
 * The full list of codes in use is given in TADIG PRD TD.13
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/13
 */
public class BdOperatorCode {
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    private String countryInitials;

    private String countryName;

    private String countryNameCn;

    private String operatorCode;

    private String operatorName;

    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(update = "now()")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public BdOperatorCode(Long id, String countryInitials, String countryName, String countryNameCn, String operatorCode, String operatorName, Date createTime, Date updateTime) {
        this.id = id;
        this.countryInitials = countryInitials;
        this.countryName = countryName;
        this.countryNameCn = countryNameCn;
        this.operatorCode = operatorCode;
        this.operatorName = operatorName;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public BdOperatorCode() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryInitials() {
        return countryInitials;
    }

    public void setCountryInitials(String countryInitials) {
        this.countryInitials = countryInitials == null ? null : countryInitials.trim();
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName == null ? null : countryName.trim();
    }

    public String getCountryNameCn() {
        return countryNameCn;
    }

    public void setCountryNameCn(String countryNameCn) {
        this.countryNameCn = countryNameCn == null ? null : countryNameCn.trim();
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode == null ? null : operatorCode.trim();
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}