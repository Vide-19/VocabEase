package com.javastudy.vocabease_admin.annotation;

import com.javastudy.vocabease_common.entity.enums.PermissionCodeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//定义注解
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GlobalInterceptor {
    //校验参数
    boolean checkParams() default true;
    //校验登录
    boolean checkLogin() default true;

    PermissionCodeEnum permissionCode() default PermissionCodeEnum.NO_PERMISSION;
}
