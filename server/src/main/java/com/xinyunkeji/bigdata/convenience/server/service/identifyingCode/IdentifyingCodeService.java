package com.xinyunkeji.bigdata.convenience.server.service.identifyingCode;

import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import org.joda.time.DateTime;
import com.xinyunkeji.bigdata.convenience.model.entity.SendRecord;
import com.xinyunkeji.bigdata.convenience.model.mapper.SendRecordMapper;
import com.xinyunkeji.bigdata.convenience.server.utils.RandomUtil;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 短信验证码service
 *
 * @author Yuezejian
 * @date 2020年 08月31日 19:21:52
 */
@Service
public class IdentifyingCodeService {
    private static final Logger log = LoggerFactory.getLogger(IdentifyingCodeService.class);

    @Autowired
    private SendRecordMapper recordMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedissonClient client;


    //获取短信验证码:redis的key过期失效+定时任务调度监测
    public String getRandomCodeV1(final String phone) throws Exception{
        ValueOperations<String,String> phoneOpera = redisTemplate.opsForValue();
        //TODO：在网络不稳定的情况下，可能手机没能接收到短信验证码，此时需要重新申请，即
        //TODO: 同个手机号多次申请验证码，如果该手机号存在着  30min内有效的验证码，则直接取出发给他即可，而无需重新生成，
        //TODO: 以免造成空间和资源的浪费
        final String key = Constant.RedisMsgCodeKey + phone;//key即为API
        if (redisTemplate.hasKey(key)) {
            return phoneOpera.get(key);
        }
        //否则的话，重新生成新的 4位短信验证码
        String msgCode= RandomUtil.randomMsgCode(4);

        SendRecord entity=new SendRecord(phone,msgCode);
        entity.setSendTime(DateTime.now().toDate());
        int res = recordMapper.insertSelective(entity);
        //如果成功插入数据库，再往redis缓存里放（手机号，验证码）设置有效时间位30分钟
        if ( res > 0 ) {
            phoneOpera.set(key,msgCode,30L, TimeUnit.MINUTES);
        }

        //调用短信供应商提供的发送短信的api - 阿里云sms、网建短信通...

        return msgCode;
    }

    //校验短信验证码-有效且在30min内
    public Boolean validateCodeV1(final String phone,final String code) throws Exception{
        ValueOperations<String,String> operations = redisTemplate.opsForValue();
        final String key = Constant.RedisMsgCodeKey + phone;
        String cacheCode = operations.get(key);
        return StringUtils.isNotBlank(cacheCode) && cacheCode.equals(code);
    }



}