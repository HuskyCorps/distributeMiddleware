package com.xinyunkeji.bigdata.convenience.server.service.mail;

import com.google.gson.Gson;
import com.xinyunkeji.bigdata.convenience.model.mapper.MsgLogMapper;
import com.xinyunkeji.bigdata.convenience.server.controller.AbstractController;
import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
/**
 * 邮件service
 *
 * @author Yuezejian
 * @date 2020年 08月24日 20:23:04
 */
@Service
public class MailService extends AbstractController {
    @Autowired
    private Environment env;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MsgLogMapper msgLogMapper;

    //TODO:发送简单的邮件消息
    //@Async("threadPoolTaskExecutor")此处切记不可使用AOP注解，方法进入切面后，由于@Around，返回类型boolean会被设为void,会造成异常
    //Caused by: org.springframework.aop.AopInvocationException: Null return value from advice does not match primitive return type
    //仍想使用异步线程处理，可以修改ACK实现方法，将返回类型设为void即可
    //或对mailSender.send做包装
    public Boolean sendSimpleEmail(final String msgId,final String subject,final String content,final String ... tos) throws Exception {
           Boolean res = true;
            SimpleMailMessage message=new SimpleMailMessage();
            message.setSubject(subject);
            message.setText(content);
            message.setTo(tos);
            message.setFrom(env.getProperty("mail.send.from"));
        try {
            mailSender.send(message);
        } catch (MailException e) {
            log.error("邮件发送失败, to={}, title={}, e={}", new Gson().toJson(tos), subject, e);
            res = false;
            throw e;
        } finally {
            this.updateMsgSendStatus(msgId,res);
        }
        return res;
    }

    //TODO:更新消息处理的结果
    private void updateMsgSendStatus(final String msgId,Boolean res){
        if (StringUtils.isNotBlank(msgId)){
            if (res){
                msgLogMapper.updateStatus(msgId, Constant.CONSUME_SUCCESS);
            }else{
                msgLogMapper.updateStatus(msgId, Constant.CONSUME_FALSE);
            }
        }
    }
}