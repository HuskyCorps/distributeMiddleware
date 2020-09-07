package com.xinyunkeji.bigdata.convenience.server.service.mail;

import com.rabbitmq.client.Channel;

import com.xinyunkeji.bigdata.convenience.model.entity.MsgLog;
import com.xinyunkeji.bigdata.convenience.model.mapper.MsgLogMapper;
import com.xinyunkeji.bigdata.convenience.server.controller.AbstractController;
import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import com.xinyunkeji.bigdata.convenience.server.request.MailRequest;
import com.xinyunkeji.bigdata.convenience.server.service.log.MsgLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 邮件监听器-消费者
 *
 * @author Yuezejian
 * @date 2020年 08月24日 20:23:50
 */
@Component
public class MailMqListener extends AbstractController {

    @Autowired
    private MailService mailService;

    @Autowired
    private MsgLogService msgLogService;

    @Autowired
    private MsgLogMapper msgLogMapper;

    //监听邮件发送的消息
    @RabbitListener(queues = {"${mq.email.queue}"},containerFactory = "multiListenerContainer")
    public void consume(Message message, MailRequest request, Channel channel) throws IOException {
        String msgId = "";
            msgId    = request.getCorrelationData();
        MsgLog msgLog = msgLogMapper.selectByPrimaryKey(msgId);
        //正常流程(msgLog !=null && !msgLog.getStatus().equals(Constant.CONSUME_SUCCESS))
        // 满足条件的进行发送,其他均排除
        // 消费幂等性
        if (null == msgLog || msgLog.getStatus().equals(Constant.CONSUME_SUCCESS)) {
            log.info("重复消费, msgId: {}", msgId);
            return;
        }

        MessageProperties properties = message.getMessageProperties();
        long tag = properties.getDeliveryTag();

        boolean success = mailService.sendSimpleEmail(request.getSubject(), request.getContent(),
                StringUtils.split(request.getUserMails(), ","));//用逗号隔开实现邮件的群发
        if (success) {
            MsgLog entity = new MsgLog(msgId, Constant.CONSUME_SUCCESS);
            log.info("更新-邮件发送消息日志-————————————————————消费成功，设状态值为“3” msgId={}",  msgId);
            try {
                msgLogService.updateLog(entity);
            } catch (Exception e) {
                log.info("更新-邮件发送消息日志-消费成功———————发生异常 Exception={},msgId={}", e, msgId);
                return;
            }
            //channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false);
            //deliveryTag:该消息的index
            //multiple：是否批量 true:将一次性ack所有小于deliveryTag的消息。
            channel.basicAck(tag, false);// 消费确认，只确认当前一条
        } else {
            MsgLog entity = new MsgLog(msgId, Constant.CONSUME_FALSE);
            log.info("更新-邮件发送消息日志——————————————————————消费失败，设状态值为“2” msgId={}",  msgId);
            try {
                msgLogService.updateLog(entity);
            } catch (Exception e) {
                log.info("更新-邮件发送消息日志-消费失败———————发生异常 Exception={},msgId={}", e, msgId);
                return;
            }
            //channel.basicNack(tag, false, true)
            //deliveryTag:该消息的index
            //multiple：是否批量 true:将一次性拒绝所有小于deliveryTag的消息。
            //requeue：被拒绝的是否重新入队列
            channel.basicNack(tag, false, false);
        }
    }
}