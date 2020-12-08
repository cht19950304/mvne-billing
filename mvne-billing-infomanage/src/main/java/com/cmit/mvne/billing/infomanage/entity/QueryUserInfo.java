package com.cmit.mvne.billing.infomanage.entity;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class QueryUserInfo {
    private String msisdn;
    private BigDecimal balanceFee;
    private BigDecimal remainResource;

    public QueryUserInfo(String msisdn,BigDecimal balanceFee,BigDecimal remainResource)
    {
        this.msisdn = msisdn;
        this.balanceFee = balanceFee;
        this.remainResource = remainResource;
    }
    public String toJsonString() {
        return JSON.toJSONString(this);
    }
}
