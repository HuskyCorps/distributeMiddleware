package com.xinyunkeji.bigdata.convenience.model.mapper;


import com.xinyunkeji.bigdata.convenience.model.entity.UserOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户订单mapper
 *
 * @author Yuezejian
 * @date 2020年 08月22日 19:58:39
 */
public interface UserOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserOrder record);

    int insertSelective(UserOrder record);

    UserOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserOrder record);

    int updateByPrimaryKey(UserOrder record);

    List<UserOrder> selectUnPayOrders();

    int unActiveOrder(@Param("id") Integer id);
}