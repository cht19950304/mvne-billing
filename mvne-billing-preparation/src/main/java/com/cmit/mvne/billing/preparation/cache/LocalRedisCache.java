package com.cmit.mvne.billing.preparation.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheWriter;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/4/17
 */
@Slf4j
public class LocalRedisCache extends RedisCache {
    private RedisCache cache;
    private RedisCacheWriter cacheWriter;
    private String name;


    /**
     * 调用父类的方法
     *
     * @param cache
     */
    public LocalRedisCache(RedisCache cache) {
        super(cache.getName(), cache.getNativeCache(), cache.getCacheConfiguration());
        this.cache = cache;
        this.cacheWriter = cache.getNativeCache();
        this.name = cache.getName();
    }

    @Override
    public void put(Object key, Object value) {

        //插入之前，若是缓存中已经存在值
        byte[] oldValue = cacheWriter.get(name, createAndConvertCacheKey(key));
        //先更新数据库，其次删除缓存
        if (oldValue != null) {
            log.info("Key exists，presumably as @CachePut operation。Perform cache removal to ensure cache consistency！");
            cache.evict(key);
            //可以增加线程，实现延迟双删
            CacheAsyncEvictHelper.CacheEvictThread cacheEvictThread = new CacheAsyncEvictHelper.CacheEvictThread(cache, key);
            CacheAsyncEvictHelper.evict(cacheEvictThread);

            return;
        }

        log.debug("数据不存在，填入缓存");
        //若不存在，则插入缓存
        cache.put(key, value);
    }

    private byte[] createAndConvertCacheKey(Object key) {
        return serializeCacheKey(createCacheKey(key));
    }
}
