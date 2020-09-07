package com.xinyunkeji.bigdata.convenience.server.service.imp.strategy;

import com.alibaba.fastjson.JSONObject;
import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import com.xinyunkeji.bigdata.convenience.server.service.wechat.WXMessagePushService;
import com.xinyunkeji.bigdata.convenience.server.utils.WXHttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
* 微信公众号消息推送（文本）
* @author Yuezj
* @time 2020/07/27
*/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class WXMessagePushText implements WXMessagePushService {

   @Override
   public JSONObject messagePush(String option, String param) {
       WXHttpClientUtils.getAccess_token(Constant.corpSecret);
       //获取token
       String token = WXHttpClientUtils.ACCESS_TOKEN;

       /*
       文本消息url
        */
       StringBuilder url = new StringBuilder();
       url.append("message/send?access_token=")
               .append(token);
       String url1 = url.toString();

       /*
       调用消息体封装方法，进行param解析和消息体的封装（目前仅支持text,image,file）
        */
       WXHttpClientUtils wxHttpClientUtils = WXHttpClientUtils.getInstance();
       String json = wxHttpClientUtils.encodeUtil(param);

       /*
       设置实体，使用WXHttpClientUtils工具类调用接口
        */
      return wxHttpClientUtils.util(json,url1);

}
}
