package com.cmit.mvne.billing.preparation.service;

import com.cmit.mvne.billing.preparation.entity.CdrFile;

import java.io.IOException;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/13
 */
public interface ProcessService {
    void process(CdrFile cdrFile) throws IOException;
}
