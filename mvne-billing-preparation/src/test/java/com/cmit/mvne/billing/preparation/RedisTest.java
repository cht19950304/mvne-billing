package com.cmit.mvne.billing.preparation;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/3/19
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RedisTest {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void verify() {

        Long size = redisTemplate.opsForList().size("UserDetail:1700000124");
        TestCase.assertEquals(1, size.intValue());

    }

    @Test
    public void testSet() {

        redisTemplate.opsForSet().add("redoLogCCC", "CCC");

        redisTemplate.delete("redoLogBBB");


    }

    @Test
    public void testValue() {
//        redisTemplate.opsForValue().set("filename:AAA", "AAA", 30, TimeUnit.SECONDS);
//        Object o = redisTemplate.opsForValue().get("filename:AAA");
//        TestCase.assertNotNull(o);

        redisTemplate.opsForValue().set("filestream", 5);
        Long number = redisTemplate.opsForValue().increment("filestream", 1);
        System.out.println(number);
    }
}