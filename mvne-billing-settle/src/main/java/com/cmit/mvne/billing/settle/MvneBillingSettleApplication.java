package com.cmit.mvne.billing.settle;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 结算子系统启动类
 *
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/1/7
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableIntegrationManagement
@EnableFeignClients
@MapperScan({"com.cmit.mvne.billing.settle.dao"})
public class MvneBillingSettleApplication {
    public static void main(String[] args) {
        SpringApplication.run(MvneBillingSettleApplication.class, args);
    }
}
