package com.xinyunkeji.bigdata.convenience.server.service.log;

import org.springframework.context.ApplicationEvent;
import net.sf.json.JSONObject;
import java.io.Serializable;

/**
 * spring消息驱动模型：applocationEvent and applocationListener ;消息（事件）
 *
 * @author Yuezejian
 * @date 2020年 08月22日 20:30:48
 */
public class LogApplicationEvent extends ApplicationEvent implements Serializable {

    private String username;
    private String operation;
    private String method;
    private String params;

    public LogApplicationEvent(Object source, String username, String operation, String method) {
        super(source);
        this.username = username;
        this.operation = operation;
        this.method = method;
    }

    public LogApplicationEvent(Object source, String username, String operation, String method, String params) {
        super(source);
        this.username = username;
        this.operation = operation;
        this.method = method;
        this.params = params;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return JSONObject.fromObject(this).toString();
    }
}