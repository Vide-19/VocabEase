package com.javastudy.vocabease_common.entity.annotation;

import com.javastudy.vocabease_common.entity.enums.VerifyRegexEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyParam {
    /**
     * 最大长度默认-1
     * @return
     */
    int max() default -1;

    /**
     * 最小长度默认-1
     * @return
     */
    int min() default -1;

    /**
     * 请求 默认非必填
     * @return
     */
    boolean required() default false;

    /**
     * 校验正则表达式
     * @return
     */
    VerifyRegexEnum regex() default VerifyRegexEnum.NO;
}
