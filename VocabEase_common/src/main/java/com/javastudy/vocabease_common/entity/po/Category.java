package com.javastudy.vocabease_common.entity.po;

import com.javastudy.vocabease_common.entity.annotation.VerifyParam;

import java.io.Serializable;


/**
 * 分类表
 */
public class Category implements Serializable {


	/**
	 * 分类id
	 */
	private Integer categoryId;

	/**
	 * 名称
	 */
	@VerifyParam(required = true)
	private String categoryName;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 图标
	 */
	private String iconPath;

	/**
	 * 背景颜色
	 */
	private String bgColor;

	/**
	 * 0文章 1考题
	 */
	@VerifyParam(required = true)
	private Integer type;

	public void setCategoryId(Integer categoryId){
		this.categoryId = categoryId;
	}

	public Integer getCategoryId(){
		return this.categoryId;
	}

	public void setCategoryName(String categoryName){
		this.categoryName = categoryName;
	}

	public String getCategoryName(){
		return this.categoryName;
	}

	public void setSort(Integer sort){
		this.sort = sort;
	}

	public Integer getSort(){
		return this.sort;
	}

	public void setIconPath(String iconPath){
		this.iconPath = iconPath;
	}

	public String getIconPath(){
		return this.iconPath;
	}

	public void setBgColor(String bgColor){
		this.bgColor = bgColor;
	}

	public String getBgColor(){
		return this.bgColor;
	}

	public void setType(Integer type){
		this.type = type;
	}

	public Integer getType(){
		return this.type;
	}

	@Override
	public String toString (){
		return "分类id:"+(categoryId == null ? "空" : categoryId)+"，名称:"+(categoryName == null ? "空" : categoryName)+"，排序:"+(sort == null ? "空" : sort)+"，图标:"+(iconPath == null ? "空" : iconPath)+"，背景颜色:"+(bgColor == null ? "空" : bgColor)+"，0问题 1考题 2问题考题:"+(type == null ? "空" : type);
	}
}
