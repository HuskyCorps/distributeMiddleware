package com.xinyunkeji.bigdata.convenience.server.service.log;

import com.google.gson.Gson;
import com.xinyunkeji.bigdata.convenience.model.entity.MsgLog;
import com.xinyunkeji.bigdata.convenience.model.mapper.MsgLogMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * rabbitMq消息发送情况日志记录
 *
 * @author Yuezejian
 * @date 2020年 08月27日 14:06:17
 */
@Service
@Transactional(rollbackFor = Exception.class)
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
    public int updateLog(MsgLog log){
        return logMapper.updateByPrimaryKeySelective(log);
    }

    /**
     * 更据邮件发送记录中，邮件的状态和重发的次数，筛选需要重发的邮件
     * （状态不为“3L”即发送不成功,重试次数小于3）
     * @param status  邮件发送状态——邮件投递中+邮件投递失败
     * @return
     * @throws Exception
     */
    public List<MsgLog> selectTimeoutMsg(String status) throws Exception {
        logger.info("查询超时消息列表");
        String arr[] = StringUtils.split(status,",");
        Long deliverLoading = Long.valueOf(arr[0]);//发送状态为投递中
        Long deliverFalse = Long.valueOf(arr[1]);//发送状态为投递失败
        Long deliverSuccess = Long.valueOf(arr[2]);//发送状态为投递成功，
        // 但数据以及损坏或丢失，而无法被正常消费，比如RabbitMQ崩溃，本汪怎么说，应该懂得啊，不可预料因素
        return logMapper.selectTimeoutMsg(deliverLoading, deliverFalse,deliverSuccess);
    }

    /**
     *
     * @param msgId
     * @param flag
     * @throws Exception
     */
    public void updateStatus(String msgId, long flag) throws Exception {
        logMapper.updateStatus(msgId,flag);
    }

    public void updateTryCount(String msgId,Long tryCount, Date nextTryTime) throws Exception {
        logMapper.updateTryCount(msgId,tryCount,nextTryTime);
    }
}
