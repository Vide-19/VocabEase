package com.javastudy.vocabease_common.entity.dto;

import com.javastudy.vocabease_common.entity.vo.MenuVO;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class SessionUserAdminDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 2733702841667793026L;
    private Integer userId;
    private String userName;
    private Boolean superAdmin;
    private List<MenuVO> menuList;
    private List<String> permissionCodeList;

    public List<MenuVO> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuVO> menuList) {
        this.menuList = menuList;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Boolean getSuperAdmin() {
        return superAdmin;
    }
    public void setSuperAdmin(Boolean superAdmin) {
        this.superAdmin = superAdmin;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<String> getPermissionCodeList() {
        return permissionCodeList;
    }

    public void setPermissionCodeList(List<String> permissionCodeList) {
        this.permissionCodeList = permissionCodeList;
    }
}
