package com.su.supplier;

/**
 * @author suweitao
 */
@FunctionalInterface
public interface MethodSupplier<T> {

    /**
     * 方法加锁，回调方法
     * @return
     * @throws Throwable
     */
    T get() throws Throwable;

}
