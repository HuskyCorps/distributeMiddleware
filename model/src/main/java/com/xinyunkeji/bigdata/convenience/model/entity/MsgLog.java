package com.xinyunkeji.bigdata.convenience.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息投递日志记录
 *
 * @author Yuezejian
 * @date 2020年 08月25日 17:00:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgLog implements Serializable {
    private static final long SerialVersionUID = 1L;

    private String msgId;//消息唯一标识
    private String msg;//消息体，json格式化
    private String exchange;//交换机
    private String routingKey;//路由键
    private Long status;//发送状态: 0投递中 1投递成功 2投递失败 3已消费
    private Long tryCount;//重试次数
    private Date nextTryTime;//下次重试时间
    private Date createTime;//创建时间
    private Date updateTime;//更新时间

    public MsgLog(String msgId, Long status) {
        this.msgId = msgId;
        this.status = status;
    }

    public MsgLog(String msgId, String msg, String exchange, String routingKey, Long status, Date createTime, Date updateTime) {
        this.msgId = msgId;
        this.msg = msg;
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    //    private static volatile MsgLog instance;
//    public static MsgLog getInstance() {
//        if ( instance == null ) {
//            synchronized (MsgLog.class) {
//                if ( instance == null ) {
//                    instance = new MsgLog();
//                }
//            }
//        }
//        return instance;
//    }

}