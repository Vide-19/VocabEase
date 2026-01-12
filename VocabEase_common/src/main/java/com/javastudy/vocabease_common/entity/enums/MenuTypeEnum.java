package com.javastudy.vocabease_common.entity.enums;

public enum MenuTypeEnum {
    MENU(0, "菜单"),
    BUTTON(1, "按钮");

    private Integer typeCode;
    private String typeDescribe;

    MenuTypeEnum(Integer typeCode, String typeDescribe) {
        this.typeCode = typeCode;
    }

    public Integer getTypeCode() {return typeCode;}

    public void setTypeCode(Integer typeCode) {this.typeCode = typeCode;}

    public String getTypeDescribe() {return typeDescribe;}

    public void setTypeDescribe(String typeDescribe) {this.typeDescribe = typeDescribe;}

    public static MenuTypeEnum getMenuTypeEnumByTypeCode(Integer typeCode) {
        if (typeCode == null)
            return null;
        for (MenuTypeEnum menuTypeEnum : MenuTypeEnum.values())
            if (menuTypeEnum.getTypeCode().equals(typeCode))
                return menuTypeEnum;
        return null;
    }
}
