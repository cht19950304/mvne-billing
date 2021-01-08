package com.cmit.mvne.billing.rating.gprs.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/5/21 15:48
 */
@Component
public class RedoLogWriter {
    private static RedisTemplate<String, Object> staticRedisTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void init() {
        staticRedisTemplate = redisTemplate;
    }

    public static void writeRedoLog(String fileName, Long field, String value) {
        String fields = Long.toString(field);
        String redoKey = "RatingRedo:" + fileName;
        staticRedisTemplate.opsForHash().put(redoKey, fields, value);
    }
}
