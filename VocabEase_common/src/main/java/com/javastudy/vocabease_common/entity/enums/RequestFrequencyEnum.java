package com.javastudy.vocabease_common.entity.enums;

public enum RequestFrequencyEnum {
    DAY(60 * 60 * 24, "一天"),
    HOUR(60 * 60, "一小时"),
    MINUTE(60, "一分钟"),
    SECOND(1, "一秒"),
    NO_LIMIT(0, "无限制");

    private Integer second;
    private String description;

    private RequestFrequencyEnum(Integer second, String description) {
        this.second = second;
        this.description = description;
    }

    public Integer getSecond() {return second;}

    public String getDescription() {return description;}

    public static RequestFrequencyEnum getEnum(Integer second) {
        for (RequestFrequencyEnum e : RequestFrequencyEnum.values())
            if (e.getSecond().equals(second))
                return e;
        return null;
    }
}
