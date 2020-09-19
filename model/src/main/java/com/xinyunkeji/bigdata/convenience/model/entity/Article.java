package com.xinyunkeji.bigdata.convenience.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章实体类
 *
 * @author Yuezejian
 * @date 2020年 08月22日 19:41:16
 */
@Data
public class Article implements Serializable{
    private Integer id;

    private String title;

    private String content;

    private Integer userId;

    private Integer scanTotal;

    private Integer praiseTotal;

    private Byte isActive;

    private Date createTime;

    private Date updateTime;

    /**
     * 临时字段-用户姓名
     */
    private String userName;
}