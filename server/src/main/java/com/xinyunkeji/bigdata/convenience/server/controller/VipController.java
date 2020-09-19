package com.xinyunkeji.bigdata.convenience.server.controller;

import com.xinyunkeji.bigdata.convenience.api.response.BaseResponse;
import com.xinyunkeji.bigdata.convenience.api.response.StatusCode;
import com.xinyunkeji.bigdata.convenience.model.entity.UserVip;
import com.xinyunkeji.bigdata.convenience.server.service.vip.VipService;
import com.xinyunkeji.bigdata.convenience.server.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Redisson延迟队列DelayedQueue，实现会员到期前N天提醒
 *
 * @author Yuezejian
 * @date 2020年 09月09日 19:57:53
 */
@RestController
@RequestMapping("vip")
public class VipController {
    private static final Logger log = LoggerFactory.getLogger(VipController.class);

    @Autowired
    private VipService vipService;

    @RequestMapping(value = "put" ,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)////application/json;charset=UTF-8
    public BaseResponse putVip(@RequestBody @Validated UserVip userVip, BindingResult result) {
        String checkRes = ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(checkRes)) {
            return new BaseResponse(StatusCode.InvalidParams.getCode(),checkRes);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            vipService.addVip(userVip);
            log.info("————成功充值会员  "+userVip.getVipDay()+"  天");

        } catch (Exception e) {
            log.error("——————————充值会员-发生异常：",e.fillInStackTrace());
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }
}