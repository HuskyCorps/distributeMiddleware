package com.xinyunkeji.bigdata.convenience.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 发送记录实体类
 *
 * @author Yuezejian
 * @date 2020年 08月22日 19:41:16
 */
@Data
@NoArgsConstructor
public class SendRecord implements Serializable{
    private Integer id;

    private String phone;

    private String code;

    private Byte isActive=1;

    private Date sendTime;

    public SendRecord(String phone, String code) {
        this.phone = phone;
        this.code = code;
    }
}