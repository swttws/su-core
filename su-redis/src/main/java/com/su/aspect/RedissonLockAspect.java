package com.su.aspect;

import com.su.annotation.RedissonLock;
import com.su.aspect.el.ExpressionEvaluator;
import com.su.client.IRedissonLockClient;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @author suweitao
 * redisson分布式锁，切面类
 */
@Aspect
@Component
public class RedissonLockAspect {

    /**
     * redis命名空间
     */
    @Value("${su.redis.namesapce}")
    private String redisNameSpace;

    @Autowired
    private IRedissonLockClient redissonLockClient;

    private static final ExpressionEvaluator<String> EVALUATOR = new ExpressionEvaluator<>();

    /**
     * redisson锁切面类
     *
     * @param joinPoint    拦截方法信息
     * @param redissonLock 注解
     * @return
     */
    @Around("@annotation(redissonLock)")
    public Object redisLock(ProceedingJoinPoint joinPoint, RedissonLock redissonLock) throws Throwable {
        //锁名不能为空
        if (!StringUtils.hasText(redissonLock.methodName()) || !StringUtils.hasText(redissonLock.flag())) {
            throw new RuntimeException("redisson锁的名称不能为空");
        }
        //解析el表达式，获取flag唯一标识
        String flag = elParam(joinPoint, redissonLock.flag());
        //拼接redisKey
        String redisKey = redisNameSpace + redissonLock.methodName() + flag;
        //获取redisson分布式锁
        return redissonLockClient.methodLock(redisKey, redissonLock.lockType(),
                redissonLock.waitTime(), redissonLock.lockTime(), redissonLock.timeUnit(),
                joinPoint::proceed);
    }

    /**
     * EL表达式解析,返回唯一标识
     *
     * @param point     ProceedingJoinPoint
     * @param lockParam lockParam
     * @return 表达式解析结果
     */
    private String elParam(JoinPoint point, String lockParam) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        Object[] args = point.getArgs();
        Object target = point.getTarget();
        EvaluationContext context = EVALUATOR.createEvaluationContext(target, target.getClass(), method, args);
        AnnotatedElementKey elementKey = new AnnotatedElementKey(method, target.getClass());
        return EVALUATOR.condition(lockParam, elementKey, context, String.class);
    }

}
