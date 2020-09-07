package com.xinyunkeji.bigdata.convenience.model.mapper;


import com.xinyunkeji.bigdata.convenience.model.entity.ArticlePraise;
import org.apache.ibatis.annotations.Param;

/**
 * 文章点赞mapper
 *
 * @author Yuezejian
 * @date 2020年 08月22日 19:51:25
 */
public interface ArticlePraiseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ArticlePraise record);

    int insertSelective(ArticlePraise record);

    ArticlePraise selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ArticlePraise record);

    int updateByPrimaryKey(ArticlePraise record);

    int cancelPraise(@Param("articleId") Integer articleId, @Param("userId") Integer userId);
}