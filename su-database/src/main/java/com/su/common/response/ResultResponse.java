package com.su.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author swt
 * @date 2023/7/16 17:06
 * 统一封装结果返回
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultResponse<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 提示消息
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 成功返回信息
     * @return ResultResponse
     */
    public static ResultResponse success(){
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setCode(200);
        resultResponse.setMsg("请求成功");
        return resultResponse;
    }

    public static ResultResponse success(Object data){
        ResultResponse response = success();
        response.setData(data);
        return response;
    }

    /**
     * 执行出错返回信息
     * @return ResultResponse
     */
    public static ResultResponse error(String msg){
        ResultResponse resultResponse=new ResultResponse();
        resultResponse.setCode(500);
        resultResponse.setMsg(msg);
        return resultResponse;
    }

    public static ResultResponse error(String msg, Integer code){
        ResultResponse resultResponse=new ResultResponse();
        resultResponse.setCode(code);
        resultResponse.setMsg(msg);
        return resultResponse;
    }
}







