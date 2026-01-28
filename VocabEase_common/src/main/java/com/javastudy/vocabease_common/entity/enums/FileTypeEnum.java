package com.javastudy.vocabease_common.entity.enums;

public enum FileTypeEnum {
    CATEGORY(0, 150, "分类图"),
    CAROUSEL(1, 400, "轮播图"),
    SHARE_LARGE(2 ,400, "大分享图"),
    SHARE_SMALL(3, 100, "小分享图");

    private Integer type;
    private Integer maxWidth;
    private String description;

    FileTypeEnum(Integer type, Integer maxWidth, String description) {
        this.type = type;
        this.maxWidth = maxWidth;
        this.description = description;
    }
    public Integer getType() {
        return type;
    }
    public static FileTypeEnum getType(Integer type) {
        for (FileTypeEnum fileTypeEnum : FileTypeEnum.values())
            if (fileTypeEnum.type.equals(type))
                return fileTypeEnum;
        return null;
    }
    public Integer getMaxWidth() {
        return maxWidth;
    }
    public String getDescription() {
        return description;
    }

}
