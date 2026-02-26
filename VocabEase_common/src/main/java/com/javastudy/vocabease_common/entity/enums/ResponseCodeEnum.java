package com.javastudy.vocabease_common.entity.enums;


public enum ResponseCodeEnum {
    CODE_200(200, "请求成功"),
    CODE_400(400, "请求参数错误"),
    CODE_401(401, "登录超时"),
    CODE_403(403, "权限不足"),
    CODE_404(404, "请求地址不存在"),
    CODE_409(409, "信息已经存在"),
    CODE_429(429, "请求过于频繁"),
    CODE_500(500, "服务器返回错误，请联系管理员");

    private Integer code;

    private String msg;

    ResponseCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
