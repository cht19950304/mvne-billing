package com.cmit.mvne.billing.settle.common;

/**
 * 结算接口返回
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/3/24
 */

public class ServerResponse<T> {

    private String code;
    private String message;
    private T data;

    private ServerResponse(String code) {
        this.code = code;
    }

    private ServerResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private ServerResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ServerResponse<T> createSuccess() {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    public static <T> ServerResponse<T> createSuccessWithMessage(String message) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), message);
    }
    public static <T> ServerResponse<T> createErrorWithMessage(String message) {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), message);
    }

    public static <T> ServerResponse<T> createSuccess(T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
    }

    public static <T> ServerResponse<T> createError() {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
