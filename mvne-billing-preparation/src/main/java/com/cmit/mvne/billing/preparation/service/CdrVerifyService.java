package com.cmit.mvne.billing.preparation.service;

import java.util.Date;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/13
 */
public interface CdrVerifyService {
    String verify(String[] cdr, Date fileRecieveDate);
}
