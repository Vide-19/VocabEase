package com.javastudy.vocabease_common.entity.enums;

public enum AppUpdateTypeEnum {
    ALL(0, ".apk", "全更新"),
    WGT(1, ".wgt", "局部热更新");

    private Integer type;
    private String suffix;
    private String desc;

    AppUpdateTypeEnum(int type, String suffix, String desc) {
        this.type = type;
        this.suffix = suffix;
        this.desc = desc;
    }
    public Integer getType() {
        return type;
    }
    public String getSuffix() {
        return suffix;
    }
    public String getDesc() {
        return desc;
    }
    public static AppUpdateTypeEnum getEnumByType(Integer type) {
        for (AppUpdateTypeEnum e : AppUpdateTypeEnum.values())
            if (e.getType().equals(type))
                return e;
        return null;
    }
}
