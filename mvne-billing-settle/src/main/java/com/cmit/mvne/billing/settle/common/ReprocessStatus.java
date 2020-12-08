package com.cmit.mvne.billing.settle.common;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/28
 */
public interface ReprocessStatus {

    String INITIAL = "pending";
    String SUCCESS = "successfully";
    String FAIL = "failed";
}
