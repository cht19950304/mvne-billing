package com.cmit.mvne.billing.rating.gprs.common;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/5/18 10:48
 */
@Configuration
public class OptimisticLock {
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }
}
