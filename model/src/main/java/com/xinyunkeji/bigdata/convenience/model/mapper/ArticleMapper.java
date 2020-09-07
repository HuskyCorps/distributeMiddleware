package com.xinyunkeji.bigdata.convenience.model.mapper;


import com.xinyunkeji.bigdata.convenience.model.entity.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章mapper
 *
 * @author Yuezejian
 * @date 2020年 08月22日 19:51:25
 */
public interface ArticleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Article record);

    int insertSelective(Article record);

    Article selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKey(Article record);

    List<Article> selectAll();

    int updatePraiseTotal(@Param("articleId") Integer articleId, @Param("flag") Integer flag);

    Article selectByPK(@Param("articleId") Integer articleId);
}