package com.javastudy.vocabease_common.entity.enums;

import java.util.Objects;

public enum CategoryTypeEnum {
    ARTICLE(3, "文章"),
    QUESTION(1, "问题"),
    OTHERS(4, "其它"),
    WORD(2, "单词");

    private Integer type;
    private String description;

    CategoryTypeEnum(Integer type, String description) {
        this.type = type;
        this.description = description;
    }

    public Integer getType() {return type;}
    public String getDescription() {return description;}

    public static CategoryTypeEnum getEnumByType(Integer type) {
        for (CategoryTypeEnum e : CategoryTypeEnum.values()) {
            if (Objects.equals(e.getType(), type))
                return e;
        }
        return null;
    }
}
