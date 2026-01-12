package com.javastudy.vocabease_common.entity.enums;

/**
 * 菜单权限码-枚举
 */
public enum PermissionCodeEnum {
    NO_PERMISSION("no_permission", "无需校验"),
    HOME("home", "首页"),
    SETTINGS("settings", "设置"),

    SETTINGS_MENU("settings_menu", "菜单设置"),
    SETTINGS_MENU_EDIT("settings_menu_edit", "新增/修改"),
    SETTINGS_MENU_DELETE("settings_menu_delete", "删除"),

    SETTINGS_ROLE_LIST("settings_role_list", "角色列表"),
    SETTINGS_ROLE_EDIT("settings_role_edit", "新增/修改"),
    SETTINGS_ROLE_DELETE("settings_role_delete", "删除"),

    SETTINGS_ACCOUNT_LIST("settings_account_list", "用户列表"),
    SETTINGS_ACCOUNT_EDIT("settings_account_edit", "新增/修改"),
    SETTINGS_ACCOUNT_DELETE("settings_account_delete", "删除"),
    SETTINGS_ACCOUNT_UPDATE_PASSWORD("settings_account_update_password", "修改密码"),
    SETTINGS_ACCOUNT_STATUS("settings_account_status", "启用/禁用"),

    CONTENT("content", "内容管理"),

    CATEGORY_LIST("category_list", "分类列表"),
    CATEGORY_EDIT("category_edit", "新增/修改/删除"),
    CATEGORY_DELETE("category_delete", "删除"),

    QUESTION_LIST("question_list", "问题列表"),
    QUESTION_EDIT("question_edit", "新增/修改"),
    QUESTION_IMPORT("question_import", "导入"),
    QUESTION_POST("question_post", "发布"),
    QUESTION_DELETE("question_delete", "取消"),
    QUESTION_DELETE_BATCH("question_delete_batch", "批量取消"),

    EXAM_LIST("exam_list", "测试列表"),
    EXAM_EDIT("exam_edit", "新增/修改"),
    EXAM_IMPORT("exam_import", "导入"),
    EXAM_POST("exam_post", "发布"),
    EXAM_DELETE("exam_delete", "取消"),
    EXAM_DELETE_BATCH("exam_delete_batch", "批量取消"),

    SHARE_LIST("share_list", "笔记列表"),
    SHARE_EDIT("share_edit", "新增/修改"),
    SHARE_POST("share_post", "发布"),
    SHARE_DELETE("share_delete", "取消"),
    SHARE_DELETE_BATCH("share_delete_batch", "批量取消"),

    APP_UPDATE_LIST("app_update_list", "应用发布列表"),
    APP_UPDATE_EDIT("app_update_edit", "应用发布新增/修改/删除"),
    APP_UPDATE_POST("app_update_post", "应用发布"),

    APP_CAROUSEL_LIST("app_carousel_list", "轮播图列表"),
    APP_CAROUSEL_EDIT("app_carousel_edit", "轮播图新增/修改/删除"),

    APP_USER_LIST("app_user_list", "应用用户列表"),
    APP_USER_EDIT("app_user_edit", "应用用户新增/修改/删除"),
    APP_USER_DEVICE("app_user_device", "应用用户设备");

    private String code;
    private String desc;

    private PermissionCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {return desc;}

    public void setDesc(String desc) {this.desc = desc;}

    public String getCode() {return code;}

    public void setCode(String code) {this.code = code;}

}
