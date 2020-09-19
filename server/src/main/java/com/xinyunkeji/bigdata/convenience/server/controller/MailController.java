package com.xinyunkeji.bigdata.convenience.server.controller;

import cn.hutool.core.lang.Snowflake;
import com.google.gson.Gson;
import com.xinyunkeji.bigdata.convenience.api.response.BaseResponse;
import com.xinyunkeji.bigdata.convenience.api.response.StatusCode;
import com.xinyunkeji.bigdata.convenience.model.entity.MsgLog;
import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import com.xinyunkeji.bigdata.convenience.server.request.MailRequest;
import com.xinyunkeji.bigdata.convenience.server.service.log.LogAopAnnotation;
import com.xinyunkeji.bigdata.convenience.server.service.log.MsgLogService;
import com.xinyunkeji.bigdata.convenience.server.service.mail.ResendMsg;
import com.xinyunkeji.bigdata.convenience.server.utils.ValidatorUtil;
import jodd.util.StringUtil;
import org.joda.time.DateTime;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 邮件发送
 *
 * @author Yuezejian
 * @date 2020年 08月24日 19:36:55
 */
@RestController
@RequestMapping("mail")
public class MailController extends AbstractController {
    //引入RabbitTemplate默认模板，
    //她位于package org.springframework.amqp.rabbit.core;下，
    //提供了一些默认的配置，DEFAULT_REPLY_TIMEOUT = 5000L;
    //String DEFAULT_ENCODING = "UTF-8";等等
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment env;

    @Autowired
    private MsgLogService msgLogService;

    public static final Snowflake snowflake = new Snowflake(3,2);

    @RequestMapping("send/mq")
    @LogAopAnnotation("发送邮件")
    public BaseResponse stringData(@RequestBody @Validated MailRequest request, BindingResult result) {
        String checkRes = ValidatorUtil.checkResult(result);
        if (StringUtil.isNotBlank(checkRes)) {
            return new BaseResponse(StatusCode.InvalidParams.getCode(),checkRes);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            //雪花算法，生成全局唯一主键
            //41bit时间戳 + 10bit工作机器id + 12bit序列号
            String msgId = snowflake.nextIdStr();
            request.setCorrelationData(msgId);
            final String msg = new Gson().toJson(request);
            //先置消息发送状态为：Constant.DELIVER_LOADING，即消息发送中
            //设置重试次数为0L
            MsgLog entity = new MsgLog(msgId,msg,env.getProperty("mq.email.exchange"),
                    env.getProperty("mq.email.routing.key"), Constant.DELIVER_LOADING, DateTime.now().toDate(), DateTime.now().toDate());
            entity.setTryCount(0L);
            //TODO：消息发送前，先将记录存入数据库
            msgLogService.recordLog(entity);
            //TODO:将类的对象塞入MQ,设置消息头，监听时用类的方法去接收
            ResendMsg.send(entity, request, rabbitTemplate);
        } catch (Exception e) {
            log.error("异常信息",e);
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;

    }
}