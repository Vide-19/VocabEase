package com.javastudy.vocabease_common.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.javastudy.vocabease_common.entity.enums.ResponseCodeEnum;
import com.javastudy.vocabease_common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static String convertObject2Json(Object object) {
        return JSON.toJSONString(object);
    }

    public static <T> T convertJson2Object(String json, Class<T> clazz) {
        try {
            return JSONObject.parseObject(json, clazz);
        } catch (Exception e) {
            logger.error("json转object异常, json: {}", json);
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
    }

    public static <T> List<T> convertJsonArray2Object(String jsonArray, Class<T> clazz) {
        try {
            return JSONArray.parseArray(jsonArray, clazz);
        } catch (Exception e) {
            logger.error("jsonArray转object异常, json: {}", jsonArray);
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
    }

}
