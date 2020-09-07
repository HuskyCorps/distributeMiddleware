package com.xinyunkeji.bigdata.convenience.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.xinyunkeji.bigdata.convenience.api.response.BaseResponse;
import com.xinyunkeji.bigdata.convenience.api.response.StatusCode;
import com.xinyunkeji.bigdata.convenience.model.entity.SysLog;
import com.xinyunkeji.bigdata.convenience.model.entity.User;
import com.xinyunkeji.bigdata.convenience.model.mapper.UserMapper;
import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import com.xinyunkeji.bigdata.convenience.server.service.log.LogAopAnnotation;
import com.xinyunkeji.bigdata.convenience.server.service.log.LogApplicationEvent;
import com.xinyunkeji.bigdata.convenience.server.utils.ValidatorUtil;
import jodd.util.StringUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志记录controller
 *
 * @author Yuezejian
 * @date 2020年 08月22日 16:05:54
 */
@RestController
@RequestMapping("log")
public class LogController extends AbstractController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ApplicationEventPublisher publisher;
    //新增用户-日志记录
    @RequestMapping("user/add")
    public BaseResponse addUser(@RequestBody @Validated User user, BindingResult result) {
        String checkRes = ValidatorUtil.checkResult(result);
        if (StringUtil.isNotBlank(checkRes)) {
            return new BaseResponse(StatusCode.InvalidParams.getCode(),checkRes);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            //TODO:写真正的核心业务逻辑
            int res = userMapper.insertSelective(user);
            if (res>0) {
                //TODO:插入成功后，记录当前用户的操作日志 ->aop,消息队列异步解耦出来
                LogApplicationEvent event = new LogApplicationEvent(this,"yuezejian",
                        "新增用户","addUser",new Gson().toJson(user));
                publisher.publishEvent(event);//抛出事件，以便监听


            }

        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //新增用户-日志记录
    @RequestMapping("user/add/aop")
//    @LogAopAnnotation(operatorName = "新增操作"  ,operatorTable = "user")
    @LogAopAnnotation(value = "新增用户-spring aop")
    public BaseResponse addUserVip(@RequestBody @Validated User user, BindingResult result) {
        String checkRes = ValidatorUtil.checkResult(result);
        if (StringUtil.isNotBlank(checkRes)) {
            return new BaseResponse(StatusCode.InvalidParams.getCode(),checkRes);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            //TODO:写真正的核心业务逻辑
            userMapper.insertSelective(user);

        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //新增用户-日志记录-rabbitmq
    @RequestMapping("user/add/mq")
    public BaseResponse addUserMQ(@RequestBody @Validated User user, BindingResult result) {
        String checkRes = ValidatorUtil.checkResult(result);
        if (StringUtil.isNotBlank(checkRes)) {
            return new BaseResponse(StatusCode.InvalidParams.getCode(),checkRes);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            //TODO:写真正的核心业务逻辑
            int res = userMapper.insertSelective(user);
            if (res > 0 ) {
                SysLog log = new SysLog(Constant.logOperateUser,"新增用户-rabbitmq","addUserMQ",new Gson().toJson(user));
                this.mqSendLog(log);
            }

        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Environment env;

    //将日志记录信息当消息塞到mq server里头去( exchange+routingKey)
    private void mqSendLog(SysLog log) throws Exception{
        rabbitTemplate.setExchange(env.getProperty("mq.log.exchange"));//设置交换机
        rabbitTemplate.setRoutingKey(env.getProperty("mq.log.routing.key"));//设置路由

        // 构建消息
        //objectMapper.writeValueAsBytes()，对‘log’类对象，进行序列化，来把类对象转化为字节数组byte[]
        Message msg = MessageBuilder.withBody(objectMapper.writeValueAsBytes(log))
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .build();
        //需要
        rabbitTemplate.send(msg);

    }

}