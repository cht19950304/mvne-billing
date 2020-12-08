package com.cmit.mvne.billing.user.analysis.entity;

import java.math.BigDecimal;

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
@TableName("rating_cdr_gprs_rerat_rec")
public class RatingCdrGprsReratRec {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    private String numberB;

    private String apn;

    private String bearerServiceCode;

    private String broadWorks;

    private String callIdentification;

    private Date createTime;

    private String custNumber;

    private String description;

    private BigDecimal downloadVol;

    private Integer eventDuration;

    private String eventTimeStamp;

    private Date finishTime;

    private String imsi;

    private Date localEventTimeStamp;

    private String msisdn;

    private String nulli;

    private String numberA;

    private String numberDialed;

    private String operatorCode;

    private String overseasCode;

    private String preratedAmount;

    private String quantity;

    private String recordType;

    private String serviceId;

    private String source;

    private String teleServiceCode;

    private BigDecimal uploadVol;

    private String videoIndicator;

    private String originalFile;

    private Long userId;

    private Long productId;

    /**
     * 产品实例id
     */
    private Long productInsId;

    /**
     * 批价科目id
     */
    private Long itemId;

    private BigDecimal deductFreeres;

    private BigDecimal ratingValue;

    private BigDecimal fee1;

    private BigDecimal fee2;

    private BigDecimal fee3;

    private BigDecimal fee4;

    private Date receiveTime;

    /**
     * the time of rerat rating
     */
    @TableField(value = "RERAT_TIME", fill = FieldFill.INSERT)
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date reratTime;

    /**
     * 重处理标志
     */
    private String redoFlag;

    /**
     * 重批标志
     */
    private String reratFlag;

    private String tailNumber;

    private Long lineNumber;


}
