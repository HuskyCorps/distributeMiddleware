package com.xinyunkeji.bigdata.convenience.model.mapper;

import com.xinyunkeji.bigdata.convenience.model.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户mapper
 *
 * @author Yuezejian
 * @date 2020年 08月22日 19:55:24
 */
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> selectList();

    User selectByEmail(@Param("email") String email);

    String selectNamesById(@Param("ids") String ids);
}