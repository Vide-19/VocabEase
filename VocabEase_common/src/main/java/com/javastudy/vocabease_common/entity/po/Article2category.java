package com.javastudy.vocabease_common.entity.po;

import java.io.Serial;
import java.io.Serializable;


/**
 * 文章-分类对应表
 */
public class Article2category implements Serializable {


	@Serial
	private static final long serialVersionUID = 7694029378419906868L;
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

	@Override
	public String toString (){
		return "文章id:"+(articleId == null ? "空" : articleId)+"，分类id:"+(categoryId == null ? "空" : categoryId);
	}
}
