package com.xinyunkeji.bigdata.convenience.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 基础响应
 *
 * @author Yuezejian
 * @date 2020年 08月22日 14:58:56
 */
@Data
@Setter
@Getter
@AllArgsConstructor
public class BaseResponse<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    public BaseResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResponse(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
    }

    public BaseResponse(StatusCode statusCode,T data) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
        this.data = data;
    }
}