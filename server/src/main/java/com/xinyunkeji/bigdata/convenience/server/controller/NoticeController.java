package com.xinyunkeji.bigdata.convenience.server.controller;

import com.xinyunkeji.bigdata.convenience.api.response.BaseResponse;
import com.xinyunkeji.bigdata.convenience.api.response.StatusCode;
import com.xinyunkeji.bigdata.convenience.model.entity.Notice;
import com.xinyunkeji.bigdata.convenience.model.mapper.NoticeMapper;
import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import com.xinyunkeji.bigdata.convenience.server.service.log.LogAopAnnotation;
import com.xinyunkeji.bigdata.convenience.server.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 商户公告-redis订阅发布机制
 *
 * @author Yuezejian
 * @date 2020年 08月25日 20:24:52
 */
@RestController
@RequestMapping("notice")
public class NoticeController extends AbstractController {

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    //系统管理员添加公告-redis订阅发布机制-发送接收消息
    @RequestMapping("add")
    @LogAopAnnotation(value = "发送公告",operatorTable = "notice")
    public BaseResponse addNotice(@RequestBody @Validated Notice notice, BindingResult result) {
        String checkRes = ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(checkRes)) {
            return new BaseResponse(StatusCode.InvalidParams.getCode(),checkRes);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            //TODO:公告存入数据库，并发送到channel中
            int res = noticeMapper.insertSelective(notice);
            if ( res > 0 ){
                redisTemplate.convertAndSend(Constant.RedisTopicNameEmail,notice);
            }
        } catch (Exception e) {
            log.error("系统管理员添加公告-redis订阅发布机制-发送接收消息异常：",e);
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }
}