package com.xinyunkeji.bigdata.convenience.server.controller;

import com.xinyunkeji.bigdata.convenience.api.response.BaseResponse;
import com.xinyunkeji.bigdata.convenience.api.response.StatusCode;
import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 统一响应模型controller
 *
 * @author Yuezejian
 * @date 2020年 08月22日 15:06:33
 */
@RestController
@RequestMapping("base")
public class BaseController {
    private static final Logger log = LoggerFactory.getLogger(BaseController.class);
    @RequestMapping("info")
    public BaseResponse info(String name) {
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            //TODO 核心业务逻辑
            if (StringUtil.isBlank(name)){
                name = "泰达智慧城市";
            }
            response.setData(name);
        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }
}
