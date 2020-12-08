package com.cmit.mvne.billing.preparation.common;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/3/24
 */
public enum ResponseCode {

    SUCCESS("200", "SUCCESS"),
    ERROR("500", "ERROR");

    private final String code;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
