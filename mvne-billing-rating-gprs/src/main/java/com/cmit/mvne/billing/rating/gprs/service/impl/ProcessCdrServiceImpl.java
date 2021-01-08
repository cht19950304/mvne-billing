package com.cmit.mvne.billing.rating.gprs.service.impl;

import com.cmit.mvne.billing.rating.gprs.common.DistributeLock;
import com.cmit.mvne.billing.rating.gprs.service.ProcessCdrService;
import com.cmit.mvne.billing.user.analysis.common.MvneException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Queue;

/**
 * @Author: caikunchi
 * @Description:
 * @Date: Create in 2020/1/2 16:37
 */

@Service
@Async("asyncServiceExecutor")
public class ProcessCdrServiceImpl implements ProcessCdrService {
    @Value(value = "${yellow-mobile.redis.redisson.wait-time}")
    int waitTime;
    @Value(value = "${yellow-mobile.redis.redisson.lease-time}")
    int leaseTime;

    @Autowired
    private DistributeLock distributeLock;

    private final static Object MUTEX1 = new Object();
    private final static Object MUTEX2 = new Object();

    // 分线程的方法
    @Override
    public void dealCdr(String cdr, Queue<String> waitQueue) throws MvneException, InterruptedException {
        String lockKey = "RatingKey" + cdr;
        /*boolean acquiredLock = distributeLock.fairLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);

        if (acquiredLock) {
            System.out.println(cdr);
            synchronized (this) {
                TestController.callBack();
                TestController.pri();
                distributeLock.unlock(lockKey);
            }


            Thread.sleep(1000);

        } else {
            synchronized (this)
            {
                waitQueue.offer(cdr);
            }
        }*/
    }

}
