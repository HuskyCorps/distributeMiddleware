package com.xinyunkeji.bigdata.convenience.server.service.wechat;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface WXMessagePushService {

   JSONObject messagePush(String option, String param);
}
