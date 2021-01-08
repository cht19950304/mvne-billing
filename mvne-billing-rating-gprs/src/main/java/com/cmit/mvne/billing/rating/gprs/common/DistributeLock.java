package com.cmit.mvne.billing.rating.gprs.common;

import com.cmit.mvne.billing.user.analysis.common.MvneException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class DistributeLock {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 拿不到lock就不罢休，不然线程就一直block
     * 没有超时时间,默认30s
     *
     * @param lockKey
     * @return
     */
    public RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    /**
     * 自己设置超时时间
     *
     * @param lockKey 锁的key
     * @param timeout 秒 如果是-1，直到自己解锁，否则不会自动解锁
     * @return
     */
    public RLock lock(String lockKey, int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, TimeUnit.SECONDS);
        return lock;
    }

    /**
     * 自己设置超时时间
     *
     * @param lockKey 锁的key
     * @param unit    锁时间单位
     * @param timeout 超时时间
     */
    public RLock lock(String lockKey, TimeUnit unit, int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }

    /**
     * 尝试加锁，最多等待waitTime，上锁以后leaseTime自动解锁
     *
     * @param lockKey   锁key
     * @param unit      锁时间单位
     * @param waitTime  等到最大时间，强制获取锁
     * @param leaseTime 锁失效时间
     * @return 如果获取成功，则返回true，如果获取失败（即锁已被其他线程获取），则返回false
     */
    public boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**************************公平锁**************************/
    /**
     * 尝试加锁，最多等待waitTime，上锁以后leaseTime自动解锁
     * 公平锁实现了等待队列，多个 redisson 客户端线程同时请求加锁时，优先分给先发出请求的线程，防止饥饿
     *
     * @param lockKey   锁key
     * @param unit      锁时间单位
     * @param waitTime  等到最大时间
     * @param leaseTime 锁失效时间
     * @return 如果获取成功，则返回true，如果获取失败（即锁已被其他线程获取），则返回false
     */
    @Retryable(value = MvneException.class,
            maxAttempts = 1,
            backoff = @Backoff(delay = 1000, multiplier = 1)
    )
    public boolean fairLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) throws com.cmit.mvne.billing.user.analysis.common.MvneException {
        boolean isAquiredLock = false;
        try {
            // RLock只是對象，在redis上鎖是tryLock做的
            RLock fairLock = redissonClient.getFairLock(lockKey);
            isAquiredLock = fairLock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            log.error("LockKey is " + lockKey + "_" + e.getMessage(), e);
        }


        return isAquiredLock;
    }

    /**
     * 释放锁
     *
     * @param lockKey 锁key
     */
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        if (lock.isLocked()) {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }

    /**
     * 释放锁
     */
    public void unlock(RLock lock) {
        lock.unlock();
    }

}
