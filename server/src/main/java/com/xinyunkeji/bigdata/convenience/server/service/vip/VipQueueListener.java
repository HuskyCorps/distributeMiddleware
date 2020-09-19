package com.xinyunkeji.bigdata.convenience.server.service.vip;

import com.xinyunkeji.bigdata.convenience.model.entity.UserVip;
import com.xinyunkeji.bigdata.convenience.model.mapper.UserVipMapper;
import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import com.xinyunkeji.bigdata.convenience.server.service.mail.MailService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Redisson的延时队列DelayQueue,Vip提前N天提醒——Listener
 *
 * @author Yuezejian
 * @date 2020年 09月09日 21:07:25
 */
@Component
public class VipQueueListener {
    private static final Logger log = LoggerFactory.getLogger(VipQueueListener.class);


    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private UserVipMapper vipMapper;

    @Autowired
    private MailService mailService;

    @Autowired
    private Environment env;//环境变量实例


    //实时监听延时队列中的代理消息
    @SneakyThrows
    @Async("threadPoolTaskExecutor")
    @Scheduled(cron = "0/5 * * * * ?")
    public void listenExpireVip() {
        RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque(Constant.RedissonUserVipQueue);
        if (blockingDeque != null && !blockingDeque.isEmpty()) {
            //本汪说下，此处我们所取到的element，就是我们
            //rDelayedQueue.offer(value,secondTTL,TimeUnit.SECONDS);
            //放进去的value了，他的格式是“14-1”
            String element = blockingDeque.poll();

            if (StringUtils.isNotBlank(element)) {
                log.info("Vip提前N天提醒,Redisson的延时队列DelayQueue监听器——Listener，监听到 element={}",element);
                //这时候，你应该知道为什么把分隔符提出去，就是为了使用的统一
                // public static final String SplitCharUserVip="-";
                String[] arr = StringUtils.split(element,Constant.SplitCharUserVip);
                Integer id = Integer.valueOf( arr[0]);
                Integer type = Integer.valueOf(arr[1]);
                UserVip vip = vipMapper.selectByPrimaryKey(id);
                if (vip != null && 1==vip.getIsActive() && StringUtils.isNotBlank(vip.getEmail())) {
                    //TODO:区分第几次提醒，发送对应消息
                    if (Constant.VipExpireFlg.First.getType().equals(type)) {
                        log.info("Vip提前N天提醒,第一次提醒");
                        String content=String.format(env.getProperty("vip.expire.first.content"),vip.getPhone());
                        mailService.sendSimpleEmail(env.getProperty("vip.expire.first.subject"),content,vip.getEmail());
                    } else {
                        //设置数据库內会员信息失效，就是把isActive由“1”变为“0”
                        int res = vipMapper.updateExpireVip(id);
                        if (res > 0) {
                            log.info("Vip提前N天提醒,第二次提醒");
                            String content=String.format(env.getProperty("vip.expire.end.content"),vip.getPhone());
                            mailService.sendSimpleEmail(env.getProperty("vip.expire.end.subject"),content,vip.getEmail());
                        }
                    }
                }

            }
        }
    }
}