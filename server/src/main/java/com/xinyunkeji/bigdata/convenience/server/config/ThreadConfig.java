package com.xinyunkeji.bigdata.convenience.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置类
 *
 * @author Yuezejian
 * @date 2020年 08月22日 22:09:26
 */
@Configuration
public class ThreadConfig {

    @Bean("threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor(){
        //ThreadPoolTaskExecutor 底层直接使用了一个BlockingQueue，
        // 初始容量为2147483647（0x7fffffff，2的31次方-1），即无界队列
        //线程池维护线程所允许的空闲时间，默认为60s， keepAliveSeconds = 60
        //其内部使用队列：BlockingQueue<Runnable> queue = this.createQueue(this.queueCapacity);
        //createQueue()方法底层使用了new LinkedBlockingQueue(内部基于链表来存放元素)
        //本汪说下：
        //LinkedBlockingQueue内部分别使用了takeLock 和 putLock 对并发进行控制，也就是说，
        //添加和删除操作并不是互斥操作，可以同时进行，这样也就可以大大提高吞吐量
        ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
        /*executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setKeepAliveSeconds(10);
        executor.setQueueCapacity(8);*/

        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setKeepAliveSeconds(10);
        executor.setQueueCapacity(4);

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }
}