package com.javastudy.vocabease_api.controller;

import com.javastudy.vocabease_api.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.component.RedisUtil;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.constants.Constants;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.service.AppAccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class LoginController extends ABaseController {

    @Resource
    private AppAccountService appAccountService;
    @Resource
    private RedisUtil redisUtil;

    private static final int CHECK_CODE_TYPE_REGISTER = 0;
    private static final int CHECK_CODE_TYPE_LOGIN = 1;

    /**
     * 登录
     */
    @RequestMapping("/login")
    @GlobalInterceptor
    public ResponseVO<String> login(HttpServletRequest request,
                                  @VerifyParam(required = true) String email,
                                  @VerifyParam(required = true) String password,
                                  @VerifyParam(required = true) String checkCode,
                                  @VerifyParam(required = true, max = 32) String deviceId,
                                  @VerifyParam(required = true, max = 32) String deviceBrand) {
        String redisKsy = Constants.REDIS_KEY_CHECK_CODE + deviceId + CHECK_CODE_TYPE_LOGIN;
        try {
            String checkCodeRedis = (String) this.redisUtil.get(redisKsy);
            if (!checkCode.equals(checkCodeRedis))
                throw new BusinessException("验证码错误");
            String token = this.appAccountService.login(email, password, getIpAddress(request), deviceId, deviceBrand);
            return getSuccessResponseVO(token);
        } finally {
            this.redisUtil.delete(redisKsy);
        }
    }
}
