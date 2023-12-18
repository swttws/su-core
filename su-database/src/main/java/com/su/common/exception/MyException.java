package com.su.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author swt
 * @date 2023/7/16 20:35
 * 全局异常,业务统一抛出该异常
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyException extends Exception{
    private Integer code;

    private String msg;
}
