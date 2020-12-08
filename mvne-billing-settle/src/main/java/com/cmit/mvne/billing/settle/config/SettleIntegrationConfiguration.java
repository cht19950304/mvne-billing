package com.cmit.mvne.billing.settle.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/2/19
 */
@Configuration
@ImportResource(locations = "classpath:META-INF/spring/integration/SettleIntegration.xml")
public class SettleIntegrationConfiguration {
}
