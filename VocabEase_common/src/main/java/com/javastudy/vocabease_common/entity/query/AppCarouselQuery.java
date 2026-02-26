package com.javastudy.vocabease_common.entity.query;



/**
 * 小程序轮播图表参数
 */
public class AppCarouselQuery extends BaseParam {


	/**
	 * 轮播图id
	 */
	private Integer carouselId;

	/**
	 * 图片地址
	 */
	private String imagPath;

	private String imagPathFuzzy;

	/**
	 * 对象类型 0单词 1文章 2问题 3笔记 4外部
	 */
	private Integer objectType;

	/**
	 * 对象id
	 */
	private String objectId;

	private String objectIdFuzzy;

	/**
	 * 外部链接
	 */
	private String outerLink;

	private String outerLinkFuzzy;

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

	public void setImagPathFuzzy(String imagPathFuzzy){
		this.imagPathFuzzy = imagPathFuzzy;
	}

	public String getImagPathFuzzy(){
		return this.imagPathFuzzy;
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

	public void setObjectIdFuzzy(String objectIdFuzzy){
		this.objectIdFuzzy = objectIdFuzzy;
	}

	public String getObjectIdFuzzy(){
		return this.objectIdFuzzy;
	}

	public void setOuterLink(String outerLink){
		this.outerLink = outerLink;
	}

	public String getOuterLink(){
		return this.outerLink;
	}

	public void setOuterLinkFuzzy(String outerLinkFuzzy){
		this.outerLinkFuzzy = outerLinkFuzzy;
	}

	public String getOuterLinkFuzzy(){
		return this.outerLinkFuzzy;
	}

	public void setSort(Integer sort){
		this.sort = sort;
	}

	public Integer getSort(){
		return this.sort;
	}

}
