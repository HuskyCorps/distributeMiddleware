package com.xinyunkeji.bigdata.convenience.server.service.log;

import com.google.gson.Gson;
import com.xinyunkeji.bigdata.convenience.model.entity.SysLog;
import com.xinyunkeji.bigdata.convenience.server.enums.Constant;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 模块化的东西 - 类 - 切面 - 将关注点从核心业务逻辑中分离出来
 *
 * @author Yuezejian
 * @date 2020年 08月23日 09:37:49
 */
@Aspect
@Component
public class LogAopAspect {
    @Autowired
    private LogService logService;

    //切点；使用了特定注解的地方将触发通知（做日志的记录）- 切点
    @Pointcut("@annotation(com.xinyunkeji.bigdata.convenience.server.service.log.LogAopAnnotation)")
    public void logPointCut(){

    }

    //通知：环绕通知（前置通知+后置通知的结合），其实就是指定的注解所在的方法 执行前 + 执行后 + 监控
    @Around(value = "logPointCut()")
    public Object executeAround(ProceedingJoinPoint joinPoint) throws Throwable{

        Long start = System.currentTimeMillis();

        Object res = joinPoint.proceed();//获取运行结果

        Long time = System.currentTimeMillis() - start;

        saveLog(joinPoint,time,res);

        return res;
    }

    //记录日志(aop - 动态代理 - 底层Java的reflect反射来实现)
    private void saveLog(ProceedingJoinPoint joinPoint, Long time, Object res) throws Throwable{
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLog entity = new SysLog();

        //获取注解上用户操作描述
        LogAopAnnotation annotation = method.getAnnotation(LogAopAnnotation.class);
        if ( annotation != null ) {
            entity.setOperation(annotation.value());
            entity.setOperatorTable(annotation.operatorTable());
        }

        //获取操作的方法名（为了防止重名，我们可以把包名，类名，方法名拼起来）
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        entity.setMethod(new StringBuilder(className).append(".").append(methodName).append("()").toString());

        //获取方法请求参数
        Object[] args = joinPoint.getArgs();
        String params = new Gson().toJson(args[0]);
        entity.setParams(params);

        //获取所需其他的参数
        entity.setTime(time);
        entity.setUsername(Constant.logOperateUser);
        entity.setCreateDate(DateTime.now().toDate());

        if (res != null && StringUtils.isNotBlank(new Gson().toJson(res))){
            entity.setMemo(new Gson().toJson(res));
        }

        logService.recordLog(entity);


    }
}
