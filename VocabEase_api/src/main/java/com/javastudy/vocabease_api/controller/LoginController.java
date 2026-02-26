package com.javastudy.vocabease_api.controller;

import com.javastudy.vocabease_api.annotation.GlobalInterceptor;
import com.javastudy.vocabease_common.component.RedisUtil;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.constants.Constants;
import com.javastudy.vocabease_common.entity.dto.CreateImagCode;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.entity.enums.VerifyRegexEnum;
import com.javastudy.vocabease_common.entity.po.AppAccount;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.service.AppAccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/account")
public class LoginController extends ABaseController {

    @Resource
    private AppAccountService appAccountService;
    @Resource
    private RedisUtil redisUtil;

    private static final int CHECK_CODE_TYPE_REGISTER = 0;
    private static final int CHECK_CODE_TYPE_LOGIN = 1;

    @RequestMapping("/checkCode")
    @GlobalInterceptor
    public void checkCode(HttpServletResponse response,
                          @VerifyParam(required = true) String deviceId,
                          @VerifyParam(required = true) Integer type) throws IOException {
        //验证码
        CreateImagCode code = new CreateImagCode(130, 38, 5, 10);
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        String myCode = code.getCode();

        String redisKey = Constants.REDIS_KEY_CHECK_CODE + deviceId + type;
        this.redisUtil.sets(redisKey, myCode, 10 * 60);

        code.write(response.getOutputStream());
    }
    /**
     * 注册
     */
    @RequestMapping("/register")
    public ResponseVO<Void> register(HttpServletRequest request,
                                     @VerifyParam(required = true, regex = VerifyRegexEnum.EMAIL) String email,
                                     @VerifyParam(required = true, max = 30)  String nickName,
                                     @VerifyParam(required = true, regex = VerifyRegexEnum.PASSWORD) String password,
                                     @VerifyParam(required = true) Integer gender,
                                     @VerifyParam(required = true) String checkCode,
                                     @VerifyParam(required = true, max = 32) String deviceId,
                                     @VerifyParam(required = true, max = 32) String deviceBrand) {
        String redisKey = Constants.REDIS_KEY_CHECK_CODE + deviceId + CHECK_CODE_TYPE_REGISTER;
        try {
            String checkCodeRedis = (String) this.redisUtil.get(redisKey);//可能为空
            if (!checkCode.equals(checkCodeRedis))
                throw new BusinessException("验证码错误");
            AppAccount account = new AppAccount();
            account.setEmail(email);
            account.setNickName(nickName);
            account.setPassword(password);
            account.setGender(gender);
            account.setLastUseDeviceId(deviceId);
            account.setLastUseDeviceBrand(deviceBrand);
            account.setLastLoginIp(getIpAddress(request));
            appAccountService.register(account);
        } catch (Exception e) {
            throw new BusinessException(ResponseCodeEnum.CODE_400);
        } finally {
            redisUtil.delete(redisKey);
        }
        return getSuccessResponseVO(null);
    }
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
    /**
     * 登录
     */
    @RequestMapping("/autoLogin")
    @GlobalInterceptor
    public ResponseVO<String> autoLogin(HttpServletRequest request,
                                    @VerifyParam(required = true) String token,
                                    @VerifyParam(required = true, max = 32) String deviceId,
                                    @VerifyParam(required = true, max = 32) String deviceBrand) {
        String tokenNew = this.appAccountService.autoLogin(token, getIpAddress(request), deviceId, deviceBrand);
        return getSuccessResponseVO(tokenNew);
    }

}
