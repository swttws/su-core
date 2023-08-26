package com.su.client;

import com.su.enums.LockType;
import com.su.supplier.MethodSupplier;
import org.redisson.RedissonLock;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author suweitao
 */
public interface IRedissonLockClient {

    /**
     *
     * @param lockName 锁名
     * @param lockType 锁类型
     * @return
     */
    public RLock getLock(String lockName, LockType lockType);

    /**
     * 获取锁
     * @param lockName 锁名称
     * @param lockType 锁类型
     * @param waitTime 锁超时时间
     * @param lockTime 加锁时间
     * @param timeUnit 时间单位
     * @return
     * @throws InterruptedException
     */
    public boolean tryLock(String lockName, LockType lockType, Long waitTime,
                           Long lockTime, TimeUnit timeUnit) throws InterruptedException;

    /**
     * 解锁
     * @param lockName 锁名称
     * @param lockType 锁类型
     * @return
     */
    public boolean unLock(String lockName,LockType lockType);

    /**
     * 方法加锁，过滤重复请求
     * @param lockName 锁名称
     * @param lockType 锁类型
     * @param waitTime 锁超时时间
     * @param lockTime 加锁时间
     * @param timeUnit 时间单位
     * @param methodSupplier 业务方法
     * @param <T>
     * @return
     * @throws Throwable
     */
    public <T> T methodLock(String lockName, LockType lockType, Long waitTime,
                            Long lockTime, TimeUnit timeUnit, MethodSupplier<T> methodSupplier)
            throws Throwable;
}
