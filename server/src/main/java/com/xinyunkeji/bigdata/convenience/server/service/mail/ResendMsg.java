package com.xinyunkeji.bigdata.convenience.server.service.mail;

import com.xinyunkeji.bigdata.convenience.model.entity.MsgLog;
import com.xinyunkeji.bigdata.convenience.server.service.log.MsgLogService;
import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
@Log4j
public class ResendMsg {
    @Autowired
    private MsgLogService msgLogService;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    private static final int MAX_TRY_COUNT = 3;


    @Scheduled(cron = "0/30 * * * * ?")
    public void resend() {
//        log.info("开始执行定时任务(重新投递消息)");
//
//        List<MsgLog> msgLogs = msgLogService.selectTimeoutMsg();
//        msgLogs.forEach(msgLog -> {
//            String msgId = msgLog.getMsgId();
//            if (msgLog.getTryCount() >= MAX_TRY_COUNT) {
//                msgLogService.updateStatus(msgId, Constant.MsgLogStatus.DELIVER_FAIL);
//                log.info("超过最大重试次数, 消息投递失败, msgId: {}", msgId);
//            } else {
//                msgLogService.updateTryCount(msgId, msgLog.getNextTryTime());
//
//                CorrelationData correlationData = new CorrelationData(msgId);
//                rabbitTemplate.convertAndSend(msgLog.getExchange(), msgLog.getRoutingKey(), MessageHelper.objToMsg(msgLog.getMsg()), correlationData);
//
//                log.info("第 " + (msgLog.getTryCount() + 1) + " 次重新投递消息");
//            }
//        });
//
//        log.info("定时任务执行结束(重新投递消息)");
    }

}