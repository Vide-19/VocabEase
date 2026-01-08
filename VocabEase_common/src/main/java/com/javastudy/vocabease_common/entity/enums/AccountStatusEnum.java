package com.javastudy.vocabease_common.entity.enums;

public enum AccountStatusEnum {
    ENABLED(1, "可用"),
    DISABLED(0, "禁用");

    private Integer status;

    private String description;

    AccountStatusEnum(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

    public static AccountStatusEnum getByStatus(Integer status) {
        for (AccountStatusEnum accountStatusEnum : AccountStatusEnum.values())
            if (accountStatusEnum.getStatus().equals(status))
                return accountStatusEnum;
        return null;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
