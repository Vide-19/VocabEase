package com.javastudy.vocabease_common.entity.enums;

public enum MenuCheckTypeEnum {
    ALL(1, "全选"),
    HALF(0, "半选");

    private int checkTypeCode;
    private String description;

    private MenuCheckTypeEnum(int checkTypeCode, String description) {
        this.checkTypeCode = checkTypeCode;
        this.description = description;
    }

    public int getCheckTypeCode() {
        return checkTypeCode;
    }

    public void setCheckTypeCode(int checkTypeCode) {
        this.checkTypeCode = checkTypeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
