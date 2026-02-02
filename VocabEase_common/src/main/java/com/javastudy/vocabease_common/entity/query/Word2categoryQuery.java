package com.javastudy.vocabease_common.entity.query;



/**
 * 单词-分类对应表参数
 */
public class Word2categoryQuery extends BaseParam {


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

}
