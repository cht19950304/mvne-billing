package com.cmit.mvne.billing.user.analysis.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author Luxf
 * @since 2020-06-01
 */
@Data
@TableName("rating_cdr_gprs_error_his")
public class RatingCdrGprsErrorHis {

    /**
     * 唯一主键
     */
    @TableId(value = "table_id")
    private Long tableId;

    private Long id;

    private String msisdn;

    private Date createTime;

    private String operatorCode;

    private String nulli;

    private BigDecimal downloadVol;

    private BigDecimal uploadVol;

    private Date localEventTimeStamp;

    private String originalFile;

    private String eventTimeStamp;

    private String numberB;

    private String apn;

    private String bearerServiceCode;

    private String broadWorks;

    private String callIdentification;

    private String custNumber;

    private String description;

    private Integer eventDuration;

    private Date finishTime;

    private String imsi;

    private String numberA;

    private String numberDialed;

    private String overseasCode;

    private String preratedAmount;

    private String quantity;

    private String recordType;

    private String serviceId;

    private String source;

    private String teleServiceCode;

    private String videoIndicator;

    private Long userId;

    private String errCode;

    private String errDesc;

    private Date receiveTime;

    private String redoFlag;

    private String reratFlag;

    private Long productId;

    private Date redoTime;

    /**
     * 产品实例id
     */
    private Long productInsId;

    /**
     * 批价科目id
     */
    private Long itemId;

    private String tailNumber;

    private Long lineNumber;


}
