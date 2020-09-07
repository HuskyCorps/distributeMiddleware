package com.xinyunkeji.bigdata.convenience.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作记录实体类
 *
 * @author Yuezejian
 * @date 2020年 08月22日 19:41:16
 */
@Data
@NoArgsConstructor
public class SysLog implements Serializable{
    private Long id;

    private String username;

    private String operation;

    private String operatorTable;

    private String method;

    private String params;

    private Long time;

    private String ip="127.0.0.1";

    private Date createDate=new Date();

    private String memo;


    public SysLog(String username, String operation, String method) {
        this.username = username;
        this.operation = operation;
        this.method = method;
    }

    public SysLog(String username, String operation, String method, String params) {
        this.username = username;
        this.operation = operation;
        this.method = method;
        this.params = params;
    }

    public SysLog(String username, String operation, String operatorTable, String method, String params) {
        this.username = username;
        this.operation = operation;
        this.operatorTable = operatorTable;
        this.method = method;
        this.params = params;
    }
}