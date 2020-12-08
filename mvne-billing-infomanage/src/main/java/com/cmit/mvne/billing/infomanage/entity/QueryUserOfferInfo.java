package com.cmit.mvne.billing.infomanage.entity;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class QueryUserOfferInfo {
    private String userId;
    private BigDecimal balanceFee;
    private List<String> productMsg;

    public QueryUserOfferInfo(String userId,BigDecimal balanceFee,List<String> productMsg)
    {
        this.userId = userId;
        this.balanceFee = balanceFee;
        this.productMsg = productMsg;
    }
    public String toJsonString() {
        return JSON.toJSONString(this);
    }
}
