package com.javastudy.vocabease_api.controller;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.javastudy.vocabease_common.entity.config.AppConfig;
import com.javastudy.vocabease_common.entity.constants.Constants;
import com.javastudy.vocabease_common.entity.dto.AppAccountDto;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.utils.JWTUtil;
import com.javastudy.vocabease_common.utils.StringTools;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


public class ABaseController {

    protected static final String STATUC_SUCCESS = "success";

    protected static final String STATUC_ERROR = "error";

    @Resource
    private JWTUtil<AppAccountDto> jwtUtil;
    @Resource
    private AppConfig appConfig;

    protected <T> ResponseVO<T> getSuccessResponseVO(T t) {
        ResponseVO<T> responseVO = new ResponseVO<>();
        responseVO.setStatus(STATUC_SUCCESS);
        responseVO.setCode(ResponseCodeEnum.CODE_200.getCode());
        responseVO.setInfo(ResponseCodeEnum.CODE_200.getMsg());
        responseVO.setData(t);
        return responseVO;
    }

    protected <T> ResponseVO<T> getBusinessErrorResponseVO(BusinessException e, T t) {
        ResponseVO<T> vo = new ResponseVO<>();
        vo.setStatus(STATUC_ERROR);
        if (e.getCode() == null) {
            vo.setCode(ResponseCodeEnum.CODE_400.getCode());
        } else {
            vo.setCode(e.getCode());
        }
        vo.setInfo(e.getMessage());
        vo.setData(t);
        return vo;
    }

    protected <T> ResponseVO<T> getServerErrorResponseVO(T t) {
        ResponseVO<T> vo = new ResponseVO<>();
        vo.setStatus(STATUC_ERROR);
        vo.setCode(ResponseCodeEnum.CODE_500.getCode());
        vo.setInfo(ResponseCodeEnum.CODE_500.getMsg());
        vo.setData(t);
        return vo;
    }

    protected AppAccountDto getTokenUserAdminDto(String token) {
        return jwtUtil.getTokenData(Constants.JWT_KEY_LOGIN_TOKEN, token, AppAccountDto.class);
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

    protected String resetContentImg (String content) {
        if (StringTools.isEmpty(content))
            return content;
        content = content.replace(Constants.READ_IMG_PATH, appConfig.getAppDomain() + Constants.READ_IMG_PATH);
        return content;
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseBody
    public ResponseVO<T> handleTokenExpired(TokenExpiredException e) {
        BusinessException businessException = new BusinessException(ResponseCodeEnum.CODE_401);
        return getBusinessErrorResponseVO(businessException, null);
    }
}