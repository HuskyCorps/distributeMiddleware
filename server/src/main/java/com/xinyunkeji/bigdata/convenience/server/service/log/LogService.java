package com.xinyunkeji.bigdata.convenience.server.service.log;

import com.google.gson.Gson;
import com.xinyunkeji.bigdata.convenience.model.entity.SysLog;
import com.xinyunkeji.bigdata.convenience.model.mapper.SysLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 日志监听service
 *
 * @author Yuezejian
 * @date 2020年 08月22日 21:06:22
 */
@Service
public class LogService {
    private static final Logger logger = LoggerFactory.getLogger(MsgLogService.class);
    @Autowired
    private SysLogMapper logMapper;

    //记录日志
    public void recordLog(SysLog log) throws Exception {
        logger.info("正在-新增-系统操作日志 SysLog={}",new Gson().toJson(log));
        logMapper.insertSelective(log);
        logger.info("新增-系统操作日志----成功 ");

    }
}