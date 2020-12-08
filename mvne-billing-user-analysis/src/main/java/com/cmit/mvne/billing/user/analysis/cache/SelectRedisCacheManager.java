package com.cmit.mvne.billing.user.analysis.cache;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.util.Map;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/5/19 10:47
 */
public class SelectRedisCacheManager extends RedisCacheManager {
    public SelectRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheNames) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheNames);
    }

}
