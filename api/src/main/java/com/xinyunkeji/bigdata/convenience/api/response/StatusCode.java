package com.xinyunkeji.bigdata.convenience.api.response;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 状态码
 *
 * @author Yuezejian
 * @date 2020年 08月22日 14:36:55
 */
@Getter
public enum StatusCode {
    Success(200,"success"),
    Fail(500,"false"),
    InvalidParams(300,"非法的参数！"),
    UserNameHasExist(301,"用户名已存在"),
    UserEmailHasExist(302,"用户邮箱已存在"),
    InvalidArticleId(303,"非法文章ID"),
    InvalidCurrentUserId(304,"非法用户ID")
    ;

    private Integer code;
    private String msg;

    StatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}