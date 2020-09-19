package com.xinyunkeji.bigdata.convenience.server.service.vip;

import com.xinyunkeji.bigdata.convenience.model.entity.UserVip;
import com.xinyunkeji.bigdata.convenience.model.mapper.UserVipMapper;
import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import jodd.datetime.TimeUtil;
import org.joda.time.DateTime;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * 基于Redisson延迟队列DelayQueue,实现会员到期前N天提醒
 *
 * @author Yuezejian
 * @date 2020年 09月09日 20:16:35
 */
@Service
public class VipService {
    private static final Logger log = LoggerFactory.getLogger(VipService.class);

    @Autowired
    private UserVipMapper userVipMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Transactional(rollbackFor = Exception.class)
    public void addVip(UserVip vip) throws Exception {
        vip.setVipTime(DateTime.now().toDate());
        int res = userVipMapper.insertSelective(vip);
        //TODO:充值成功(现实一般是需要走支付的..在这里以成功插入db为准) - 设置两个过期提醒时间，
        //TODO:一个是vipDay后的；一个是在到达vipDay前 x 的时间
        //TODO:如，vipDay=10天，x=2，即代表vip到期 前2天 提醒一次，vip到期时提醒一次，即
        //TODO:第一次提醒的时间点为：ttl=10-2=8，即距离现在开始的8天后进行第一次提醒；
        //TODO:第二次提醒的时间点是：ttl=10，即距离现在开始的10天后进行第二次提醒   -- 以此类推
        //TODO: (时间的话，建议转化为s；当然啦，具体业务具体设定即可)
        //TODO:基于redisson的延迟队列实现，重点就在于 ttl 的计算
        // (为了测试方便,在这里我们以 s 为单位)；
        //TODO:如果是多次提醒的话，需要做标记
        if (res > 0 ) {
            RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque(Constant.RedissonUserVipQueue);
            RDelayedQueue<String> rDelayedQueue = redissonClient.getDelayedQueue(blockingDeque);

            //TODO:第一次提醒
            //这个value,实际是这样的value=vipId+"-"+"1",{value="14-1"}
            // 下面的处理只是为了构架考虑，所以外提了出去。
            //为了方便大家理解，本汪把相关代码都加到了注释里
            // public static final String SplitCharUserVip="-";
            // 用户会员到期前的多次提醒的标识
            //    public enum VipExpireFlg{
            //        First(1),
            //        End(2),
            //        ;
            //
            //        private Integer type;
            //
            //        VipExpireFlg(Integer type) {
            //            this.type = type;
            //        }
            //
            //        public Integer getType() {
            //            return type;
            //        }
            //
            //        public void setType(Integer type) {
            //            this.type = type;
            //        }
            //    }
            String value = vip.getId()+Constant.SplitCharUserVip+Constant.VipExpireFlg.First.getType();
            Long firstTTL = Long.valueOf(String.valueOf(vip.getVipDay()-Constant.x));
            if ( firstTTL > 0 ) {
                //在firstTTL秒内，把value对象移动到目标队列中去
                rDelayedQueue.offer(value,firstTTL,TimeUnit.SECONDS);
            }
            //TODO：第二次提醒
            //这个value,实际是value=vipId+"-"+"2",{value="14-2"}
            value = vip.getId()+Constant.SplitCharUserVip+Constant.VipExpireFlg.End.getType();
            Long secondTTL = Long.valueOf(vip.getVipDay());
            //在secondTTL秒内，把value对象移动到目标队列中去
            rDelayedQueue.offer(value,secondTTL,TimeUnit.SECONDS);
            //本汪带大家看一下官方的实例
            //Java的基于Redis的DelayedQueue对象允许将每个元素以指定的延迟传输到目标队列。
            // 对于将消息传递给消费者的指数退避策略可能很有用。目标队列可以是任何队列实现的RQueue接口。
            //RBlockingQueue<String> distinationQueue = ...
            //RDelayedQueue<String> delayedQueue = getDelayedQueue(distinationQueue);
            //--》move object to distinationQueue in 10 seconds
            //delayedQueue.offer("msg1", 10, TimeUnit.SECONDS);
            //--》move object to distinationQueue in 1 minutes
            //delayedQueue.offer("msg2", 1, TimeUnit.MINUTES);
            //
            //
            //--》 msg1 will appear in 15 seconds
            //distinationQueue.poll(15, TimeUnit.SECONDS);
            //
            //--》msg2 will appear in 2 seconds
            //distinationQueue.poll(2, TimeUnit.SECONDS);



        }

    }
}