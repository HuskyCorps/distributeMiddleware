package com.xinyunkeji.bigdata.convenience.server.service.vip;

import com.xinyunkeji.bigdata.convenience.model.entity.UserVip;
import com.xinyunkeji.bigdata.convenience.model.mapper.UserVipMapper;
import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import com.xinyunkeji.bigdata.convenience.server.service.mail.MailService;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.api.map.event.EntryEvent;
import org.redisson.api.map.event.EntryExpiredListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * vip过期提醒的监听器
 *
 * @author Yuezejian
 * @date 2020年 09月03日 22:20:50
 */
@Component
public class RedissonMapCacheUserVip implements ApplicationRunner, Ordered {

    private static final Logger log = LoggerFactory.getLogger(RedissonMapCacheUserVip.class);

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private Environment env;

    @Autowired
    private UserVipMapper vipMapper;

    @Autowired
    private MailService mailService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("不间断执行自定义操作——————————————————————————————order1");
        this.listenUserVip();

    }

    @Override
    public int getOrder() {
        return 1;
    }

    //监听会员过期的数据 1.到期前N天提醒 2.到期后的提醒 需要给相应的用户发送通知（邮件）
    private void listenUserVip() {
        RMapCache<String , Integer> rMapCache = redissonClient.getMapCache(Constant.RedissonUserVIPKey);
        rMapCache.addListener(new EntryExpiredListener<String,Integer>() {

            @Override
            public void onExpired(EntryEvent<String, Integer> entryEvent) {
                //key = 充值记录id -类型
                String key = String.valueOf(entryEvent.getKey());
                //value = 充值记录id
                String value = String.valueOf(entryEvent.getValue());
               log.info("————监听用户会员过期信息，监听到数据：key={},value={}",key,value);

               if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
                   String [] arr = StringUtils.split(key,Constant.SplitCharUserVip);
                   Integer id = Integer.valueOf(value);

                   UserVip vip = vipMapper.selectByPrimaryKey(id);
                   if (vip != null && 1==vip.getIsActive() && StringUtils.isNotBlank(vip.getEmail())) {
                       //TODO:区分第几次提醒，发送对应消息
                       Integer type = Integer.valueOf(arr[1]);
                       if (Constant.VipExpireFlg.First.getType().equals(type)) {
                           String content=String.format(env.getProperty("vip.expire.first.content"),vip.getPhone());
                           mailService.sendSimpleEmail(env.getProperty("vip.expire.first.subject"),content,vip.getEmail());
                       } else {
                          int res = vipMapper.updateExpireVip(id);
                           if (res > 0) {
                               String content=String.format(env.getProperty("vip.expire.end.content"),vip.getPhone());
                               mailService.sendSimpleEmail(env.getProperty("vip.expire.end.subject"),content,vip.getEmail());
                           }
                       }
                   }
               }
            }
        });
    }


}