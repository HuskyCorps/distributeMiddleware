package com.xinyunkeji.bigdata.convenience.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * 公告实体类
 *
 * @author Yuezejian
 * @date 2020年 08月22日 19:41:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notice implements Serializable{

    private Integer id;

    @NotBlank(message = "公告标题不能为空！")
    private String title;

    @NotBlank(message = "公告内容不能为空！")
    private String content;

    @NotBlank(message = "用户邮箱不能为空！")
    private String userMails;

    private Byte isActive=1;
}