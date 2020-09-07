package com.xinyunkeji.bigdata.convenience.server.service.log;

import com.google.gson.Gson;
import com.xinyunkeji.bigdata.convenience.model.entity.MsgLog;
import com.xinyunkeji.bigdata.convenience.model.mapper.MsgLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * rabbitMq消息发送情况日志记录
 *
 * @author Yuezejian
 * @date 2020年 08月27日 14:06:17
 */
@Service
public class MsgLogService {
    private static final Logger logger = LoggerFactory.getLogger(MsgLogService.class);

    @Autowired
    private MsgLogMapper logMapper;

    //记录日志
//    @Async("threadPoolTaskExecutor")
    public void recordLog(MsgLog log) throws Exception {
        logger.info("正在-新增-消息发送日志 MsgLog={}",new Gson().toJson(log));
        logMapper.insertSelective(log);
        logger.info("新增-消息发送日志----成功 ");
    }

    //更新修改日志
//    @Async("threadPoolTaskExecutor")
    public void updateLog(MsgLog log) throws Exception {
        logger.info("正在-更新-消息发送日志 MsgLog={}",new Gson().toJson(log));
        logMapper.updateByPrimaryKeySelective(log);
        logger.info("更新-消息发送日志----成功 ");
    }
}
