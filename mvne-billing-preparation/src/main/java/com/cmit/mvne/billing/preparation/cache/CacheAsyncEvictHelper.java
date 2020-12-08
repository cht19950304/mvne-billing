package com.cmit.mvne.billing.preparation.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 实现异步对缓存双删
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/4/20
 */
@Slf4j
@Component
public class CacheAsyncEvictHelper {

    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    public static void evict(Runnable command) {
        scheduledExecutorService.schedule(command, 1, TimeUnit.SECONDS);
    }


    public static class CacheEvictThread implements Runnable  {
        private Object key;
        private RedisCache cache;

        public CacheEvictThread(RedisCache cache, Object key) {
            this.cache = cache;
            this.key = key;
        }

        @Override
        public void run() {
            log.info("remove cache for the second time! key:{}", key);
            cache.evict(key);
        }
    }


}
