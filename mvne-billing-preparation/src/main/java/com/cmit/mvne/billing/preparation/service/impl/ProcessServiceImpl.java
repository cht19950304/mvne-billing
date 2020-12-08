package com.cmit.mvne.billing.preparation.service.impl;

import com.cmit.mvne.billing.preparation.entity.CdrFile;
import com.cmit.mvne.billing.preparation.service.FilePersistService;
import com.cmit.mvne.billing.preparation.service.ProcessService;
import com.cmit.mvne.billing.preparation.service.CdrParsingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 采集预处理服务类
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/2/1
 */
@Service
@Slf4j
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    private CdrParsingService cdrParsingService;
    @Autowired
    private FilePersistService filePersistService;

    @Override
    public void process(CdrFile cdrFile) throws IOException {

        log.info("Starting process file {}", cdrFile.getWorkFile().getName());
        // 解析、校验、查重
        cdrParsingService.parsing(cdrFile);
        // 持久化
        filePersistService.save(cdrFile);

        log.info("Processing file {} succeed! the correct cdr number is {}, the error cdr number is {}",
                cdrFile.getOriginFile().getName(),
                cdrFile.getSuccessCdrList().size(),
                cdrFile.getErrorCdrList().size());
    }
}
