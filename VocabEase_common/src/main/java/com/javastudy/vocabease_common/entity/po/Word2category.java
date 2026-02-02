package com.javastudy.vocabease_common.entity.po;

import java.io.Serial;
import java.io.Serializable;


/**
 * 单词-分类对应表
 */
public class Word2category implements Serializable {


	@Serial
	private static final long serialVersionUID = 223293464169101737L;
	/**
	 * 单词id
	 */
	private Integer wordId;

	/**
	 * 分类id
	 */
	private Integer categoryId;


	public void setWordId(Integer wordId){
		this.wordId = wordId;
	}

	public Integer getWordId(){
		return this.wordId;
	}

	public void setCategoryId(Integer categoryId){
		this.categoryId = categoryId;
	}

	public Integer getCategoryId(){
		return this.categoryId;
	}

	@Override
	public String toString (){
		return "单词id:"+(wordId == null ? "空" : wordId)+"，分类id:"+(categoryId == null ? "空" : categoryId);
	}
}
