package com.xinyunkeji.bigdata.convenience.server.config;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.xinyunkeji.bigdata.convenience.model.entity.MsgLog;
import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import com.xinyunkeji.bigdata.convenience.server.service.log.MsgLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * rabbitmq模板操作bean组件示例的自定义注入配置
 *
 * @author Yuezejian
 * @date 2020年 08月23日 10:11:17
 */
//在消息发送、接受时记录DB日志，定时轮训DB日志，
// 查明哪些发送消息没有成功消费，启动重新发送消息机制,进行消息补偿
@Configuration
public class RabbitmqTemplate {

    private static final Logger log = LoggerFactory.getLogger(RabbitTemplate.class);

    @Autowired
    private MsgLogService msgLogService;

    @Autowired
    private Environment env;

    @Autowired
    private CachingConnectionFactory  connectionFactory;

    //默认配置
    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;

    //rabbitmq自定义注入模板操作组件
    @Bean
    public RabbitTemplate rabbitTemplate() {
        connectionFactory.setPublisherConfirms(env.getProperty("spring.rabbitmq.publisher-confirms",boolean.class));//发送确认
        connectionFactory.setPublisherReturns(env.getProperty("spring.rabbitmq.publisher-returns",boolean.class));//如果消息丢失，需要有所返回
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 触发setReturnCallback回调必须设置mandatory=true,
        // 否则Exchange没有找到Queue就会丢弃掉消息, 而不会触发回调
        rabbitTemplate.setMandatory(true);
        //确认消息发送成功，还是发送丢失
        //ConfirmCallback  只确认消息是否正确到达 Exchange 中
       rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
           /**
            * @param correlationData 消息唯一标识
            * @param ack 确认结果
            * @param cause 失败原因
            */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
               if (ack) {
                   try {
                       log.info("消息发送到Exchange--成功:correlationData={},ask={},cause={}",correlationData,true,cause);
                      //TODO:成功发送到Exchange中后，更新消息发送的状态为投递成功状态
                       String msgId = correlationData.getId();
                       MsgLog msgLog = new MsgLog(msgId, Constant.DELIVER_SUCCESS);
                       msgLogService.updateLog(msgLog);
                   } catch (Exception e) {
                       log.info("消息发送到Exchange--成功,日志记录异常",e.getMessage());
                   }


               } else {
                   log.info("消息发送到Exchange--失败:correlationData={},ask={},cause={}",correlationData,true,cause);

               }


            }
        });
        //ReturnCallback   消息是否从Exchange路由到Queue, 注意: 这是一个失败回调,
        // 只有消息从Exchange路由到Queue失败才会回调这个方法
       rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
           /**
            *
            * @param message 标记消息
            * @param replyCode 响应码
            * @param replyText 响应内容
            * @param exchange 交换机
            * @param rountingKey 路由
            */
           @Override
           public void returnedMessage(Message message, int replyCode,
                                       String replyText, String exchange, String rountingKey) {
                   log.info("消息由Exchange发送到Queue时失败:message={},replyCode={},replyText={},exchange={},rountingKey={}",
                           message,replyCode,replyText,exchange,rountingKey);
           }

       });

        return rabbitTemplate;
    }

    /**
     * 单一实例消费者（一条队列只会有一个消费者，一条消费者线程监听）
     * @return
     */
    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainer(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        //设置缓存连接，AbstractRabbitListenerContainerFactory
        factory.setConnectionFactory(connectionFactory);
        //生产者服务将JSON类型的数据传递到对应的队列， 而消费端处理器中接收到的数据类型也是JSON类型
        //消息转换器使用了RabbitMQ自带的Jackson2JsonMessageConverter转换器
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        //确认消费模式-NONE自动确认 -AUTO根据情况确认 -MANUAL手动确认
        //默认情况下消息消费者是自动 ack （确认）消息的，需要设置为手动确认
        // 原因是：自动确认会在消息发送给消费者后立即确认，这样存在丢失消息的可能。
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //设置并发
        factory.setConcurrentConsumers(1);
        //最大并发
        factory.setMaxConcurrentConsumers(1);
        factory.setPrefetchCount(1);
        factory.setTxSize(1);
        return factory;
    }

    /**
     * 多实例消费者（一条队列有多个消费者实例，多个消费者线程进行监听和出路）
     * @return
     */
    @Bean(name = "multiListenerContainer")
    public SimpleRabbitListenerContainerFactory multiListenerContainer(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factoryConfigurer.configure(factory,connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        //确认消费模式-NONE自动确认 -AUTO根据情况确认 -MANUAL手动确认
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.simple.concurrency",int.class));
        factory.setMaxConcurrentConsumers(env.getProperty("spring.rabbitmq.listener.simple.max-concurrency",int.class));
        factory.setPrefetchCount(env.getProperty("spring.rabbitmq.listener.simple.prefetch",int.class));
        return factory;
    }

//    /**
//     * 确认消息
//     * @param message 消息
//     * @param channel 频道
//     * @param deliveryTag 传送标签唯一标识 ID
//     */
//    @RabbitHandler
//    public void processMessage2(String message, Channel channel, @Header Map<String,Object> map ) {
//        log.info("",message);
//        if (map.get(""))
//        try {
//            //basicAck has two params
//            //deliverTag:它代表了 RabbitMQ 向该 Channel 投递的这条消息的唯一标识 ID，是一个单调递增的正整数，delivery tag 的范围仅限于 Channel
//            //multiple:为了减少网络流量，手动确认可以被批处理，
//            // 当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
//            channel.basicAck(deliveryTag,false);
//        } catch (IOException e) {
//            log.info("",e.getStackTrace());
//        }
//    }

    /**
     * 日志记录————预先创建交换机、路由及其绑定
     * @return
     */
    //预先创建交换机、路由及其绑定
    @Bean
    public TopicExchange logExchange() {
        //durable持久化， autoDelete是否自动删除
        //env.getProperty("mq.log.exchange")其实就是个路径的字符串
        // 在application.properties中进行了配置，方便修改
        return  new TopicExchange(env.getProperty("mq.log.exchange"),true,false);
    }

    @Bean
    public Queue logQueue() {
        return new Queue(env.getProperty("mq.log.queue"),true);
    }

    //进行交换机和路由的绑定
    @Bean
    public Binding logBinding() {
       return BindingBuilder.bind(logQueue())
                .to(logExchange())
                .with(env.getProperty("mq.log.routing.key")
                );

    }

    /**
     * 邮件发送————预先创建交换机、路由及其绑定
     * @return
     */
    @Bean
    public DirectExchange mailExchange() {
        return  new DirectExchange( env.getProperty("mq.email.exchange"),true,false);
    }

    @Bean
    public Queue mailQueue() {
        return new Queue(env.getProperty("mq.email.queue"),true);
    }

    @Bean
    public Binding mailBinding() {
        return BindingBuilder.bind(mailQueue()).to(mailExchange()).with(env.getProperty("mq.email.routing.key"));
    }

}