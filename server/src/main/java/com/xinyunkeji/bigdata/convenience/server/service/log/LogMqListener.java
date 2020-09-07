package com.xinyunkeji.bigdata.convenience.server.service.log;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinyunkeji.bigdata.convenience.model.entity.SysLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * 队列的监听消费者
 *
 * @author Yuezejian
 * @date 2020年 08月23日 20:02:25
 */
@Component
public class LogMqListener {
    private static final Logger log = LoggerFactory.getLogger(LogMqListener.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LogService logService;

    /**
     * 指定监听的队列（可以是多个）
     * 以及监听消费者处理消息的模式
     * （单一消费者实例-单一线程）
     */
    @RabbitListener(queues = {"${mq.log.queue}"},containerFactory = "singleListenerContainer")
    public void consumeLogMsg(@Payload byte[] msg) {
        try {
            log.info("日志记录的监听-消费者-监听到消息");
            SysLog entity = objectMapper.readValue(msg, SysLog.class);//序列化
            logService.recordLog(entity);

        } catch (Exception e) {
            log.info("日志记录的监听-消费者-发送异常：",e);
        }

    }
}