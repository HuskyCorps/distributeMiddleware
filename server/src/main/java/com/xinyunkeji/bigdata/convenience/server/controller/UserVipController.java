package com.xinyunkeji.bigdata.convenience.server.controller;

import com.xinyunkeji.bigdata.convenience.api.response.BaseResponse;
import com.xinyunkeji.bigdata.convenience.api.response.StatusCode;
import com.xinyunkeji.bigdata.convenience.model.entity.UserVip;
import com.xinyunkeji.bigdata.convenience.server.service.vip.UserVipService;
import com.xinyunkeji.bigdata.convenience.server.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Vip到期提醒Controller
 *
 * @author Yuezejian
 * @date 2020年 09月03日 21:29:43
 */
@RestController
@RequestMapping("user/vip")
public class UserVipController extends AbstractController {

    @Autowired
    private UserVipService vipService;

    //充值会员
    @RequestMapping(value = "put",method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)////application/json;charset=UTF-8
    public BaseResponse putVip(@RequestBody @Validated UserVip userVip, BindingResult result) {
        String checkRes = ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(checkRes)) {
            return new BaseResponse(StatusCode.InvalidParams.getCode(),checkRes);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            vipService.addVip(userVip);
        } catch (Exception e) {
            log.error("——————————充值会员-发生异常：",e.fillInStackTrace());
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }
}