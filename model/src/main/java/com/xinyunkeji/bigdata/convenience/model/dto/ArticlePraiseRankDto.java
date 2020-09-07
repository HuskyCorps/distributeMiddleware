package com.xinyunkeji.bigdata.convenience.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 文章点赞排行榜信息
 *
 * @author Yuezejian
 * @date 2020年 08月22日 19:41:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ArticlePraiseRankDto implements Serializable {

    //文章id
    private String articleId;

    //文章标题
    private String title;

    //点赞总数
    private String total;

    private Double score;
}