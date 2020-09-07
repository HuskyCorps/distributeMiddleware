package com.xinyunkeji.bigdata.convenience.server.service.log;

import com.xinyunkeji.bigdata.convenience.model.entity.SysLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * spring消息驱动模型：applocationEvent and applocationListener ;消息（监听器）
 *
 * @author Yuezejian
 * @date 2020年 08月22日 20:42:27
 */
@Component
public class LogApplicationListener implements ApplicationListener<LogApplicationEvent> {//ApplicationListener<LogApplicationEvent>完成监听器与消息事件的绑定

    @Autowired
    LogService logService;

    private static final Logger log = LoggerFactory.getLogger(ApplicationListener.class);

    //监听并处理消息
    @Override
    @Async("threadPoolTaskExecutor")//此处在高并发下有BUG，可能导致OOM
    // 利用线程异步处理，提高响应速度，配置于ThreadConfig文件内
    public void onApplicationEvent(LogApplicationEvent event) {
        log.info("spring的消息驱动模型，监听并处理消息：{}",event);
        try {
            //TODO:写真正的业务逻辑
            if ( event != null ) {
                SysLog sysLog = new SysLog(event.getUsername(),event.getOperation(),event.getMethod());
                logService.recordLog(sysLog);
            }

        } catch (Exception e) {
            log.info("spring的消息驱动模型，监听并处理消息：发生异常{}",e);
        }
    }
}