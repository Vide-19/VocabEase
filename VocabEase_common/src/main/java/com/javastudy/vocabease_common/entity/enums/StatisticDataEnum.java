package com.javastudy.vocabease_common.entity.enums;

public enum StatisticDataEnum {
    APP_READ(0, "小程序浏览"),
    REGISTER_USER(1, "注册用户"),
    ARTICLE(2, "文章"),
    WORD(3, "单词"),
    QUESTION(4, "问题"),
    SHARE(5, "笔记"),
    FEEDBACK(6, "反馈");

    private Integer code;
    private String description;

    StatisticDataEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }
    public String getDescription() {
        return description;
    }
    public static StatisticDataEnum getEnumByCode(Integer code) {
        for (StatisticDataEnum e : StatisticDataEnum.values())
            if (e.getCode().equals(code))
                return e;
        return null;
    }
}
