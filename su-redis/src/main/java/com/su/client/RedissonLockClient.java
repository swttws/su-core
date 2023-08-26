package com.su.client;

import com.su.enums.LockType;
import com.su.supplier.MethodSupplier;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author suweitao
 * redisson lock客户端
 */
@Component
public class RedissonLockClient implements IRedissonLockClient {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * @param lockName 锁名
     * @param lockType 类型
     * @return
     */
    @Override
    public RLock getLock(String lockName, LockType lockType) {
        switch (lockType) {
            case REENTRANT:
                return redissonClient.getLock(lockName);
            case WRITE:
                return redissonClient.getReadWriteLock(lockName).writeLock();
            case READ:
                return redissonClient.getReadWriteLock(lockName).readLock();
            case FAIR:
                return redissonClient.getFairLock(lockName);
            default:
                throw new RuntimeException("获取锁类型失败");
        }
    }

    /**
     * 获取锁
     *
     * @param lockName 锁名称
     * @param lockType 锁类型
     * @param waitTime 锁超时时间
     * @param lockTime 加锁时间
     * @param timeUnit 时间单位
     * @return
     */
    @Override
    public boolean tryLock(String lockName, LockType lockType, Long waitTime,
                           Long lockTime, TimeUnit timeUnit) throws InterruptedException {
        //获取锁的类型
        RLock rLock = getLock(lockName, lockType);
        return rLock.tryLock(waitTime, lockTime, timeUnit);
    }

    /**
     * 解锁
     *
     * @param lockName 锁名称
     * @param lockType 锁类型
     * @return
     */
    @Override
    public boolean unLock(String lockName, LockType lockType) {
        RLock lock = getLock(lockName, lockType);
        //锁为当前线程持有才可以释放
        if (lock.isLocked() && lock.isHeldByCurrentThread()) {
            lock.unlock();
            return true;
        }
        return false;
    }

    /**
     * 方法加锁，过滤重复请求(同一时刻的请求)
     *
     * @param lockName       锁名称
     * @param lockType       锁类型
     * @param waitTime       锁超时时间
     * @param lockTime       加锁时间
     * @param timeUnit       时间单位
     * @param methodSupplier 业务方法
     * @param <T>
     * @return
     * @throws Throwable
     */
    @Override
    public <T> T methodLock(String lockName, LockType lockType, Long waitTime, Long lockTime, TimeUnit timeUnit, MethodSupplier<T> methodSupplier) throws Throwable {
        //获取锁
        boolean lock = tryLock(lockName, lockType, waitTime, lockTime, timeUnit);
        try {
            if(lock){
                //获取到锁，执行业务方法
                return methodSupplier.get();
            }
        } finally {
            //解锁
            unLock(lockName,lockType);
        }
        return null;
    }
}
