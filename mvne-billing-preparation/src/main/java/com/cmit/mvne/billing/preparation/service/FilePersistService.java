package com.cmit.mvne.billing.preparation.service;

import com.cmit.mvne.billing.preparation.entity.CdrFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/5/13
 */
public interface FilePersistService {
    @Transactional(rollbackFor = Exception.class)
    void save(CdrFile cdrFile) throws IOException;
}
