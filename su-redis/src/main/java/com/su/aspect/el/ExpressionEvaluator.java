package com.su.aspect.el;

import org.springframework.aop.support.AopUtils;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * el表达式解析类
 * @author 21781
 * @param <T>
 */
public class ExpressionEvaluator<T> extends CachedExpressionEvaluator {

    private final ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();

    private final Map<ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<>(64);

    private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>(64);

    /**
     * 创建EL解析器上下文
     *
     * @param target      target
     * @param targetClass targetClass
     * @param method      method
     * @param args        args
     * @return EL解析器上下文
     */
    public EvaluationContext createEvaluationContext(Object target, Class<?> targetClass, Method method, Object[] args) {
        Method targetMethod = getTargetMethod(targetClass, method);
        ExpressionRootObject root = new ExpressionRootObject(target, args);
        return new MethodBasedEvaluationContext(root, targetMethod, args, this.paramNameDiscoverer);
    }

    /**
     * 解析EL表达式
     * @param expression EL表达式
     * @param elementKey AnnotatedElementKey
     * @param elContext  EL解析器上下文
     * @param clazz      目标class
     * @return           EL表达式解析结果
     */
    public T condition(String expression, AnnotatedElementKey elementKey, EvaluationContext elContext, Class<T> clazz) {
        return getExpression(this.conditionCache, elementKey, expression).getValue(elContext, clazz);
    }

    /**
     * 获取目标方法
     *
     * @param targetClass targetClass
     * @param method      method
     * @return 目标方法
     */
    private Method getTargetMethod(Class<?> targetClass, Method method) {
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
        Method targetMethod = this.targetMethodCache.get(methodKey);
        if (Objects.isNull(targetMethod)) {
            targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
            this.targetMethodCache.put(methodKey, targetMethod);
        }
        return targetMethod;
    }
}