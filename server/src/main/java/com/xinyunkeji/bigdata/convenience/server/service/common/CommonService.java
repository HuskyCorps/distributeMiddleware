package com.xinyunkeji.bigdata.convenience.server.service.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinyunkeji.bigdata.convenience.model.entity.SysLog;
import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * 通用service
 *
 * @author Yuezejian
 * @date 2020年 08月31日 22:18:04
 */
@Service
public class CommonService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment env;

    @Autowired
    private ObjectMapper objectMapper;


    //统一用户记录日志
    public void recordLog(final Object obj,final String operation,final String method) throws Exception{
        SysLog log=new SysLog(Constant.logOperateUser,operation,method,objectMapper.writeValueAsString(obj));

        rabbitTemplate.setExchange(env.getProperty("mq.log.exchange"));
        rabbitTemplate.setRoutingKey(env.getProperty("mq.log.routing.key"));

        Message msg= MessageBuilder.withBody(objectMapper.writeValueAsBytes(log))
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                .build();
        rabbitTemplate.send(msg);


    }
}