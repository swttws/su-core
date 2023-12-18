package com.su.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author swt
 * @date 2023/7/16 20:37
 * 黑名单异常，抛出该异常会记录黑名单
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BackException extends Exception{
    private Integer code;

    private String msg;
}
