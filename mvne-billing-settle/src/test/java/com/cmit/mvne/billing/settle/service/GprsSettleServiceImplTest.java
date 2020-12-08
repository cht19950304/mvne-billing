package com.cmit.mvne.billing.settle.service;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:zengxiangfei@chinamobile.com">zengxf</a>
 * @since 2020/3/19
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class GprsSettleServiceImplTest {
    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void verify() {

        Long size = redisTemplate.opsForList().size("UserDetail:1700000124");
        TestCase.assertEquals(1, size.intValue());

    }
}