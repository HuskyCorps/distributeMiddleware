package com.xinyunkeji.bigdata.convenience.server.service.imp;

import com.alibaba.fastjson.JSONObject;
import com.xinyunkeji.bigdata.convenience.server.service.imp.strategy.WXMessagePushFile;
import com.xinyunkeji.bigdata.convenience.server.service.imp.strategy.WXMessagePushImage;
import com.xinyunkeji.bigdata.convenience.server.service.imp.strategy.WXMessagePushText;
import com.xinyunkeji.bigdata.convenience.server.service.wechat.WXMessagePushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
/**
 * 微信公众号消息推送
 * @author Yuezj
 * @time 2020/07/27
 */
public class WXMessagePushServiceImp {

    /**
       微信公众号消息推送上下文配置
     * @author Yuezj
     * @time 2020/07/27


     */
    public static class Context{
        private String type;
        private WXMessagePushService wxMessagePushService;
        public Context(String type, WXMessagePushService wxMessagePushService) {
            this.type = type;
            this.wxMessagePushService = wxMessagePushService;
        }

        public WXMessagePushService getWxMessagePushService () {
            return wxMessagePushService;
        }

        public boolean option (String type) {
            return this.type.equals(type);
        }
    }

    /**
      预加载微信消息推送的多种方式(拓展时添加预加载和实现类即可)
     * @author Yuezj
     * @time 2020/07/27

     */
    private static List<Context> pushType = new ArrayList<>();
    static {
        pushType.add(new Context("text",new WXMessagePushText()));
        pushType.add(new Context("image",new WXMessagePushImage()));
        pushType.add(new Context("file",new WXMessagePushFile()));
    }

    /**
    根据实际推送类型，匹配相应的推送方式
     * @author Yuezj
     * @time 2020/07/27

     */
    public JSONObject pushOption (String type, String param) {
        WXMessagePushService wxMessagePushService = null;
        for (Context context : pushType) {
            if ( context.option(type)) {
                wxMessagePushService = context.getWxMessagePushService();
                break;
            }
        }
        //适配器异常时推出
        if(StringUtils.isEmpty(wxMessagePushService)){
           JSONObject jsonObject = new JSONObject();
           jsonObject.put("msg","推送方式匹配异常，请检查适配器");
           return jsonObject;
        }
        return wxMessagePushService.messagePush(type,param);
    }


}
