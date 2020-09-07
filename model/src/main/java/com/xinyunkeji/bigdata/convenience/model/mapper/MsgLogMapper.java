package com.xinyunkeji.bigdata.convenience.model.mapper;


import com.xinyunkeji.bigdata.convenience.model.entity.MsgLog;

/**
 * 消息发送日志记录
 *
 * @author Yuezejian
 * @date 2020年 08月27日 20:25:53
 */
public interface MsgLogMapper {
    int deleteByPrimaryKey(String id);

    int insert(MsgLog record);

    int insertSelective(MsgLog record);

    MsgLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MsgLog record);

    int updateByPrimaryKey(MsgLog record);
}
