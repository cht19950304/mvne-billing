package com.cmit.mvne.billing.preparation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.integration.config.EnableIntegrationManagement;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.util.TimeZone;


@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableIntegrationManagement
@MapperScan({"com.cmit.mvne.billing.preparation.dao"})
public class MvneBillingPreparationApplication {

	@PostConstruct
	public void init() {
//		TimeZone.setDefault(TimeZone.getDefault());

//		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
	}

	public static void main(String[] args) {
		SpringApplication.run(MvneBillingPreparationApplication.class, args);

	}
}
