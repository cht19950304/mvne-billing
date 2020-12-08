package com.cmit.mvne.billing.user.analysis.cache;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.util.Map;

/**
 * @Author: caikunchi
 * @Description: 局数据的redis缓存管理器
 * @Date: Create in 2020/5/25 14:50
 */
public class BureauRedisCacheManager extends RedisCacheManager {
    public BureauRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheNames) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheNames);
    }
}
