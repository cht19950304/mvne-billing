package com.cmit.mvne.billing.rating.gprs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableConfigurationProperties
@EnableFeignClients
@EnableDiscoveryClient
@EnableCaching
@SpringBootApplication
@ComponentScan({"com.cmit.mvne.billing.user.analysis.*", "com.cmit.mvne.billing.rating.gprs.*"})
@MapperScan({"com.cmit.mvne.billing.user.analysis.mapper", "com.cmit.mvne.billing.rating.gprs.mapper"})
public class BillingRatingGprsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillingRatingGprsApplication.class, args);
	}
}
