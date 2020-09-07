package com.xinyunkeji.bigdata.convenience.server.controller;

import com.xinyunkeji.bigdata.convenience.api.response.BaseResponse;
import com.xinyunkeji.bigdata.convenience.api.response.StatusCode;
import com.xinyunkeji.bigdata.convenience.model.entity.User;
import com.xinyunkeji.bigdata.convenience.model.mapper.UserMapper;
import com.xinyunkeji.bigdata.convenience.server.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户Controller
 *
 * @author Yuezejian
 * @date 2020年 09月01日 16:09:20
 */
@RestController
@RequestMapping("user")
public class UserController extends AbstractController {

    @Autowired
    private UserMapper userMapper;

    private static final ThreadLocal<User> currentUser = ThreadLocal.withInitial(() -> null);

    @RequestMapping("login")
    public BaseResponse login(@RequestBody @Validated User user , BindingResult result) {
        String checkResult = ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(checkResult)) {
            return new BaseResponse(StatusCode.InvalidParams.getCode(),checkResult);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);

        String before = Thread.currentThread().getName() + ":" + currentUser.get();
        currentUser.set(user);
        try {
            String after = Thread.currentThread().getName() + ":" + currentUser.get();
            StringBuilder sb = new StringBuilder();
            sb.append(before).append("->").append(after);
            response.setData(sb);
        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        } finally {
            currentUser.remove();
        }
        return response;

    }
}