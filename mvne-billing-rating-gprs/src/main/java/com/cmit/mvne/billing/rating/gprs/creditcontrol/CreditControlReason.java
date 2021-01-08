package com.cmit.mvne.billing.rating.gprs.creditcontrol;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/2/21 9:52
 */
public class CreditControlReason {
    public static final String LOW_BALANCE_REASON = "1";
    public static final String LOW_BALANCE_AND_ZERO_FREERES_REASON = "2";
    public static final String OUT_OF_BALANCE_REASON = "3";

    public static final String LOW_VOICE_FREERES_REASON = "11";
    public static final String LOW_GPRS_FREERES_REASON = "12";
    public static final String LOW_SMS_FREERES_REASON = "13";

    public static final String CHARGE_REASON = "21";

    public static final String ORDER_VOICE_REASON = "31";
    public static final String ORDER_GPRS_REASON = "32";
    public static final String ORDER_SMS_REASON = "33";

}
