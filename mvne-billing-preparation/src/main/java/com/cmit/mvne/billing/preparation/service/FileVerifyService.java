package com.cmit.mvne.billing.preparation.service;

import java.io.File;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/13
 */
public interface FileVerifyService {
    String verifyFile(File workFile);

    void deleteFileDupKey(String fileName);
}
