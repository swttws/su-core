package com.su.annotation;

import java.lang.annotation.*;

/**
 * @author suweitao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface LoadFile {

    /**
     * 加载路径
     */
    String filePath() default "";

    /**
     * 资源加载的优先级
     */
    int order() default 1;

}
