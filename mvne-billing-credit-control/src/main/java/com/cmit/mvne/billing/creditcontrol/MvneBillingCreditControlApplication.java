package com.cmit.mvne.billing.creditcontrol;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/2/11
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.cmit.mvne.billing.creditcontrol.remote")
@EnableTransactionManagement
@MapperScan({"com.cmit.mvne.billing.creditcontrol.mapper","com.cmit.mvne.billing.creditcontrol.job.mapper"})
public class MvneBillingCreditControlApplication {
	
    public static void main(String[] args) {
        SpringApplication.run(MvneBillingCreditControlApplication.class, args);
    }
}
