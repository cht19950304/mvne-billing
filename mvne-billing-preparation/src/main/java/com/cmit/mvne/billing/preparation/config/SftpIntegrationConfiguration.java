package com.cmit.mvne.billing.preparation.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/9/8 9:59
 */

@Configuration
@ConditionalOnProperty(prefix = "mvne.collect", name = "enabled", havingValue = "true", matchIfMissing = true)
@ImportResource(locations = "classpath:META-INF/spring/integration/SftpIntegration.xml")
public class SftpIntegrationConfiguration {
}
