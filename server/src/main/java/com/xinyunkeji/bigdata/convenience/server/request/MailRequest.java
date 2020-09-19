package com.xinyunkeji.bigdata.convenience.server.request;

import com.google.gson.Gson;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.IOException;
import java.io.Serializable;

/**
 * 邮件
 *
 * @author Yuezejian
 * @date 2020年 08月24日 19:39:42
 */
@Data
public class MailRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String correlationData;//RabbitMQ queue唯一标识符

    @NotBlank(message = "用户邮箱不能为空！")
    private String userMails;

    @NotBlank(message = "邮箱主题不能为空！")
    private String subject;

    @NotBlank(message = "邮箱内容不能为空！")
    private String content;

    public MailRequest(String correlationData, String userMails, String subject, String content) {
        this.correlationData = correlationData;
        this.userMails = userMails;
        this.subject = subject;
        this.content = content;
    }

    /**
     * 为了解决一下问题
     *  Warning:no String-argument constructor/factory
     *  method to deserialize from String value
     * @param json
     * @throws IOException
     */
    public MailRequest(String json) throws IOException {
        MailRequest param = new Gson().fromJson(json, MailRequest.class);
        this.correlationData = param.getCorrelationData();
        this.userMails = param.getUserMails();
        this.subject = param.getSubject();
        this.content = param.getContent();
    }

}