package com.javastudy.vocabease_common.entity.enums;

public enum VerifyRegexEnum {
    NO("", "不校验"),
    IP("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", "ip地址"),
    POSITIVE_INTEGER("^[1-9]\\d*$", "正整数"),
    NUMBER_LETTER_UNDERLINE("^\\w+$", "数字字母下划线"),
    EMAIL("^[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[a-zA-Z]{2,}$", "邮箱"),
    PHONE("^1[3-9]\\d{9}$", "电话"),
    COMMON("^[a-zA-Z0-9_\\u4e00-\\u9fa5]+$", "数字字母中文下划线"),
    PASSWORD("^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[~!@#$%^&*_])[\\da-zA-Z~!@#$%^&*_]{8,20}$", "密码，数字字母特殊字符8-20位"),
    ACCOUNT("^[a-zA-Z][a-zA-Z0-9_]{7,19}$", "账户，字母开头，1-20位"),
    MONEY("^[0-9]+(\\.[0-9]{1,2})?$", "金额");
    //正则表达式
    private String regex;
    private String description;

    VerifyRegexEnum(String regex, String description) {
        this.regex = regex;
        this.description = description;
    }

    public String getRegex() {
        return regex;
    }
    public String getDescription() {
        return description;
    }

}
