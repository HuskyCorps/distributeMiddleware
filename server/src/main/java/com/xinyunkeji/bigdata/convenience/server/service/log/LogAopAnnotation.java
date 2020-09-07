package com.xinyunkeji.bigdata.convenience.server.service.log;

import java.lang.annotation.*;

/**
 * spring aop 触发点-注解(满足切点表达式，都可以触发)
 *
 * @author Yuezejian
 * @date 2020年 08月23日 07:51:00
 */
@Target(ElementType.METHOD)//触发对象为方法
@Retention(RetentionPolicy.RUNTIME)//运行时做持久化
@Documented //java.lang.annotation注解
public @interface LogAopAnnotation {
    String value() default "";//操作名称-新增，更新，删除
    String operatorName() default "";
    String operatorTable() default "";//所操作的表

}