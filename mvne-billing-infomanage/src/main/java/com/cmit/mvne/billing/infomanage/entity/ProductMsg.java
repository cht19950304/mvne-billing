package com.cmit.mvne.billing.infomanage.entity;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductMsg {
    private String productName;
    private BigDecimal total;
    private BigDecimal remain;
    private Long createDate;
    private Long expireDate;
    public ProductMsg(String productName,BigDecimal total,BigDecimal remain,Long createDate,Long expireDate)
    {
        this.productName = productName;
        this.total = total;
        this.remain = remain;
        this.createDate = createDate;
        this.expireDate = expireDate;
    }
    public String toJsonString() {
        return JSON.toJSONString(this);
    }
}
