package com.cmit.mvne.billing.infomanage.common;

/**
 * @Author: caikunchi
 * @Description: 信管同步流程中的错误码及错误描述
 * @Date: Create in 2019/12/11 15:08
 */
public class InfoManageErrorConstant {
    public static final String ITABLE_NOT_FOUND_CODE = "2001";
    public static final String ITABLE_NOT_FOUND_DESC =
            "Can't not find i_user or i_prod by order_id";

    public static final String MYSQL_INSERT_FAILED_CODE = "2002";

    public static final String REDIS_LPUSH_FAILED_CODE = "2003";

    public static final String ACQUIRE_LOCK_FAILED_CODE = "2004";
    public static final String ACQUIRE_LOCK_FAILED_DESC =
            "Can't acquire lock over three times. Please check redis!";

    public static final String SYSTEM_ERROR_CODE = "2005";

    public static final String SCAN_AND_UPDATE_ERR_CODE = "2006";
    public static final String SCAN_AND_UPDATE_ERR_DESC =
            "Wrong quality of updating scanned order!";

    public static final String MYSQL_UPDATE_FAILED_CODE = "2007";
}
