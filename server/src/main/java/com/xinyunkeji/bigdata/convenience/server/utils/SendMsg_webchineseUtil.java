package com.xinyunkeji.bigdata.convenience.server.utils;


import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;


/**
 * 短信验证码发送工具
 *
 * @author Yuezejian
 * @date 2020年 08月31日 21:19:03
 */
public class SendMsg_webchineseUtil {
    public static void main(String[] args)throws Exception
    {

        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("http://gbk.api.smschinese.cn");
        post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");//在头文件中设置转码
        NameValuePair[] data ={ new NameValuePair("Uid", "HuskyYue"),new NameValuePair("Key", "d41d8cd98f00b204e980"),new NameValuePair("smsMob","16602653790"),new NameValuePair("smsText","请您准时参加面试，验证码：8888")};
        post.setRequestBody(data);

        client.executeMethod(post);
        Header[] headers = post.getResponseHeaders();
        int statusCode = post.getStatusCode();
        System.out.println("statusCode:"+statusCode);
        for(Header h : headers)
        {
            System.out.println(h.toString());
        }
        String result = new String(post.getResponseBodyAsString().getBytes("gbk"));
        System.out.println(result); //打印返回消息状态
        post.releaseConnection();

    }
}