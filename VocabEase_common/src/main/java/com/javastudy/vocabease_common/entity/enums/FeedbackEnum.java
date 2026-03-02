package com.javastudy.vocabease_common.entity.enums;

public enum FeedbackEnum {
    CLIENT(0, "访客"),
    ADMIN(1, "管理员"),
    REGULAR(10, "普通用户"),
    NO_REPLY(2, "未回复"),
    IS_REPLY(3, "已回复");

    private Integer code;
    private String description;

    FeedbackEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
    public Integer getCode() {
        return code;
    }
    public String getDescription() {
        return description;
    }
    public static FeedbackEnum getEnumByCode(Integer code) {
        for (FeedbackEnum feedbackEnum : FeedbackEnum.values())
            if (feedbackEnum.getCode().equals(code))
                return feedbackEnum;
        return null;
    }
}
