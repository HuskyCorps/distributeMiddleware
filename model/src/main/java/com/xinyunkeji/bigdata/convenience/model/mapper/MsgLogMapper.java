package com.xinyunkeji.bigdata.convenience.model.mapper;


import com.xinyunkeji.bigdata.convenience.model.entity.MsgLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

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

    List<MsgLog> selectTimeoutMsg(@Param("deliverLoading") Long deliverLoading, @Param("deliverFalse") Long deliverFalse, @Param("deliverSuccess") Long deliverSuccess);

    int updateStatus(@Param("msgId") String msgId,@Param("status") long status);

    int updateTryCount(@Param("msgId") String msgId,@Param("tryCount") Long tryCount,@Param("nextTryTime") Date nextTryTime);

}
