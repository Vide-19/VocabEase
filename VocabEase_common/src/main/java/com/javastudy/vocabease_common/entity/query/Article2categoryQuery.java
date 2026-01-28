package com.javastudy.vocabease_common.entity.query;



/**
 * 文章-分类对应表参数
 */
public class Article2categoryQuery extends BaseParam {


	/**
	 * 文章id
	 */
	private Integer articleId;

	/**
	 * 分类id
	 */
	private Integer categoryId;


	public void setArticleId(Integer articleId){
		this.articleId = articleId;
	}

	public Integer getArticleId(){
		return this.articleId;
	}

	public void setCategoryId(Integer categoryId){
		this.categoryId = categoryId;
	}

	public Integer getCategoryId(){
		return this.categoryId;
	}

}
