package com.cmit.mvne.billing.infomanage.common;

/**
 * @Author: caikunchi
 * @Description: 信管同步流程中的错误码及错误描述
 * @Date: Create in 2019/12/11 15:08
 */
public class InfoManageErrorConstant {
    public static final String MYSQL_INSERT_TIMEOUT_CODE = "I1001";
    public static final String MYSQL_UPDATE_TIMEOUT_CODE = "I1002";
    public static final String MYSQL_INSERT_FAILED_CODE = "I1003";
    public static final String MYSQL_UPDATE_FAILED_CODE = "I1004";
    public static final String MYSQL_SELECT_FAILED_CODE = "I1005";
    public static final String OVER_RETRY_UPDATE_TIME_CODE = "I1006";
    public static final String OVER_RETRY_UPDATE_TIME_DESC = "Get optimistic lock failed!";
    public static final String SENT_SMS_CREDIT_FAILED_CODE = "I1007";
    public static final String MYSQL_OTHER_FAILED_CODE = "500";

    public static final String ITABLE_NOT_FOUND_CODE = "I2001";
    public static final String ITABLE_NOT_FOUND_DESC =
            "Can't not find i_user or i_prod by order_id";

    public static final String REDIS_LPUSH_FAILED_CODE = "I2003";

    public static final String ACQUIRE_LOCK_FAILED_CODE = "I2004";
    public static final String ACQUIRE_LOCK_FAILED_DESC =
            "Can't acquire lock over three times. Please check redis!";

    public static final String SYSTEM_ERROR_CODE = "I2005";

    public static final String SCAN_AND_UPDATE_ERR_CODE = "I2006";
    public static final String SCAN_AND_UPDATE_ERR_DESC =
            "Wrong quality of updating scanned order!";




}
