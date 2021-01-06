package com.cmit.mvne.billing.infomanage.entity;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class QueryUserCdrInfo {
    private String msisdn;
    private Long localEventTimestamp;
    private String eventTimestamp;
    private String location;
    private String apn;
    private Integer eventDuration;
    private BigDecimal totalVol;
    private String resource;
    private BigDecimal fee1;

    public QueryUserCdrInfo(String msisdn,Long localEventTimestamp,String eventTimestamp,String location,String apn,Integer eventDuration,BigDecimal totalVol,String resource,BigDecimal fee1)
    {
        this.msisdn = msisdn;
        this.localEventTimestamp = localEventTimestamp;
        this.eventTimestamp = eventTimestamp;
        this.location = location;
        this.apn = apn;
        this.eventDuration = eventDuration;
        this.totalVol = totalVol;
        this.resource = resource;
        this.fee1 = fee1;
    }
    public String toJsonString() {
        return JSON.toJSONString(this);
    }
}
