package com.javastudy.vocabease_common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.javastudy.vocabease_common.entity.config.AppConfig;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("jwtUtil")
public class JWTUtil<T> {

    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    @Resource
    private AppConfig appConfig;

    /**
     * 签名生成
     */
    public String createToken(String key, T data, Integer expireDay) {
        String token = null;//👇根据my_project改生成，不仅仅是base64加密
        try {
            long expireMillis = expireDay.longValue() * 24L * 60L * 60L * 1000L;
            Date expiresAt = new Date(System.currentTimeMillis() + expireMillis);

            logger.info("=== [生成 Token] 过期天数={}, 计算出的毫秒增量={}, 过期时间={}",
                    expireDay, expireMillis, expiresAt);
             token = JWT.create()
                     .withClaim(key, JsonUtil.convertObject2Json(data))
                     .withExpiresAt(expiresAt)
                     .sign(Algorithm.HMAC256(appConfig.getJwtCommonSecret()));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return token;
    }
    /**
     * 签名验证
     */
    public <T> T getTokenData(String key, String token, Class<T> clazz) {
        try {
            if (StringTools.isEmpty(token))
                return null;
            String currentSecret = appConfig.getJwtCommonSecret();
            logger.info("=== [验证接口] 验证 Token 使用的密钥：[{}]", currentSecret);
            logger.info("=== [验证接口] 待验证 Token 前缀：[{}]", token.substring(0, 20) + "...");

            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(currentSecret)).build();
            DecodedJWT jwt = verifier.verify(token);
            String jsonData = jwt.getClaim(key).asString();
            return JsonUtil.convertJson2Object(jsonData, clazz);
        } catch (Exception e) {
            logger.error("JWT 验证失败，异常信息:", e);
            return null;
        }
    }
}