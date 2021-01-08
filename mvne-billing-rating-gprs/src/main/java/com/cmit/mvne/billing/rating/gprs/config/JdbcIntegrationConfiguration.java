package com.cmit.mvne.billing.rating.gprs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/2/29
 */
@Configuration
@ImportResource(locations = "classpath:META-INF/spring/integration/jdbcIntegration.xml")
public class JdbcIntegrationConfiguration {
}
