package com.su.annotation;

import java.lang.annotation.*;

/**
 * @author suweitao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RedissonLock {

}
