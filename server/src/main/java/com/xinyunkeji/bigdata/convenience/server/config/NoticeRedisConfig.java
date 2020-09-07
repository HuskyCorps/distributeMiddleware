package com.xinyunkeji.bigdata.convenience.server.config;

import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import com.xinyunkeji.bigdata.convenience.server.service.notice.NoticeRedisListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;


/**
 * 用户消息-订阅发布
 * 指定容器工厂和有谁去进行监听
 * @author Yuezejian
 * @date 2020年 08月25日 21:08:36.
 */
@Configuration
public class NoticeRedisConfig {
    //redis消息监听容器-发布和订阅时都是通过这个容器处理的
    //将new PatternTopic(Constant.RedisTopicNameEmail)放入listenerAdapter中
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory factory, MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        //消费者配置监听器中注入生产者使用的序列化工具。
//        listenerAdapter.setSerializer(new GenericJackson2JsonRedisSerializer());
//        listenerAdapter.setSerializer(new JdkSerializationRedisSerializer());
        //添加一个到多个otpic(频道)
        container.addMessageListener(listenerAdapter,new PatternTopic(Constant.RedisTopicNameEmail));
        return container;
    }

    @Bean
    public RedisConnectionFactory factory() {
        return new JedisConnectionFactory();
    }

    @Autowired
    private NoticeRedisListener noticeRedisListener;
    /**
     * 绑定消息-消息监听器-监听接收消息的方法
     * @return
     */
    @Bean
    public MessageListenerAdapter listenerAdapter() {
        return  new MessageListenerAdapter(noticeRedisListener,"listenMsg");
    }
}