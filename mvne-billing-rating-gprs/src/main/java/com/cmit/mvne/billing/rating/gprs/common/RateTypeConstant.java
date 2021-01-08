package com.cmit.mvne.billing.rating.gprs.common;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/3/24 13:23
 */
public class RateTypeConstant {
    /**
     * 订购了套餐且适用
     */
    public static final Integer APPLIED_TYPE = 1;
    /**
     * 订购了套餐，适用但是已经用完
     */
    public static final Integer APPLIED_BUT_OVER_TYPE = 2;
    /**
     * 未订购套餐
     */
    public static final Integer NULL_ORDER_TYPE = 3;
    /**
     * 订购了但是不适用
     */
    public static final Integer NOT_APPLIED_TYPE = 5;
}
