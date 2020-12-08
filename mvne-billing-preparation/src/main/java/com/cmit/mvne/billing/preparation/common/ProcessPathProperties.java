package com.cmit.mvne.billing.preparation.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/3/12
 */
@Data
@Component
@ConfigurationProperties(prefix = "process")
public class ProcessPathProperties {

    private String inputPath;
    private String workPath;
    private String backupPath;
    private String successPath;
    private String errorPath;

}
