package com.xinyunkeji.bigdata.convenience.server.utils;

import org.springframework.validation.BindingResult;

/**
 * 统一校验前端参数的工具
 *
 * @author Yuezejian
 * @date 2020年 08月23日 10:11:17
 */
public class ValidatorUtil {

    //TODO:统一校验前端传递过来的参数
    public static String checkResult(BindingResult result){
        StringBuilder sb=new StringBuilder("");
        if (result.hasErrors()){
            /*List<ObjectError> list=result.getAllErrors();
            for (ObjectError error:list){
                sb.append(error.getDefaultMessage()).append("\n");
            }*/

            //java8 steam api
            result.getAllErrors().forEach(error -> sb.append(error.getDefaultMessage()).append("\n"));
        }
        return sb.toString();
    }

//    public static void main(String[] args) {
//        Gson gson=new Gson();
//
//        Notice notice=new Notice(null,"标题","内容",Byte.valueOf("1"));
//        String msg=gson.toJson(notice);
//        System.out.println(msg);
//
//        Notice newNotice=gson.fromJson(msg,Notice.class);
//        System.out.println(newNotice);
//
//    }
}

































