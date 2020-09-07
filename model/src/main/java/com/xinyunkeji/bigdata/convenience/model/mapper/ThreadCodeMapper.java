package com.xinyunkeji.bigdata.convenience.model.mapper;


import com.xinyunkeji.bigdata.convenience.model.entity.ThreadCode;
import org.apache.ibatis.annotations.Param;

/**
 * 线程码mapper
 *
 * @author Yuezejian
 * @date 2020年 08月22日 19:51:33
 */
public interface ThreadCodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ThreadCode record);

    int insertSelective(ThreadCode record);

    ThreadCode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ThreadCode record);

    int updateByPrimaryKey(ThreadCode record);

    int insertCode(@Param("code") String code);
}