package com.xinyunkeji.bigdata.convenience.server.service.notice;

import com.google.gson.Gson;
import com.xinyunkeji.bigdata.convenience.model.entity.Notice;
import com.xinyunkeji.bigdata.convenience.server.service.mail.MailService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商户公告监听器
 * @author Yuezejian
 * @date 2020年 08月25日 21:23:28
 */
@Service("noticeRedisListener")
public class NoticeRedisListener {
    private static final Logger log = LoggerFactory.getLogger(NoticeRedisListener.class);

    @Autowired
    private MailService mailService;

    //订阅，监听 并处理channel中的消息
    public void listenMsg(String message) {
        try {
            log.info("订阅，监听 并处理redis channel中的消息：message={}",message);
            //监听到的message,为JSON格式字符串，可以对其进行反序列化
            if (StringUtils.isNotBlank(message)  && message.contains("{") ) {
                Notice notice = new Gson().fromJson(message,Notice.class);
                mailService.sendSimpleEmail(notice.getTitle(),notice.getContent(),notice.getUserMails());
            }
        } catch (Exception e) {
            log.error("订阅，监听 并处理redis channel中的消息-发生异常：message={}",message,e);
        }
    }
}