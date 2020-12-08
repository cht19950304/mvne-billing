package com.cmit.mvne.billing.user.analysis.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class QueryUserCdrInfoDto {
    private String msisdn;
    private Date localEventTimeStamp;
    private String eventTimeStamp;
    private String operatorCode;
    private String apn;
    private Integer eventDuration;
    private BigDecimal downloadVol;
    private BigDecimal uploadVol;
    private BigDecimal fee1;

    public QueryUserCdrInfoDto(String msisdn,Date localEventTimeStamp,String eventTimeStamp,String operatorCode,String apn,Integer eventDuration,BigDecimal downloadVol,BigDecimal uploadVol,BigDecimal fee1)
    {
        this.msisdn = msisdn;
        this.localEventTimeStamp=localEventTimeStamp;
        this.eventTimeStamp = eventTimeStamp;
        this.operatorCode = operatorCode;
        this.apn = apn;
        this.eventDuration = eventDuration;
        this.downloadVol = downloadVol;
        this.uploadVol = uploadVol;
        this.fee1 = fee1;
    }
}
