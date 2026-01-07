package com.javastudy.vocabease_common.entity.po;

import com.javastudy.vocabease_common.entity.annotation.VerifyParam;

import java.io.Serializable;
import java.util.List;


/**
 * 菜单表
 */
public class Menu implements Serializable {


	/**
	 * 菜单id，自增主键
	 */
	private Integer menuId;

	/**
	 * 菜单名
	 */
	@VerifyParam(required = true, max = 32)
	private String menuName;

	/**
	 * 菜单类型
	 */
	@VerifyParam(required = true)
	private Integer menuType;

	/**
	 * 菜单地址
	 */
	private String menuUrl;

	/**
	 * 上级菜单id
	 */
	@VerifyParam(required = true)
	private Integer pId;

	/**
	 * 菜单排序
	 */
	@VerifyParam(required = true)
	private Integer sort;

	/**
	 * 权限编码
	 */
	@VerifyParam(required = true, max = 50)
	private String permissionCode;

	/**
	 * 图标
	 */
	@VerifyParam(max = 50)
	private String icon;

	private List<Menu> children;

	public List<Menu> getChildren() {
		return children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}

	public void setMenuId(Integer menuId){
		this.menuId = menuId;
	}

	public Integer getMenuId(){
		return this.menuId;
	}

	public void setMenuName(String menuName){
		this.menuName = menuName;
	}

	public String getMenuName(){
		return this.menuName;
	}

	public void setMenuType(Integer menuType){
		this.menuType = menuType;
	}

	public Integer getMenuType(){
		return this.menuType;
	}

	public void setMenuUrl(String menuUrl){
		this.menuUrl = menuUrl;
	}

	public String getMenuUrl(){
		return this.menuUrl;
	}

	public void setpId(Integer pId){
		this.pId = pId;
	}

	public Integer getpId(){
		return this.pId;
	}

	public void setSort(Integer sort){
		this.sort = sort;
	}

	public Integer getSort(){
		return this.sort;
	}

	public void setPermissionCode(String permissionCode){
		this.permissionCode = permissionCode;
	}

	public String getPermissionCode(){
		return this.permissionCode;
	}

	public void setIcon(String icon){
		this.icon = icon;
	}

	public String getIcon(){
		return this.icon;
	}

	@Override
	public String toString (){
		return "菜单id，自增主键:"+(menuId == null ? "空" : menuId)+"，菜单名:"+(menuName == null ? "空" : menuName)+"，菜单类型:"+(menuType == null ? "空" : menuType)+"，菜单地址:"+(menuUrl == null ? "空" : menuUrl)+"，上级菜单id:"+(pId == null ? "空" : pId)+"，菜单排序:"+(sort == null ? "空" : sort)+"，权限编码:"+(permissionCode == null ? "空" : permissionCode)+"，图标:"+(icon == null ? "空" : icon);
	}
}
