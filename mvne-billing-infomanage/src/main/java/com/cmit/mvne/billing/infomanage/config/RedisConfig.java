package com.cmit.mvne.billing.infomanage.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @Author: caikunchi
 * @Description: redis配置类
 * @Date: Create in 2019/12/11 11:23
 */

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig {
    @Value(value = "${lettuce.sentinel.master}")
    private String sentinelMaster;
    @Value(value = "${lettuce.sentinel.node1}")
    private String node1;
    @Value(value = "${lettuce.sentinel.port1}")
    private Integer port1;
    @Value(value = "${lettuce.sentinel.node2}")
    private String node2;
    @Value(value = "${lettuce.sentinel.port1}")
    private Integer port2;
    @Value(value = "${lettuce.sentinel.node3}")
    private String node3;
    @Value(value = "${lettuce.sentinel.port1}")
    private Integer port3;
    @Value(value = "${spring.redis.password}")
    private String password;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        // springboot2默认redisTemplate使用lettuce客户端
        // 直接在配置中配置spring.redis.lettuce.pool相关参数即可使用lettuce资源池
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        template.setEnableTransactionSupport(true);
        return template;
    }

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {

        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
                .master(sentinelMaster)
                .sentinel(node1, port1)
                .sentinel(node2, port2)
                .sentinel(node3, port3);
        sentinelConfig.setPassword(RedisPassword.of(password));

        return new LettuceConnectionFactory(sentinelConfig);
    }

    /**
     * 配置LettuceClientConfiguration 包括线程池配置和安全项配置
     * @param genericObjectPoolConfig common-pool2线程池
     * @return lettuceClientConfiguration
     */
    @Bean
    public LettuceClientConfiguration lettuceClientConfiguration(GenericObjectPoolConfig genericObjectPoolConfig) {
        return LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig)
                .build();
    }

    @Bean
    public GenericObjectPoolConfig genericObjectPoolConfig(CommonPool2Properties commonPool2Properties) {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(commonPool2Properties.getMaxIdle());
        poolConfig.setMinIdle(commonPool2Properties.getMinIdle());
        poolConfig.setMaxTotal(commonPool2Properties.getMaxTotal());

        return poolConfig;
    }

    /**
     * description 配置事务管理器
     **/
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
}
