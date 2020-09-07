package com.xinyunkeji.bigdata.convenience.server.service.identifyingCode;

import com.xinyunkeji.bigdata.convenience.model.entity.SendRecord;
import com.xinyunkeji.bigdata.convenience.model.mapper.SendRecordMapper;
import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import com.xinyunkeji.bigdata.convenience.server.utils.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 短信验证码失效验证-基于Redisson的mapcache   service
 * Redisson组件，缓存映射MapCache 可以很好地解决前面提到过的DB负载过高，
 * 定时任务处理不及时和批次查询的数据量可能过大占用过多的缓存的问题
 *
 * @author Yuezejian
 * @date 2020年 09月02日 20:18:09
 */
@Service
public class MsgCodeService {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private SendRecordMapper recordMapper;

    public String getRandomCode(final String phone) throws Exception {
        RMapCache<String, String> rMapCache = redissonClient.getMapCache(Constant.RedissonMsgCodeKey);

        String code = rMapCache.get(phone);
        if (StringUtils.isNotBlank(code)) {
            return code;
        }
        //TODO：在网络不稳定的情况下，可能手机没能接收到短信验证码，此时需要重新申请，即
        //TODO: 同个手机号多次申请验证码，如果该手机号存在着  30min内有效的验证码，则直接取出发给他即可，而无需重新生成，
        //TODO: 以免造成空间和资源的浪费
        String msgCode = RandomUtil.randomMsgCode(4);
        SendRecord entity = new SendRecord(phone,msgCode);
        entity.setSendTime(DateTime.now().toDate());
        int res = recordMapper.insertSelective(entity);
        if (res > 0) {
            rMapCache.put(phone,msgCode,1, TimeUnit.MINUTES);
        }

        //调用短信供应商提供的发送短信的api - 阿里云sms、网建短信通...
        //这个的实现在SendMsg_webchineseUtil中，这里不加缀余
        return msgCode;
    }

    public boolean validateCode(final String phone,final String code) throws Exception{
        RMapCache<String,String> rMapCache = redissonClient.getMapCache(Constant.RedissonMsgCodeKey);
        String cacheCode = rMapCache.get(phone);
        return StringUtils.isNotBlank(cacheCode) && cacheCode.equals(code);

    }

}