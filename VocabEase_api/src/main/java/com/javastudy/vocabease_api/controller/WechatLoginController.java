package com.javastudy.vocabease_api.controller;

import com.javastudy.vocabease_common.entity.config.AppConfig;
import com.javastudy.vocabease_common.entity.po.AppAccount;
import com.javastudy.vocabease_common.entity.vo.ResponseVO;
import com.javastudy.vocabease_common.exception.BusinessException;
import com.javastudy.vocabease_common.service.AppAccountService;
import com.javastudy.vocabease_common.utils.WechatUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/account")
public class WechatLoginController extends ABaseController {

    @Resource
    private AppAccountService appAccountService;
    @Resource
    private AppConfig appConfig;

    @Value("${wechat.miniapp.appid}")
    private String appId;

    @Value("${wechat.miniapp.secret}")
    private String appSecret;

    private static final Logger logger = LoggerFactory.getLogger(WechatLoginController.class);
    /**
     * 微信小程序一键登录/注册
     * 修改点：在参数列表中加入 HttpServletRequest request
     */
    @RequestMapping("/wxLogin")
    public ResponseVO<Map<String, Object>> wxLogin(@RequestBody Map<String, String> params,
                                                   HttpServletRequest request) { // <--- 2. 添加此参数
        try {
            // 1. 调用微信接口获取 OpenID 和 SessionKey
            Map<String, String> wxResult = WechatUtil.getJsCode2Session(appId, appSecret, params.get("code"));

            if (wxResult == null || !wxResult.containsKey("openid")) {
                throw new BusinessException("微信登录失败，Code 无效");
            }

            String openId = wxResult.get("openid");
            String sessionKey = wxResult.get("session_key");

            // 2. 核心业务逻辑
            AppAccount account = this.appAccountService.getAccountByOpenId(openId);

            String token;
            boolean isNewUser = false;
            String deviceId = params.get("deviceId");
            String deviceBrand = params.get("deviceBrand");

            if (account == null) {
                // --- 新用户注册 ---
                AppAccount newAccount = new AppAccount();
                newAccount.setOpenId(openId);
                newAccount.setNickName("VocabEase用户_" + openId.substring(openId.length() - 4));
                newAccount.setGender(2);
                newAccount.setLastUseDeviceId(deviceId);
                newAccount.setLastUseDeviceBrand(deviceBrand);

                appAccountService.registerByWechat(newAccount);
                account = newAccount;
                isNewUser = true;
            } else {
                // --- 老用户更新 ---
                account.setLastUseDeviceId(deviceId);
                account.setLastUseDeviceBrand(deviceBrand);
                appAccountService.updateAccountDevice(account);
            }

            // 3. 生成 Token
            // 修改点：把刚才注入的 request 传进去，不要再传 null 了！
            token = this.appAccountService.generateToken(account, getIpAddress(request), deviceId, deviceBrand);
            logger.info("=== [登录接口] 生成 Token 使用的密钥：[{}]", appConfig.getJwtCommonSecret());
            logger.info("=== [登录接口] 生成的 Token 前缀：[{}]", token.substring(0, 20) + "...");
            // 4. 返回结果
            Map<String, Object> result = Map.of(
                    "token", token,
                    "isNewUser", isNewUser,
                    "userInfo", account
            );

            return getSuccessResponseVO(result);

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("登录异常：" + e.getMessage());
        }
    }
}