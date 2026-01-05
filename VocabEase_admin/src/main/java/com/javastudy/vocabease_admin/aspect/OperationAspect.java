package com.javastudy.vocabease_admin.aspect;

import com.javastudy.vocabease_admin.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.utils.StringTools;
import com.javastudy.vocabease_common.utils.VerifyUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

//AOP切面类
@Aspect
@Component("operationAspect")
public class OperationAspect {
    private Logger logger = LoggerFactory.getLogger(OperationAspect.class);
    public static final String[] BASE_TYPE = new String[]{"java.lang.String",
            "java.lang.Integer", "java.lang.Long"};
    //注解配置
    /*
    //定义切点
    @Pointcut("@annotation(com.javastudy.vocabease_admin.annotation.GlobalInterceptor)")
    public void pointCut() {}
    //定义事件通知类型
    @Before("pointCut()")
    public void interceptorDo(JoinPoint joinPoint) {}*/

    //内置配置
    @Before("@annotation(com.javastudy.vocabease_admin.annotation.GlobalInterceptor)")
    public void interceptorDo(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        GlobalInterceptor interceptor = method.getAnnotation(GlobalInterceptor.class);
        if (interceptor == null)
            return;
        if (interceptor.checkParams()) {
            validateParams(method, args);
        }

    }

    private void checkValue(Object value, VerifyParam verifyParam) {
        Boolean isEmpty = value == null || StringTools.isEmpty(value.toString());
        Integer length = value == null ? 0 : value.toString().length();
        /**
         * 校验空 参数空但又必须传参
         */
        if (isEmpty && verifyParam.required())
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        /**
         * 校验长度
         */
        if (!isEmpty && (verifyParam.max() != -1 && verifyParam.max() < length || verifyParam.min() != -1 && verifyParam.min() > length))
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        /**
         * 校验正则
         */
        if (!isEmpty && !StringTools.isEmpty(verifyParam.regex().getRegex()) && !VerifyUtil.verify(verifyParam.regex(), value.toString()))
            throw new BusinessException(ResponseCodeEnum.CODE_600);

    }
    /**
     * 参数校验
     */
    private void validateParams(Method method, Object[] arguments) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Object argument = arguments[i];
            VerifyParam verifyParam = parameter.getAnnotation(VerifyParam.class);
            if (verifyParam == null)
                continue;
            String paramType = parameter.getParameterizedType().getTypeName();
            if (ArrayUtils.contains(BASE_TYPE, paramType)) {
                checkValue(argument, verifyParam);
            } else {
                checkObjectValue(parameter, argument);
            }
        }
    }
    private void checkObjectValue(Parameter parameter, Object value) {
        try {
            String paramType = parameter.getParameterizedType().getTypeName();
            Class<?> clazz = Class.forName(paramType);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                VerifyParam verifyParam = field.getAnnotation(VerifyParam.class);
                if (verifyParam == null)
                    continue;
                field.setAccessible(true);
                Object fieldValue = field.get(value);
                checkValue(fieldValue, verifyParam);
            }
        } catch (Exception e) {
            logger.error("校验参数错误", e);
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

    }

}
