package com.xinyunkeji.bigdata.convenience.server.controller;

import com.xinyunkeji.bigdata.convenience.api.response.BaseResponse;
import com.xinyunkeji.bigdata.convenience.api.response.StatusCode;
import com.xinyunkeji.bigdata.convenience.model.dto.PraiseDto;
import com.xinyunkeji.bigdata.convenience.model.entity.Article;
import com.xinyunkeji.bigdata.convenience.model.mapper.ArticleMapper;
import com.xinyunkeji.bigdata.convenience.server.service.article.PraiseService;
import com.xinyunkeji.bigdata.convenience.server.service.log.LogAopAnnotation;
import com.xinyunkeji.bigdata.convenience.server.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 文章点赞Controller
 *
 * @author Yuezejian
 * @date 2020年 09月10日 19:59:46
 */
@RestController
@RequestMapping("praise")
public class PraiseController extends AbstractController{

    @Autowired
    private PraiseService praiseService;

    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 获取文章列表
     * @return
     */
    @RequestMapping(value = "getArticleList", method = RequestMethod.GET)
    public BaseResponse getArticleList() {
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            response.setData(praiseService.getAll());
        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    /**
     * 获取文章详情及点赞信息
     * @param articleId
     * @param curUserId
     * @return
     */
    @RequestMapping("getArticleInfo")
    public BaseResponse getArticleInfo(@RequestParam Integer articleId,Integer curUserId) {
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            response.setData(praiseService.getArticleInfo(articleId,curUserId));

        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    /**
     * 文章点赞
     * @param dto
     * @param result
     * @return
     */
    @RequestMapping(value = "on",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @LogAopAnnotation(value = "文章点赞",operatorTable = "article_praise和article")
    public BaseResponse praiseOn(@RequestBody @Validated PraiseDto dto, BindingResult result) {
        String resCheck = ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(resCheck)) {
            return new BaseResponse(StatusCode.InvalidParams.getCode(),resCheck);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            //TODO：查询点赞文章是否失效
            Article article = articleMapper.selectByPrimaryKey(dto.getArticleId());
            if ( article != null) {
                if (praiseService.praiseOn(dto)) {
                    response.setData("点赞成功");
                } else {
                    response.setData("该文章已经点赞过了");
                }
            } else {
                response.setData("该文章已下架或不存在");
            }
        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    /**
     * 取消文章点赞
     * @param dto
     * @param result
     * @return
     */
    @RequestMapping(value = "off",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @LogAopAnnotation(value = "取消文章点赞",operatorTable = "article_praise和article")
    public BaseResponse praiseOff(@RequestBody @Validated PraiseDto dto, BindingResult result) {
        String resCheck = ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(resCheck)) {
            return new BaseResponse(StatusCode.InvalidParams.getCode(),resCheck);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            //TODO：查询点赞文章是否失效
            Article article = articleMapper.selectByPrimaryKey(dto.getArticleId());
            if (article != null) {

                if (praiseService.praiseOff(dto)) {
                    response.setData("已成功取消点赞");
                } else {
                    response.setData("您还未点赞过该文章");
                }
            } else {
                response.setData("该文章已下架或不存在");
            }

        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    /**
     * 获取当前用户点赞过的历史文章-用户详情
     * @param currentUserId
     * @return
     */
    @RequestMapping(value = "praiseHistory",method = RequestMethod.POST)
    public BaseResponse getHistoryPraiseArticleList(@RequestParam Integer currentUserId) {
        if (currentUserId == null || currentUserId < 0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {

            response.setData(praiseService.getPraiseHistoryArticle(currentUserId));

        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    /**
     * 获取文章点赞排行榜
     * @return
     */
    @RequestMapping(value = "ranks" ,method = RequestMethod.POST)
    public BaseResponse getRankingList() {
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            response.setData(praiseService.getPraiseRanks());

        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }




}