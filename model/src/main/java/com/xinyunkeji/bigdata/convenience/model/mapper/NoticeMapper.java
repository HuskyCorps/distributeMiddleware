package com.xinyunkeji.bigdata.convenience.model.mapper;


import com.xinyunkeji.bigdata.convenience.model.entity.Notice;

/**
 * 公告mapper
 *
 * @author Yuezejian
 * @date 2020年 08月22日 19:51:26
 */
public interface NoticeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Notice record);

    int insertSelective(Notice record);

    Notice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Notice record);

    int updateByPrimaryKey(Notice record);
}