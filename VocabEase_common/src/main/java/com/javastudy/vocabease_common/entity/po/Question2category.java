package com.javastudy.vocabease_common.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;


/**
 * 问题-分类对应表
 */
public class Question2category implements Serializable {


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

	@Override
	public String toString (){
		return "问题id:"+(questionId == null ? "空" : questionId)+"，分类id:"+(categoryId == null ? "空" : categoryId);
	}
}
