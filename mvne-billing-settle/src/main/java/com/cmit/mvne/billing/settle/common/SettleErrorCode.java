package com.cmit.mvne.billing.settle.common;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/6/8
 */
public enum SettleErrorCode {

    USER_NOT_EXIST("USER_NOT_EXIST", "Can not find User Details!");

    SettleErrorCode(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }
    public String getErrorMessage() {
        return errorMessage;
    }

    private final String errorCode;
    private final String errorMessage;

}
