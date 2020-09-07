package com.xinyunkeji.bigdata.convenience.server.controller;

import com.google.gson.Gson;
import com.xinyunkeji.bigdata.convenience.api.response.BaseResponse;
import com.xinyunkeji.bigdata.convenience.api.response.StatusCode;
import com.xinyunkeji.bigdata.convenience.model.entity.MsgLog;
import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import com.xinyunkeji.bigdata.convenience.server.request.MailRequest;
import com.xinyunkeji.bigdata.convenience.server.service.log.LogAopAnnotation;
import com.xinyunkeji.bigdata.convenience.server.service.log.MsgLogService;
import com.xinyunkeji.bigdata.convenience.server.utils.ValidatorUtil;
import jodd.util.StringUtil;
import org.joda.time.DateTime;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
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

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment env;

    @Autowired
    private MsgLogService msgLogService;



    @RequestMapping("send/mq")
    @LogAopAnnotation("发送邮件")
    public BaseResponse stringData(@RequestBody @Validated MailRequest request, BindingResult result) {
        String checkRes = ValidatorUtil.checkResult(result);
        if (StringUtil.isNotBlank(checkRes)) {
            return new BaseResponse(StatusCode.InvalidParams.getCode(),checkRes);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            //先做消息发送状态为：Constant.DELIVER_LOADING，即发送中的消息发送日志记录
            String msgId = UUID.randomUUID()+"";
            request.setCorrelationData(msgId);
            MsgLog msgLog = new MsgLog(msgId,new Gson().toJson(request),env.getProperty("mq.email.exchange"),
                    env.getProperty("mq.email.routing.key"), Constant.DELIVER_LOADING, DateTime.now().toDate(), DateTime.now().toDate());
            msgLogService.recordLog(msgLog);
            //TODO:将邮件信息充当消息塞入MQ  server里去，-生产者
            rabbitTemplate.setExchange(env.getProperty("mq.email.exchange"));
            rabbitTemplate.setRoutingKey(env.getProperty("mq.email.routing.key"));
            //TODO:将类的对象塞入MQ,设置消息头，监听时用类的方法去接收
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            rabbitTemplate.convertAndSend(request, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    //设置消息持久化和消息头的类型
                    MessageProperties properties = message.getMessageProperties();
                    properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);//设置持久化
                    properties.setHeader(AbstractJavaTypeMapper.DEFAULT_CLASSID_FIELD_NAME,MailRequest.class);
                    return message;
                }
            },new CorrelationData(msgId));
        } catch (Exception e) {
            log.error("异常信息",e);
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;

    }
}