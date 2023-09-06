package com.su.aspect.el;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 解析表达式需要的表达式根对象
 *
 * @author Ludeching
 */
@Getter
@AllArgsConstructor
public class ExpressionRootObject {

    private final Object object;

    private final Object[] args;
}