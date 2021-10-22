package com.hjw.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hujw
 * @description
 * @create 2021-10-16 14:26
 */
//标注哪些方法是用户登录后有效（比如修改头像密码的方法）
@Target(ElementType.METHOD)//作用于方法上
@Retention(RetentionPolicy.RUNTIME)//程序运行时有效
public @interface LoginRequired {
}
