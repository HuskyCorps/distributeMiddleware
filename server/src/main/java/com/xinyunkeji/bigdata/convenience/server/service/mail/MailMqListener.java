package com.xinyunkeji.bigdata.convenience.server.service.mail;

import com.rabbitmq.client.Channel;
import com.xinyunkeji.bigdata.convenience.model.entity.MsgLog;
import com.xinyunkeji.bigdata.convenience.model.mapper.MsgLogMapper;
import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import com.xinyunkeji.bigdata.convenience.server.request.MailRequest;
import com.xinyunkeji.bigdata.convenience.server.service.log.MsgLogService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
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
public class MailMqListener {
    public static final Logger log = LoggerFactory.getLogger(MailMqListener.class);

    @Autowired
    private MailService mailService;

    @Autowired
    private MsgLogService msgLogService;

    @Autowired
    private MsgLogMapper msgLogMapper;

    //监听邮件发送的消息
    @RabbitListener(queues = {"${mq.email.queue}"},containerFactory = "multiListenerContainer")
    public void consume(Message message, MailRequest entity, Channel channel) throws IOException, InterruptedException {
        MessageProperties properties = message.getMessageProperties();
        Long tag = properties.getDeliveryTag();
        Long tryCount=0L;
        String msgId = "";
//        MailRequest entity = new Gson().fromJson(message.getBody().toString(),MailRequest.class);
        msgId    = entity.getCorrelationData();
        log.info("————————————————————监听到消息，消息内容： message={}",entity);
        MsgLog msgLog = msgLogMapper.selectByPrimaryKey(msgId);
        //消费幂等性
        //这儿本汪说一下，
        //什么是需要用定时器重新投递的？1.没有成功发入队列的 2.成功进入队列（但数据丢失等原因，无法被正常监听消费的） -> 才需要重新投递
        //什么是需要进行发送重试的？已经成功发入了队列，并被消费，但是邮件发送出了异常，没有成功收到的 ->才需要重试发送（每10秒重试一次）
        //状态为投递成功的，我们进行邮件发送
        //监听到的消息，肯定都是投递成功的
        try {
            if (msgLog != null && !Constant.CONSUME_SUCCESS.equals(msgLog.getStatus()) && msgLog.getTryCount() < Constant.MAIL_RESEND_MAX_TIME) {
                tryCount = msgLog.getTryCount();
                //核心业务逻辑【消息处理~发送邮件(同步；异步)】
                boolean res = mailService.sendSimpleEmail(msgId, entity.getSubject(), entity.getContent(),
                        StringUtils.split(entity.getUserMails(), ","));//用逗号隔开实现邮件的群发
                if (res) {
                    //确认消费
                    //channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false);
                    //deliveryTag:该消息的index
                    //multiple：是否批量 true:将一次性ack所有小于deliveryTag的消息。
                    channel.basicAck(tag, false);// 消费确认，只确认当前一条
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //出现异常时可以走重试机制 ~ 重试次数为3次，每次间隔10s
            // (考虑到RabbitMQ消息延时会有3~4second,可以将休眠时间设为6~7second,确保每10second左右发送一次)
            if (tryCount < Constant.MAIL_RESEND_MAX_TIME) {
                Thread.sleep(Constant.NEXT_TRY_TIME_AFTER);
                //channel.basicNack(tag, false, true)
                //deliveryTag:该消息的index
                //multiple：是否批量 true:将一次性拒绝所有小于deliveryTag的消息。
                //requeue：被拒绝的是否重新入队列
                //重入队列的消息，会被再次重试发送，每重试一次，重试次数加“1”
                channel.basicNack(tag,false,true);
                tryCount +=1L;
                msgLogMapper.updateTryCount(msgId,tryCount, DateTime.now().toDate());
                return;
            }
        }
        //当走到这一步时,代表消息已被消费（status=3）或者 重试次数达到上限 等情况-但不管是哪种情况，
        // 都需要将消息从队列中移除，防止下次项目重启时重新监听消费
        channel.basicAck(tag,false);//消费确认，只确认当前一条
        }

}
