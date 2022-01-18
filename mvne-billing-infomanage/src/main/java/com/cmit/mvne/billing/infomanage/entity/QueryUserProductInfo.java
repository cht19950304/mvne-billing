package com.cmit.mvne.billing.infomanage.entity;

import lombok.Data;

@Data
public class QueryUserProductInfo {
    private String msisdn;
    private Long balanceFee;
    private ProductMsg productMsg;

    public QueryUserProductInfo() {
    }

    public QueryUserProductInfo(String msisdn, Long balanceFee, ProductMsg productMsg) {
        this.msisdn = msisdn;
        this.balanceFee = balanceFee;
        this.productMsg = productMsg;
    }
}
