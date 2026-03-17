package com.javastudy.vocabease_common.entity.po;

import java.io.Serial;
import java.io.Serializable;


/**
 * 问题选项表
 */
public class Item4question implements Serializable {


	@Serial
	private static final long serialVersionUID = 5417284871355491456L;
	/**
	 * 选项id
	 */
	private Integer itemId;

	/**
	 * 问题id
	 */
	private Integer questionId;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 是否正确
	 */
	private Integer isCorrect;

	public Integer getIsCorrect() {
		return isCorrect;
	}

	public void setIsCorrect(Integer isCorrect) {
		this.isCorrect = isCorrect;
	}

	public void setItemId(Integer itemId){
		this.itemId = itemId;
	}

	public Integer getItemId(){
		return this.itemId;
	}

	public void setQuestionId(Integer questionId){
		this.questionId = questionId;
	}

	public Integer getQuestionId(){
		return this.questionId;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return this.title;
	}

	public void setSort(Integer sort){
		this.sort = sort;
	}

	public Integer getSort(){
		return this.sort;
	}

	@Override
	public String toString (){
		return "选项id:"+(itemId == null ? "空" : itemId)+"，问题id:"+(questionId == null ? "空" : questionId)+"，标题:"+(title == null ? "空" : title)+"，排序:"+(sort == null ? "空" : sort);
	}
}
