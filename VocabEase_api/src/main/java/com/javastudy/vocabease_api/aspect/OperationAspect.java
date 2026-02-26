package com.javastudy.vocabease_api.aspect;

import com.javastudy.vocabease_api.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.component.RedisUtil;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.constants.Constants;
import com.javastudy.vocabease_common.entity.dto.AppAccountDto;
import com.javastudy.vocabease_common.entity.enums.RequestFrequencyEnum;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.utils.JWTUtil;
import com.javastudy.vocabease_common.utils.StringTools;
import com.javastudy.vocabease_common.utils.VerifyUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

//AOP切面类
@Aspect
@Component("operationAspect")
public class OperationAspect {
    private final Logger logger = LoggerFactory.getLogger(OperationAspect.class);
    public static final String[] BASE_TYPE = new String[]{"java.lang.String",
            "java.lang.Integer", "java.lang.Long"};

    @Resource
    private JWTUtil<AppAccountDto> jwtUtil;
    @Resource
    private RedisUtil redisUtil;

    //内置配置
    @Before("@annotation(com.javastudy.vocabease_api.annotation.GlobalInterceptor)")
    public void interceptorDo(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        Object[] args = joinPoint.getArgs();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        GlobalInterceptor interceptor = method.getAnnotation(GlobalInterceptor.class);
        if (interceptor == null)
            return;
        //校验参数
        if (interceptor.checkParams())
            validateParams(method, args);
        //校验登录
        if (interceptor.checkLogin())
            checkLogin();
        //校验频次
        if (interceptor.requestFrequencyThreshold() != 0 &&
                interceptor.requestFrequencyThreshold() != RequestFrequencyEnum.NO_LIMIT.getSecond()) {
            String fullMethodName = target.getClass().getName() + "." + method.getName();
            checkRequestFrequency(fullMethodName, interceptor.frequencyType(), interceptor.requestFrequencyThreshold());
        }


    }

    private void checkValue(Object value, VerifyParam verifyParam) {
        Boolean isEmpty = value == null || StringTools.isEmpty(value.toString());
        Integer length = value == null ? 0 : value.toString().length();
        //校验空 参数空但又必须传参
        if (isEmpty && verifyParam.required())
            throw new BusinessException(ResponseCodeEnum.CODE_400);
        //校验长度
        if (!isEmpty && (verifyParam.max() != -1 && verifyParam.max() < length || verifyParam.min() != -1 && verifyParam.min() > length))
            throw new BusinessException(ResponseCodeEnum.CODE_400);
         //校验正则
        if (!isEmpty && !StringTools.isEmpty(verifyParam.regex().getRegex()) && !VerifyUtil.verify(verifyParam.regex(), value.toString()))
            throw new BusinessException(ResponseCodeEnum.CODE_400);
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
            throw new BusinessException(ResponseCodeEnum.CODE_400);
        }
    }

    private void checkLogin() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("token");
        AppAccountDto dto = jwtUtil.getTokenData(Constants.JWT_KEY_LOGIN_TOKEN, token, AppAccountDto.class);
        if (dto == null)
            throw new BusinessException(ResponseCodeEnum.CODE_401);
    }

    private void checkRequestFrequency(String fullMethodName, RequestFrequencyEnum frequencyType, Integer threshold) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = getIpAddress(request);
        ip = ip.replace(":", "");
        String redisKey = fullMethodName + ip;
        Integer count = (Integer) this.redisUtil.get(redisKey);
        if (count != null && count >= threshold)
            throw new BusinessException(ResponseCodeEnum.CODE_429);
        this.redisUtil.increment(redisKey, 1, frequencyType.getSecond());
    }

    protected String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip))
            if (ip.contains(",")) //多次反向代理后会有多个ip，第一个为真实ip
                ip = ip.split(",")[0];
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("Proxy-Client-IP");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("HTTP_CLIENT_IP");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip))
            ip = request.getRemoteAddr();
        return ip;
    }

}