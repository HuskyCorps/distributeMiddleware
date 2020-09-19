package com.xinyunkeji.bigdata.convenience.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis自定义注入-redis模板
 *
 * @author Yuezejian
 * @date 2020年 08月25日 20:47:30
 */
@Configuration
public class RedisConfig {
    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Bean
    public RedisTemplate redisTemplate() {
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        //设置key/value的序列化策略
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //也可以使用new JdkSerializationRedisSerializer(),反序列化时注意匹配
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        //开启事务的支持
        redisTemplate.afterPropertiesSet();
        return redisTemplate;

    }

    // class StringRedisTemplate extends RedisTemplate<String, String>
    //是子类
    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(connectionFactory);
        //开启事务支持
        stringRedisTemplate.setEnableTransactionSupport(true);
        return stringRedisTemplate;
    }
}