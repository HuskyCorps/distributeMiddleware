package com.xinyunkeji.bigdata.convenience.model.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户订单实体类
 *
 * @author Yuezejian
 * @date 2020年 08月22日 19:41:16
 */
@Data
public class UserOrder implements Serializable{
    private Integer id;

    @NotNull(message = "用户id不能为空！")
    private Integer userId;

    private String orderNo;

    private Byte payStatus=1;

    private Byte isActive=1;

    private Date orderTime;

    private Date updateTime;
}