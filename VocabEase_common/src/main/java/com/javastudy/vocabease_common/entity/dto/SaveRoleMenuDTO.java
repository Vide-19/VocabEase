package com.javastudy.vocabease_common.entity.dto;

import com.javastudy.vocabease_common.entity.annotation.VerifyParam;

public class SaveRoleMenuDTO {
    @VerifyParam(required = true)
    private Integer roleId;

    @VerifyParam(required = true)
    private String menuIds;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(String menuIds) {
        this.menuIds = menuIds;
    }
}
