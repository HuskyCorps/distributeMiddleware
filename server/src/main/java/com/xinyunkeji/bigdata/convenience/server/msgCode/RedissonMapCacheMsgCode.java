package com.xinyunkeji.bigdata.convenience.server.msgCode;

import com.xinyunkeji.bigdata.convenience.model.mapper.SendRecordMapper;
import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import com.xinyunkeji.bigdata.convenience.server.service.common.CommonService;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.api.map.event.EntryEvent;
import org.redisson.api.map.event.EntryExpiredListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

/**
 * 短信验证码失效验证-key(其中的元素 - field)失效监听
 *
 * @author Yuezejian
 * @date 2020年 09月06日 16:36:33
 */
//实现了ApplicationRunner,Ordered后，在应用启动后可以不间断地执行监听，
// Order用来对多个自动监听进行排序，0优先级最高，bean为2147483647，优先级最低
@Service
public class RedissonMapCacheMsgCode implements ApplicationRunner, Ordered {
    private static final Logger log = LoggerFactory.getLogger(RedissonMapCacheMsgCode.class);

    @Autowired
    private RedissonClient redisson;

    @Autowired
    private SendRecordMapper recordMapper;

    @Autowired
    private CommonService commonService;

    //TODO:应用启动及运行期间，可以不间断地执行我们自定义的服务逻辑
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("——————应用启动期间，不间断执行短信验证失效的业务逻辑-order 0 --");
        this.listenExpireCode();
    }

    @Override
    public int getOrder() {
        return 0;
    }

    //监听器--监听mapCache里失效的验证码
    private void listenExpireCode() {
            RMapCache<String, String> rMapCache = redisson.getMapCache(Constant.RedissonMsgCodeKey);
            rMapCache.addListener(new EntryExpiredListener<String,String>() {


                @Override
                public void onExpired(EntryEvent<String, String> entryEvent) {
                    try {
                        String phone = entryEvent.getKey();
                        String msgCode = String.valueOf(entryEvent.getValue());
                        log.info("————当前手机号：{}，对应的验证码：{}", phone, msgCode);

                        if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(msgCode)) {
                            int res = recordMapper.updateExpirePhoneCode(phone, msgCode);
                            if (res > 0) {
                                commonService.recordLog(phone + "--" + msgCode, "监听mapCache里失效的验证码，对数据库进行更新", "listenExpireCode");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }
}