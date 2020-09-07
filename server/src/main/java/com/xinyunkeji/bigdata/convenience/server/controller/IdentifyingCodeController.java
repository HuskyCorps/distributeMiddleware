package com.xinyunkeji.bigdata.convenience.server.controller;

import com.xinyunkeji.bigdata.convenience.api.response.BaseResponse;
import com.xinyunkeji.bigdata.convenience.api.response.StatusCode;
import com.xinyunkeji.bigdata.convenience.server.service.identifyingCode.IdentifyingCodeService;
import com.xinyunkeji.bigdata.convenience.server.service.identifyingCode.MsgCodeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短信验证码失效验证
 *
 * @author Yuezejian
 * @date 2020年 08月31日 19:14:59
 */
@RestController
@RequestMapping("msg/code")
public class IdentifyingCodeController extends AbstractController{

    @Autowired
    private  IdentifyingCodeService identifyingCodeService;

    @Autowired
    private MsgCodeService codeService;


    //获取验证码
    @RequestMapping("send")
    public BaseResponse sendCode(@RequestParam String phone) {
        if (StringUtils.isBlank(phone)) {
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
//            response.setData(identifyingCodeService.getRandomCodeV1(phone));
            response.setData(codeService.getRandomCode(phone));

        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //验证验证码
    @RequestMapping("validate")
    public BaseResponse validateCode(@RequestParam String phone, @RequestParam String code ) {
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(code)) {
           return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
//            response.setData(identifyingCodeService.validateCodeV1(phone,code));
              response.setData(codeService.validateCode(phone,code));
        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }



}