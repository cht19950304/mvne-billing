package com.cmit.mvne.billing.rating.gprs.common;

/**
 * @Author: caikunchi
 * @Description: 信管同步流程中的错误码及错误描述
 * @Date: Create in 2019/12/11 15:08
 */
public class RatingErrorConstant {

    public static final String MYSQL_INSERT_FAILED_CODE = "E2002";

    public static final String REDIS_LPUSH_FAILED_CODE = "E2003";

    public static final String ACQUIRE_LOCK_FAILED_CODE = "E2004";
    public static final String ACQUIRE_LOCK_FAILED_DESC =
            "Can't acquire lock over three times. Please check redis!";

    public static final String SYSTEM_ERROR_CODE = "E2005";

    public static final String UNEXPECTED_ERROR_CODE = "E2999";

    public static final String NO_USER_INFO_CODE = "E3001";
    public static final String NO_USER_INFO_DESC = "can't find user detail by msisdn and eventTimestamp";

    public static final String NO_RATING_RULES_CODE = "E3002";
    public static final String NO_RATING_RULES_DESC = "Can't find rating rules!";

    public static final String CANNOT_SUM_FREERES_CODE = "E3003";
    public static final String CANNOT_SUM_FREERES_DESC = "Can't sum freeres by product ins info.";

    public static final String CANNOT_SUM_BALANCE_CODE = "E3004";
    public static final String CANNOT_SUM_BALANCE_DESC = "Can't sum balance fee!";

    public static final String CANNOT_FIND_BALANCE_CODE = "E3005";
    public static final String CANNOT_FIND_BALANCE_DESC = "Can't find balance fee!";

    public static final String CANNOT_FIND_FREERES_CODE = "E3006";
    public static final String CANNOT_FIND_FREERES_DESC = "Can't find freeres by product ins info.";

    public static final String CANNOT_FIND_PM_PRODUCT_CODE = "E3007";
    public static final String CANNOT_FIND_PM_PRODUCT_DESC = "Can't find pmProduct by user's ordered product.";

    public static final String CANNOT_FIND_COUNTRY_CODE = "E3007";
    public static final String CANNOT_FIND_COUNTRY_DESC = "Can't find country by cdr opertor code.";

    public static final String DUP_CDR_CODE = "E4001";
    public static final String DUP_CDR_DESC = "Dup cdr! Check the id from current table!";

    public static final String OVER_RETRY_SUM_TIME_CODE = "E5001";
    public static final String OVER_RETRY_SUM_TIME_DESC = "Get optimistic lock failed!";
}
