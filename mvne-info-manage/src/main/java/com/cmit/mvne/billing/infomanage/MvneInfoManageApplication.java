package com.cmit.mvne.billing.infomanage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableRetry
@MapperScan({"com.cmit.mvne.billing.infomanage.mapper","com.cmit.mvne.billing.infomanage.job.mapper","com.cmit.mvne.billing.user.analysis.mapper"})
public class MvneInfoManageApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvneInfoManageApplication.class, args);
	}

}
