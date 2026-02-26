package com.javastudy.vocabease_common.entity.po;

import java.io.Serial;
import java.io.Serializable;


/**
 * 测试问题表
 */
public class AppQuestion4exam implements Serializable {


	@Serial
	private static final long serialVersionUID = 1026476912805127264L;
	/**
	 * id
	 */
	private Integer id;

	/**
	 * 测试id
	 */
	private Integer examId;

	/**
	 * 用户id
	 */
	private String userId;

	/**
	 * 问题id
	 */
	private Integer questionId;

	/**
	 * 作答内容
	 */
	private String answer;

	/**
	 * 判断 0未作答 2正确 3错误
	 */
	private Integer result;


	public void setId(Integer id){
		this.id = id;
	}

	public Integer getId(){
		return this.id;
	}

	public void setExamId(Integer examId){
		this.examId = examId;
	}

	public Integer getExamId(){
		return this.examId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setQuestionId(Integer questionId){
		this.questionId = questionId;
	}

	public Integer getQuestionId(){
		return this.questionId;
	}

	public void setAnswer(String answer){
		this.answer = answer;
	}

	public String getAnswer(){
		return this.answer;
	}

	public void setResult(Integer result){
		this.result = result;
	}

	public Integer getResult(){
		return this.result;
	}

	@Override
	public String toString (){
		return "id:"+(id == null ? "空" : id)+"，测试id:"+(examId == null ? "空" : examId)+"，用户id:"+(userId == null ? "空" : userId)+"，问题id:"+(questionId == null ? "空" : questionId)+"，作答内容:"+(answer == null ? "空" : answer)+"，判断 0未作答 1正确 2错误:"+(result == null ? "空" : result);
	}
}
