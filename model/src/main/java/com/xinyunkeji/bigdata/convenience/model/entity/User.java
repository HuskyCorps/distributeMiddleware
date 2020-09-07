package com.xinyunkeji.bigdata.convenience.model.entity;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * 用户实体类
 *
 * @author Yuezejian
 * @date 2020年 08月22日 19:41:16
 */
@Data
@ToString
public class User implements Serializable{
    private Integer id;

    @NotBlank(message = "姓名不能为空!")
    private String name;

    @NotBlank(message = "邮箱不能为空！")
    private String email;
}