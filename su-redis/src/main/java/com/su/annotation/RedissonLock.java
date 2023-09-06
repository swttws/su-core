package com.su.annotation;

import com.su.enums.LockType;

import java.lang.annotation.*;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author suweitao
 * 加锁注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RedissonLock {

    /**
     * 方法名称
     */
    String methodName() default "";

    /**
     * 唯一标识
     */
    String flag() default "";

    /**
     * 锁类型
     */
    LockType lockType() default LockType.WRITE;

    /**
     * 加锁时间,默认30秒
     */
    long lockTime() default 30;

    /**
     * 超时时间,默认60秒
     */
    long waitTime() default 60;

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
