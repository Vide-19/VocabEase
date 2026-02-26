package com.javastudy.vocabease_common.entity.enums;

public enum CollectTypeEnum {
    SHARE(0, "分享"),
    WORD(1, "单词"),
    ARTICLE(2, "文章"),
    QUESTION(3, "问题");

    private Integer type;
    private String description;

    CollectTypeEnum(Integer type, String description) {
        this.type = type;
        this.description = description;
    }
    public Integer getType() {
        return type;
    }
    public String getDescription() {
        return description;
    }
    public static CollectTypeEnum getEnum(Integer type) {
        for (CollectTypeEnum item : CollectTypeEnum.values())
            if (item.getType().equals(type))
                return item;
        return null;
    }
}
