package com.javastudy.vocabease_common.entity.query;



/**
 * 问题-分类对应表参数
 */
public class Question2categoryQuery extends BaseParam {


	/**
	 * 问题id
	 */
	private Integer questionId;

	/**
	 * 分类id
	 */
	private Integer categoryId;


	public void setQuestionId(Integer questionId){
		this.questionId = questionId;
	}

	public Integer getQuestionId(){
		return this.questionId;
	}

	public void setCategoryId(Integer categoryId){
		this.categoryId = categoryId;
	}

	public Integer getCategoryId(){
		return this.categoryId;
	}

}
