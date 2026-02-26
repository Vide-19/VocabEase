package com.javastudy.vocabease_common.entity.po;

import java.io.Serial;
import java.io.Serializable;


/**
 * 小程序轮播图表
 */
public class AppCarousel implements Serializable {


	@Serial
	private static final long serialVersionUID = 2500420172601058928L;
	/**
	 * 轮播图id
	 */
	private Integer carouselId;

	/**
	 * 图片地址
	 */
	private String imagPath;

	/**
	 * 对象类型 0单词 1文章 2问题 3笔记 4外部
	 */
	private Integer objectType;

	/**
	 * 对象id
	 */
	private String objectId;

	/**
	 * 外部链接
	 */
	private String outerLink;

	/**
	 * 排序
	 */
	private Integer sort;


	public void setCarouselId(Integer carouselId){
		this.carouselId = carouselId;
	}

	public Integer getCarouselId(){
		return this.carouselId;
	}

	public void setImagPath(String imagPath){
		this.imagPath = imagPath;
	}

	public String getImagPath(){
		return this.imagPath;
	}

	public void setObjectType(Integer objectType){
		this.objectType = objectType;
	}

	public Integer getObjectType(){
		return this.objectType;
	}

	public void setObjectId(String objectId){
		this.objectId = objectId;
	}

	public String getObjectId(){
		return this.objectId;
	}

	public void setOuterLink(String outerLink){
		this.outerLink = outerLink;
	}

	public String getOuterLink(){
		return this.outerLink;
	}

	public void setSort(Integer sort){
		this.sort = sort;
	}

	public Integer getSort(){
		return this.sort;
	}

	@Override
	public String toString (){
		return "轮播图id:"+(carouselId == null ? "空" : carouselId)+"，图片地址:"+(imagPath == null ? "空" : imagPath)+"，对象类型 0单词 1文章 2问题 3笔记 4外部:"+(objectType == null ? "空" : objectType)+"，对象id:"+(objectId == null ? "空" : objectId)+"，外部链接:"+(outerLink == null ? "空" : outerLink)+"，排序:"+(sort == null ? "空" : sort);
	}
}
