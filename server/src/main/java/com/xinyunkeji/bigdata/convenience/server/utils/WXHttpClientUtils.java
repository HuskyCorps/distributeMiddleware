package com.xinyunkeji.bigdata.convenience.server.utils;

import com.alibaba.fastjson.JSONObject;
import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


/**
 * 微信公众号消息推送工具
 * @author Yuezj
 * @time 2020/07/27
 */
public class WXHttpClientUtils {
//    private static String tokenAccess = WXConst.ACCESS_TOKEN;
    private static String str1 = Constant.WXApiUrl;

    /**全局token 所有与微信有交互的前提 */
    public static String ACCESS_TOKEN;

    /**全局token上次获取事件 */
    public static long LASTTOKENTIME;

    /** secret静态，用来保存上次使用时的secret */
    public static String SECRET;

    /**
     * 用于发送消息
     * @param json  消体息的内容
     * @param string 用于拼接的url地址
     * @return
     */
    public JSONObject util(String json, String string) {
        System.out.println("消息的内容体：--------------------"+json);
        JSONObject jsonObject = new JSONObject();
            //请求access_token地址
            StringBuilder sb = new StringBuilder();
            sb.append(str1).append(string);
            String url = sb.toString();
            System.out.println("请求的全地址："+ url);
        try {
            //创建提交方式
            URL postUrl = new URL(url);
            HttpURLConnection http = (HttpURLConnection) postUrl.openConnection();
            http.setRequestMethod("POST");
            http.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            http.setDoOutput(true);
            http.setDoInput(true);
            // 连接超时30秒
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
            // 读取超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000");
            http.connect();

            //写入内容
            OutputStream outputStream = http.getOutputStream();
            outputStream.write(json.getBytes("UTF-8"));
            System.out.println("输出流：——————————————————————————"+json.getBytes("UTF-8"));

            InputStream inputStream = http.getInputStream();
            int size = inputStream.available();
            byte[] jsonBytes = new byte[size];
            inputStream.read(jsonBytes);
            String result = new String(jsonBytes, "UTF-8");
            jsonObject = (JSONObject) JSONObject.parse(result);
            System.out.println("请求返回结果:——————————————————————" + result);
            //清空输出流
            outputStream.flush();
            //关闭输出通道
            outputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }

    /**
     * 获取全局token方法
     * 该方法通过使用HttpClient发送http请求，HttpGet()发送请求
     * 微信返回的json中access_token是我们的全局token
     */
    public static synchronized void getAccess_token(String secret) {

        if ( StringUtils.isEmpty(secret) ) {
            secret = secret.trim();
            if ( StringUtils.isEmpty(secret) ) {
                secret = "defaultSecret";
            }
        }
        if (ACCESS_TOKEN == null || !secret.equals(SECRET) || System.currentTimeMillis() - LASTTOKENTIME > 7000 * 1000) {//access_token will lose efficacy 2 hours later,7200 seconds
                //请求access_token地址
                StringBuilder sb = new StringBuilder();
                sb.append(Constant.WXApiUrl+"gettoken?corpid=").append(Constant.corpId).append("&corpsecret=").append(secret);
                String url = sb.toString();
            try {
                //创建提交方式
                HttpGet httpGet = new HttpGet(url);
                //获取到httpclien
                HttpClient httpClient = HttpClientBuilder.create().build();
                //发送请求并得到响应  
                HttpResponse response = httpClient.execute(httpGet);
                //判断请求是否成功  
                System.out.println("状态码为"+response.getStatusLine().getStatusCode());
                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    System.out.println("加载全局TOKEN方法，已经执行！每隔7000秒执行一次");
                    //将得到的响应转为String类型  
                    String str = EntityUtils.toString(response.getEntity(), "utf-8");
                    System.out.println("响应结果为:"+str);
                    //字符串转json  
                    JSONObject jsonObject = JSONObject.parseObject(str);
                    //给静态变量赋值，获取到access_token  
                    ACCESS_TOKEN = (String) jsonObject.get("access_token");
                    System.out.println("access_toker:"+ACCESS_TOKEN);
                    //给获取access_token时间赋值，方便下此次获取时进行判断  
                    LASTTOKENTIME = System.currentTimeMillis();
                    //获取access_token成功后，为SECRET赋当前secret的值
                    SECRET = secret;
                }
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block  
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block  
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param param 截去"{","}"的json格式字符串 eg:param="content":"这是手机端发送内容","msgtype":"text","touser":"@all"
     * @return String 消息体
     */
    public String encodeUtil(String param) {
        /*
        解析param字符串
         */
        JSONObject prase = (JSONObject) JSONObject.parse(param);
        String touser = (String) prase.get("touser");
        String toparty = (String) prase.get("toparty");
        String totag = (String) prase.get("totag");
        String msgtype = (String) prase.get("msgtype");
        Integer agentid = 1000002;
        String content = (String) prase.get("content");
        String mediaId = (String) prase.get("media_id");
        Integer safe = 0;

        /*
        封装发送消息请求json
         */
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\"touser\":" + "\"" + touser + "\",");
        sb.append("\"toparty\":" + "\"" + toparty + "\",");
        sb.append("\"totag\":" + "\"" + totag + "\",");
        sb.append("\"msgtype\":" + "\"" + msgtype + "\",");
        /*
        文本消息体拼接
         */
        if ( "text".equals(msgtype)) {
            sb.append("\"text\":" + "{");
            sb.append("\"content\":" + "\"" + content + "\"");
        }
        /*
        图片信息体拼接
         */
        if ( "image".equals(msgtype)) {
            sb.append("\"image\":" + "{");
            sb.append("\"media_id\":" + "\"" + mediaId + "\"");
        }
        /*
        文件消息体拼接
         */
        if ( "file".equals(msgtype)) {
            sb.append("\"file\":" + "{");
            sb.append("\"media_id\":" + "\"" + mediaId + "\"");
        }

        sb.append("}");
        sb.append(",\"safe\":" + "\"" + safe + "\",");
        sb.append("\"agentid\":" + "\"" + agentid + "\",");
        sb.append("\"enable_id_trans\":" + "\"" + 0 + "\"");
        sb.append("\"enable_duplicate_check\":" + "\"" + 0 + "\"");
        sb.append("\"duplicate_check_interval\":" + "\"" + 1800 + "\"");
        sb.append("}");
        String json = sb.toString();
        return json;
    }

    private static WXHttpClientUtils wxHttpClientUtils;
    private WXHttpClientUtils() { }
    public static WXHttpClientUtils getInstance() {
        if ( wxHttpClientUtils == null ) {
            synchronized ( WXHttpClientUtils.class ) {
                if ( wxHttpClientUtils == null ) {
                    wxHttpClientUtils = new WXHttpClientUtils();
                }
            }
        }
        return wxHttpClientUtils;
    }


}
