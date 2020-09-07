package com.xinyunkeji.bigdata.convenience.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import com.xinyunkeji.bigdata.convenience.server.service.imp.WXMessagePushServiceImp;
import com.xinyunkeji.bigdata.convenience.server.service.log.LogAopAnnotation;
import com.xinyunkeji.bigdata.convenience.server.utils.WXHttpClientUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 企业微信消息推送
 * @author Yuezj
 * @time 2020/07/27
 */
@RestController
@RequestMapping("wechat")
public class WXMessagePushController {

    /**
     *  通过部门id获取部门成员
     * @Pamas departmentId
     * 获取部门成员
     * @author Yuezj
     */
    @RequestMapping("getDepartmentNumber")
    public JSONObject getDepartmentNumber (HttpServletRequest request, HttpServletResponse response, String departmentId) {
        WXHttpClientUtils.getAccess_token(Constant.corpSecret);//方法调用时，重新加载token
        //获取token
        String token = WXHttpClientUtils.ACCESS_TOKEN;
        /*
        拼接url地址
         */
        StringBuilder sb = new StringBuilder();
        sb.append("user/simplelist?access_token=")
                .append(token)
                .append("&department_id=")
                .append(departmentId)
                .append("&fetch_child=1");

        String url1 = sb.toString();
        /*
        拼接内容体
         */
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        String json = stringBuilder.toString();
        WXHttpClientUtils wx = WXHttpClientUtils.getInstance();
        return wx.util(json,url1);

    }

    /**
     * 企业微信消息推送
     * @param   "content":"这是手机端发送内容","msgtype":"text","touser":"@all"
     * @return JSONObject
     * @author Yuezj
     */
    @RequestMapping("messagePush")
    public JSONObject messagePush (HttpServletRequest request, HttpServletResponse response, String param ) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        //将接受到的参数转换为JSon格式
        JSONObject jsonObject = new JSONObject();
        //解决The valid characters are defined in RFC 7230 and RFC 3986 问题
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(param).append("}");
        param = sb.toString();
        if (!StringUtils.isEmpty(param)) {
            JSONObject prase = (JSONObject) JSONObject.parse(param);
            String msgtype = (String) prase.get("msgtype");
            System.out.println("发送类型-------------------------------"+msgtype);
            if(!StringUtils.isEmpty(msgtype)) {
                //调用类型适配器，动态适配发送模块
                WXMessagePushServiceImp imp = new WXMessagePushServiceImp();
               jsonObject = imp.pushOption(msgtype,param);
               return jsonObject;
            }
        } else {
            jsonObject.put("msg","消息发送失败");

        }
        return jsonObject;
    }

}
