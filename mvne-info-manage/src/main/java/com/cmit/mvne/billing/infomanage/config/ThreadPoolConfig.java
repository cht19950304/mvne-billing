package com.cmit.mvne.billing.infomanage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: caikunchi
 * @Description: 线程池配置
 * @Date: Create in 2019/12/4 11:36
 */

@Configuration
@EnableAsync
public class ThreadPoolConfig {
    @Value(value = "${yellow-mobile.thread.pool.corePoolSize}")
    int corePoolSize;

    @Value(value = "${yellow-mobile.thread.pool.maxPoolSize}")
    int maxPoolSize;

    @Value(value = "${yellow-mobile.thread.pool.QueueCapacity}")
    int QueueCapacity;

    @Value(value = "${yellow-mobile.thread.pool.keepAliveSeconds}")
    int keepAliveSeconds;

    @Value(value = "${yellow-mobile.thread.pool.namePrefix}")
    String namePrefix;

    @Bean
    public Executor asyncServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置核心线程数
        executor.setCorePoolSize(corePoolSize);
        // 设置最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        // 设置队列容量
        executor.setQueueCapacity(QueueCapacity);
        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 设置默认线程名称
        executor.setThreadNamePrefix(namePrefix);
        // 设置拒绝策略，这个策略不想放弃执行任务。但是由于池中已经没有任何资源了，那么就直接使用调用该execute的线程本身来执行。
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize();
        return executor;
    }
}
