package com.xinyunkeji.bigdata.convenience.server.config;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 自定义注入Redisson操作bean组件
 *
 * @author Yuezejian
 * @date 2020年 09月01日 20:10:14
 */
@Configuration
public class RedissionConfig {

    @Autowired
    private Environment env;

    //单节点
//    @Bean
//    public RedissonClient client() {
//        Config config = new Config();
//        config.useSingleServer().setAddress(env.getProperty("redisson.url.single"));
//        RedissonClient client = Redisson.create(config);
//        return client;
//    }

    //集群
    @Bean
    public RedissonClient client() {
        Config config = new Config();
        config.useClusterServers().setScanInterval(2000)
                .addNodeAddress(StringUtils.split(env.getProperty("redisson.url.cluster"),","))
                .setMasterConnectionMinimumIdleSize(10)
                .setMasterConnectionPoolSize(64)
                .setSlaveConnectionMinimumIdleSize(10)
                .setSlaveConnectionPoolSize(64)
                .setConnectTimeout(15000);
        RedissonClient client = Redisson.create(config);
        return client;
    }
}