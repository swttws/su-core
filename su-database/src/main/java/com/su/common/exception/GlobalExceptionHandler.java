package com.su.common.exception;

import com.su.common.response.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author swt
 * @date 2023/7/16 20:37
 * 全局异常抛出处理器
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("spring.redis.nameSpace")
    private String redisNameSpace;

    /**
     * java中异常抛出处理器
     * @param e java异常
     * @return ResultResponse
     */
    @ExceptionHandler(Exception.class)
    public ResultResponse exceptionHandler(Exception e){
        return ResultResponse.error(e.getMessage());
    }

    /**
     * 业务异常处理器
     * @param myException 业务异常
     * @return ResultResponse
     */
    @ExceptionHandler(MyException.class)
    public ResultResponse myExceptionHandler(MyException myException){
        return ResultResponse.error(myException.getMsg(),myException.getCode());
    }

    /**
     * 业务异常，黑名单处理器
     * @param backException 业务异常，需要黑名单
     * @return ResultResponse
     */
    @ExceptionHandler(BackException.class)
    public ResultResponse backExceptionHandler(BackException backException){
        // TODO 黑名单记录到redis中

        return ResultResponse.error(backException.getMsg(),backException.getCode());
    }


}
