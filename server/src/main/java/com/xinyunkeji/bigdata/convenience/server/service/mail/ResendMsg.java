package com.xinyunkeji.bigdata.convenience.server.service.mail;

import com.google.gson.Gson;
import com.xinyunkeji.bigdata.convenience.model.entity.MsgLog;
import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import com.xinyunkeji.bigdata.convenience.server.request.MailRequest;
import com.xinyunkeji.bigdata.convenience.server.service.log.MsgLogService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RabbitMQ邮件定时重发
 *
 * @author Yuezejian
 * @date 2020年 08月29日 22:03:11
 */
@Component
public class ResendMsg {
    public static final Logger log = LoggerFactory.getLogger(ResendMsg.class);

    @Autowired
    private MsgLogService msgLogService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @SneakyThrows
    @Scheduled(cron = "0 0/2 * * * ?")
//    @Scheduled(cron = "0 0 0/1 * * ?")
//    @Scheduled(cron = "0 0/30 * * * ?")
    public void resend() {
        try {
            log.info("开始执行定时任务(重新投递邮件)");
            List<MsgLog> msgLogs = null;
            //获取需要重新投递的(状态为投递中/投递失败,或为投递成功，但限定时间内仍未被正常消费的)
            String status = Constant.DELIVER_LOADING + "," + Constant.DELIVER_FALSE + "," + Constant.DELIVER_SUCCESS;
            msgLogs = msgLogService.selectTimeoutMsg(status);
            if (msgLogs != null && !msgLogs.isEmpty()) {
                msgLogs.forEach(msgLog -> {
                    if (StringUtils.isNotBlank(msgLog.getMsg()) &&
                            StringUtils.isNotBlank(msgLog.getExchange()) &&
                            StringUtils.isNotBlank(msgLog.getRoutingKey())) {
                        //反序列化，日志记录中的msg是进过序列化的MailRequest对象
                        MailRequest request = new Gson().fromJson(msgLog.getMsg(),MailRequest.class);
                        //重新投递
                        send(msgLog, request, rabbitTemplate);
                        log.info("重新投递--成功--消息id={}", msgLog.getMsgId());
                    }
                });
            }
        } catch (Exception e) {
            log.error("定时任务重新拉取投递失败的消息~重新进行投递~发生异常：",e.fillInStackTrace());
        }


    }

    public static void send(MsgLog msgLog, MailRequest request, RabbitTemplate rabbitTemplate) {
        rabbitTemplate.setExchange(msgLog.getExchange());
        rabbitTemplate.setRoutingKey(msgLog.getRoutingKey());
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.convertAndSend(request, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置消息持久化和消息头的类型
                MessageProperties properties = message.getMessageProperties();
                properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);//设置持久化
                properties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, MailRequest.class);
                return message;
            }
        });
    }

}