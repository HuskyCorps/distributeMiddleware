package com.xinyunkeji.bigdata.convenience.model.mapper;


import com.xinyunkeji.bigdata.convenience.model.entity.UserVip;
import org.apache.ibatis.annotations.Param;

/**
 * 用户vip信息mapper
 *
 * @author Yuezejian
 * @date 2020年 08月22日 19:54:25
 */
public interface UserVipMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserVip record);

    int insertSelective(UserVip record);

    UserVip selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserVip record);

    int updateByPrimaryKey(UserVip record);

    int updateExpireVip(@Param("id") Integer id);
}