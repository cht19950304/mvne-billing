package com.cmit.mvne.billing.user.analysis.cache;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/4/16
 */
@Configuration
@EnableCaching
public class CachingConfig {

    /**
     * 自定义KeyGenerator
     * @return
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                //获取代理对象的最终目标对象
                Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
                StringBuilder sb = new StringBuilder();
                sb.append(targetClass.getSimpleName()).append(":");
                sb.append(method.getName()).append(":");
                //调用SimpleKey的逻辑
                Object key = SimpleKeyGenerator.generateKey(params);
                return sb.append(key);
            }
        };
    }

//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//        //设置特有的Redis配置
//        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
//        //定制化的Cache为300s
////        cacheConfigurations.put("as",customRedisCacheConfiguration(Duration.ofSeconds(300)));
////        cacheConfigurations.put("books",customRedisCacheConfiguration(Duration.ofSeconds(300)));
////        cacheConfigurations.put("cs",customRedisCacheConfiguration(Duration.ofSeconds(300)));
//        //默认超时时间60s
//        return RedisCacheManager.builder(connectionFactory).
//                transactionAware().   //Cache的事务支持
//                cacheDefaults(customRedisCacheConfiguration(Duration.ofSeconds(600))).
//                withInitialCacheConfigurations(cacheConfigurations).   //设置个性化的Cache配置
//                build();
//    }

    @Bean
    @Primary
    public LocalRedisCacheManager localRedisCacheManager(RedisConnectionFactory connectionFactory) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        RedisCacheWriter redisCacheWriter = RedisCacheWriter.lockingRedisCacheWriter(connectionFactory);
        LocalRedisCacheManager localRedisCacheManager = new LocalRedisCacheManager(redisCacheWriter,
                customRedisCacheConfiguration(Duration.ofSeconds(600)),
                cacheConfigurations);

        localRedisCacheManager.setTransactionAware(true);
        return localRedisCacheManager;
    }

    @Bean
    public SelectRedisCacheManager selectRedisCacheManager(RedisConnectionFactory connectionFactory) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        RedisCacheWriter redisCacheWriter = RedisCacheWriter.lockingRedisCacheWriter(connectionFactory);
        SelectRedisCacheManager selectRedisCacheManager = new SelectRedisCacheManager(redisCacheWriter,
                customRedisCacheConfiguration(Duration.ofSeconds(6000)),
                cacheConfigurations);

        selectRedisCacheManager.setTransactionAware(true);
        return selectRedisCacheManager;
    }

    @Bean
    public BureauRedisCacheManager bureauRedisCacheManager(RedisConnectionFactory connectionFactory) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        RedisCacheWriter redisCacheWriter = RedisCacheWriter.lockingRedisCacheWriter(connectionFactory);
        BureauRedisCacheManager bureauRedisCacheManager = new BureauRedisCacheManager(redisCacheWriter,
                customRedisCacheConfiguration(),
                cacheConfigurations);

        bureauRedisCacheManager.setTransactionAware(true);
        return bureauRedisCacheManager;
    }

    public RedisCacheConfiguration customRedisCacheConfiguration() {
        //设置序列化格式
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer
                = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(RedisObjectMapper.redisConfigurationObjectMapper());
        return RedisCacheConfiguration.
                defaultCacheConfig().serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer)).
                computePrefixWith(cacheName -> cacheName + ":").   //设置Cache的前缀，默认::
                disableCachingNullValues();   //若返回值为null，则不允许存储到Cache中
    }

    /**
     * 设置RedisConfiguration配置
     *
     * @param ttl
     * @return
     */
    public RedisCacheConfiguration customRedisCacheConfiguration(Duration ttl) {
        //设置序列化格式
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer
                = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(RedisObjectMapper.redisConfigurationObjectMapper());
        return RedisCacheConfiguration.
                defaultCacheConfig().serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer)).
                computePrefixWith(cacheName -> cacheName + ":").   //设置Cache的前缀，默认::
                disableCachingNullValues().   //若返回值为null，则不允许存储到Cache中
                entryTtl(ttl);  //设置缓存缺省超时时间
    }

    /**
     * JSON转换，避免和容器中ObjectMapper冲突
     */
    public static class RedisObjectMapper {
        public static ObjectMapper redisConfigurationObjectMapper() {
            ObjectMapper objectMapper = new ObjectMapper();
            //JDK1.8新版时间格式化Model
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            objectMapper.registerModule(javaTimeModule);
            //Date类型禁止转换为时间戳
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            //序列化时格式化时间戳
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            //字段名字开启驼峰命名法
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            //序列化无public的属性或方法时，不会抛出异常
            objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            //序列化时保存对象类型,以便反序列化时直接得到具体POJO
            objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
            //非空数据才进行格式化
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            //针对BigDecimal，序列化时，不采取科学计数法
            objectMapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
            //反序列化时，POJO中不含有JSON串的属性，不解析该字段，并且不会抛出异常
            objectMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            //反序列化{}时，不抛出异常，而是得到null值
            objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            return objectMapper;
        }
    }

}
