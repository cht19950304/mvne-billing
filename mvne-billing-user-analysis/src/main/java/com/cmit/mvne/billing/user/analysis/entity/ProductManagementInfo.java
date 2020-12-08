package com.cmit.mvne.billing.user.analysis.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/2/23 11:06
 */
@Data
public class ProductManagementInfo {
    /**
     * 产品名称
     */
    String productName;

    /**
     * 产品价格（默认欧分）
     */
    BigDecimal productFee;

    /**
     *
     */
    String resource;

    /**
     *
     */
    String overFee;

    /**
     *
     */
    String regionCode;

    /**
     *
     */
    int effectiveTime;

    /**
     *
     */
    Long effectiveDate;

    /**
     *
     */
    Long expireDate;
}
