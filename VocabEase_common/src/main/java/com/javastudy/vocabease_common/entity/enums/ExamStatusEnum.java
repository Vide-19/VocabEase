package com.javastudy.vocabease_common.entity.enums;

import java.util.Objects;

public enum ExamStatusEnum {
    NO_FINISH(0, "未完成"),
    IS_FINISH(1, "已完成"),
    TRUE(2, "正确"),
    FALSE(3, "错误");

    private Integer status;
    private String description;

    ExamStatusEnum(Integer status, String description) {
        this.status = status;
        this.description = description;
    }
    public Integer getStatus() {
        return status;
    }
    public String getDescription() {
        return description;
    }
    public static ExamStatusEnum getEnumByStatus(Integer status) {
        for (ExamStatusEnum postStatusEnum : ExamStatusEnum.values())
            if (Objects.equals(postStatusEnum.getStatus(), status))
                return postStatusEnum;
        return null;
    }
}
