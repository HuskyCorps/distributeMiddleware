package com.xinyunkeji.bigdata.convenience.server.service.vip;

import com.xinyunkeji.bigdata.convenience.model.entity.UserVip;
import com.xinyunkeji.bigdata.convenience.model.mapper.UserVipMapper;
import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import org.joda.time.DateTime;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * Vip到期提醒Service
 *
 * @author Yuezejian
 * @date 2020年 09月03日 21:31:51
 */
@Service
public class UserVipService {
    private static final Logger log = LoggerFactory.getLogger(UserVipService.class);

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private UserVipMapper userVipMapper;

    //充值会员-redisson的MapCache
    @Transactional(rollbackFor = Exception.class)
    public void addVip(UserVip vip) throws Exception {
        vip.setVipTime(DateTime.now().toDate());
        int res = userVipMapper.insertSelective(vip);
        if (res > 0 ) {
            //假设（vipDay = 20,即会员充值20天）,20天后失效：第一次提醒 ttl = vipDay - x; 第二次提醒 ttl = vipDay
            //1.到期前N天提醒 2.到期后提醒
            RMapCache<String,Integer> rMapCache = redissonClient.getMapCache(Constant.RedissonUserVIPKey);

            //TODO:第一次提醒,x默认值10，提前10天提醒
            String key = vip.getId() + Constant.SplitCharUserVip + Constant.VipExpireFlg.First.getType();//vipId_1 过期前提醒
            Long firstTTL = Long.valueOf(String.valueOf(vip.getVipDay()-Constant.x));
            if (firstTTL > 0) {
                rMapCache.put(key, vip.getId(), firstTTL, TimeUnit.SECONDS);
            }

            //TODO:第二次提醒
            key = vip.getId() + Constant.SplitCharUserVip + Constant.VipExpireFlg.End.getType();//vipId_1 过期后提醒
            Long secondTTL = Long.valueOf(String.valueOf(vip.getVipDay()));
            rMapCache.put(key,vip.getId(),secondTTL, TimeUnit.SECONDS);

        }

    }

}