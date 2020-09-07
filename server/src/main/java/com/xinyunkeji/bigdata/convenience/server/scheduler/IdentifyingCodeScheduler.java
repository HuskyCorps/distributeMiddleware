package com.xinyunkeji.bigdata.convenience.server.scheduler;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.Joiner;
import com.xinyunkeji.bigdata.convenience.model.entity.SendRecord;
import com.xinyunkeji.bigdata.convenience.model.mapper.SendRecordMapper;
import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import com.xinyunkeji.bigdata.convenience.server.service.common.CommonService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 短信失效定时器
 *
 * @author Yuezejian
 * @date 2020年 08月31日 22:15:40
 */
public class IdentifyingCodeScheduler {
    private static final Logger log = LoggerFactory.getLogger(IdentifyingCodeScheduler.class);

    @Autowired
    private SendRecordMapper sendRecordMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CommonService commonService;
    //redis Key过期失效-定时任务
    @Async("threadPoolTaskExecutor")
    @Scheduled(cron = "0 0/30 * * * ?")
    public void schedulerCheckCode() {
        try {
            List<SendRecord> list = sendRecordMapper.selectAllActiveCodes();
            Set<Integer> idsSet = new HashSet<>();
            if (list != null && !list.isEmpty()) {
                list.forEach(sendRecord -> {
                            if (StringUtils.isNotBlank(sendRecord.getPhone())) {
                                if (!redisTemplate.hasKey(Constant.RedisMsgCodeKey + sendRecord.getPhone())) {
                                    int res = sendRecordMapper.updateExpireCode(sendRecord.getId());
                                    if (res > 0 ) {
                                        idsSet.add(sendRecord.getId());
                                    }
                                }
                            }

                });
            }
            String idsSetArrs = Joiner.on(",").join(idsSet);
            sendRecordMapper.updateTimeoutCode(idsSetArrs);
            commonService.recordLog(idsSetArrs,"短信验证码redis的Key及时失效-定时任务-mq","schedulerCheckCode");

//            List<SendRecord> list = sendRecordMapper.selectTimeoutCodes();
//            if (list != null && !list.isEmpty()) {
//                Set<Integer> set = list.stream().map(SendRecord::getId).collect(Collectors.toSet());
//                String ids = Joiner.on(",").join(set);
//                sendRecordMapper.updateTimeoutCode(ids);
//                //发送mq消息，记录日志-便于查看
//                commonService.recordLog(ids,"短信验证码及时失效-定时任务-mq","schedulerCheckCode");



        } catch (Exception e) {
            log.info("定时监测并失效已过期验证码——发送异常",e);
        }

    }
}