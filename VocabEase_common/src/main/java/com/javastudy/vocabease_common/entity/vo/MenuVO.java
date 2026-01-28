package com.javastudy.vocabease_common.entity.vo;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class MenuVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 4213846188019786062L;
    /**
     * 菜单名
     */
    private String menuName;
    /**
     * 菜单地址
     */
    private String menuUrl;
    /**
     * 图标
     */
    private String icon;

    private List<MenuVO> children;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<MenuVO> getChildren() {
        return children;
    }

    public void setChildren(List<MenuVO> children) {
        this.children = children;
    }
}
