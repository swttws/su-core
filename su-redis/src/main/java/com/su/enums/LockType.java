package com.su.enums;

/**
 * @author suweitao
 * 锁类型枚举类
 */
public enum LockType {

    /**
     * 重入锁
     */
    REENTRANT,

    /**
     * 公平锁: 按照请求获取锁的顺序
     */
    FAIR,

    /**
     * 读锁
     */
    READ,

    /**
     * 写锁
     */
    WRITE

}
