package com.xinyunkeji.bigdata.convenience.server.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.amqp.rabbit.support.CorrelationData;

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
}