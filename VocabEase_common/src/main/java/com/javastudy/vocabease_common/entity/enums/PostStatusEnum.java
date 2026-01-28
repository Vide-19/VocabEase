package com.javastudy.vocabease_common.entity.enums;

import java.util.Objects;

public enum PostStatusEnum {
    NO_POST(0, "未发布"),
    IS_POST(1, "已发布");

    private Integer status;
    private String description;

    PostStatusEnum(Integer status, String description) {
        this.status = status;
        this.description = description;
    }
    public Integer getStatus() {
        return status;
    }
    public String getDescription() {
        return description;
    }
    public static PostStatusEnum getEnumByStatus(Integer status) {
        for (PostStatusEnum postStatusEnum : PostStatusEnum.values())
            if (Objects.equals(postStatusEnum.getStatus(), status))
                return postStatusEnum;
        return null;
    }
}
