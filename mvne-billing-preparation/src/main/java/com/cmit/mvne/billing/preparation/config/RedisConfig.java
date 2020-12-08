package com.cmit.mvne.billing.preparation.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Redis配置类
 *
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/1/8
 */
@Configuration
@EnableTransactionManagement
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig {
    /**
     * 哨兵配置
     */
    @Value(value = "${lettuce.sentinel.master}")
    private String sentinelMaster;
    @Value(value = "${lettuce.sentinel.node1}")
    private String node1;
    @Value(value = "${lettuce.sentinel.port1}")
    private Integer port1;
    @Value(value = "${lettuce.sentinel.node2}")
    private String node2;
    @Value(value = "${lettuce.sentinel.port2}")
    private Integer port2;
    @Value(value = "${lettuce.sentinel.node3}")
    private String node3;
    @Value(value = "${lettuce.sentinel.port3}")
    private Integer port3;
    @Value(value = "${spring.redis.password}")
    private String password;

    /**
     * 单机配置
     */
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private Integer redisPort;
    @Value("${spring.redis.password}")
    private String redisPassword;

    @Bean(name = "lettuceConnectionFactory")
    public RedisConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration =
                new RedisStandaloneConfiguration(redisHost, redisPort);
        redisConfiguration.setPassword(RedisPassword.of(redisPassword));

        /*RedisSentinelConfiguration redisConfiguration = new RedisSentinelConfiguration()
                .master(sentinelMaster)
                .sentinel(node1, port1)
                .sentinel(node2, port2)
                .sentinel(node3, port3);
        redisConfiguration.setPassword(RedisPassword.of(password));*/

        return new LettuceConnectionFactory(redisConfiguration);
    }

    @Bean(name="redisTemplateTrans")
    public RedisTemplate<String, Object> redisTemplateTrans(LettuceConnectionFactory lettuceConnectionFactory) {
        // springboot2默认redisTemplate使用lettuce客户端
        // 直接在配置中配置spring.redis.lettuce.pool相关参数即可使用lettuce资源池
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(lettuceConnectionFactory);
        template.setEnableTransactionSupport(true);
        return template;
    }

    /**
     * description 配置事务管理器
     **/
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
}
