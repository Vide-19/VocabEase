package com.javastudy.vocabease_common.utils;

import com.alibaba.fastjson2.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信小程序工具类 (适配 Fastjson2)
 */
public class WechatUtil {

    /**
     * 调用微信接口：code2session
     * 获取 openid 和 session_key
     *
     * @param appId 小程序 AppID
     * @param appSecret 小程序 AppSecret
     * @param jsCode 前端 wx.login() 获取的 code
     * @return Map 包含 openid, session_key, unionid(如果有)
     */
    public static Map<String, String> getJsCode2Session(String appId, String appSecret, String jsCode) {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        // 注意：实际生产中建议对参数进行 URLEncode，虽然微信这几个参数通常不需要，但加上更严谨
        String params = "?appid=" + appId + "&secret=" + appSecret + "&js_code=" + jsCode + "&grant_type=authorization_code";

        BufferedReader in = null;
        try {
            URL requestUrl = new URL(url + params);
            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            // GET 请求通常不需要 setDoOutput(true)，除非你要发送 body，这里去掉以防万一
            // connection.setDoOutput(true);

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }

                String responseStr = response.toString();

                // 【修正点 1】使用 parseObject 解析字符串
                JSONObject json = JSONObject.parseObject(responseStr);

                // 【修正点 2】使用 containsKey 判断字段是否存在，或者直接获取判断 null
                if (json.containsKey("openid")) {
                    Map<String, String> result = new HashMap<>();

                    // 获取字符串，如果不存在会返回 null
                    String openid = json.getString("openid");
                    if (openid != null) {
                        result.put("openid", openid);
                    }

                    if (json.containsKey("session_key")) {
                        result.put("session_key", json.getString("session_key"));
                    }

                    if (json.containsKey("unionid")) {
                        result.put("unionid", json.getString("unionid"));
                    }

                    return result;
                } else {
                    // 微信返回了错误信息，如 errcode, errmsg
                    // 例如：{"errcode":40029,"errmsg":"invalid code"}
                    Integer errCode = json.getInteger("errcode");
                    String errMsg = json.getString("errmsg");
                    System.err.println("微信登录失败 - 错误码:" + errCode + ", 信息:" + errMsg);
                    return null;
                }
            } else {
                System.err.println("请求微信服务器失败，HTTP码: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}