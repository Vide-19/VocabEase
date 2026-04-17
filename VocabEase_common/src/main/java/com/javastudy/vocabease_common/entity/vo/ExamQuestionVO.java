package com.javastudy.vocabease_common.entity.vo;

import com.javastudy.vocabease_common.entity.po.Item4question;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


/**
 * 问题表
 */
public class ExamQuestionVO implements Serializable {


	@Serial
	private static final long serialVersionUID = -8044696147942209378L;

	private Integer id;

	/**
	 * 测试id
	 */
	private Integer examId;

	/**
	 * 问题id
	 */
	private Integer questionId;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 等级
	 */
	private Integer level;

	/**
	 * 问题类型 0判断 1单选 2多选 3填空
	 */
	private Integer questionType;

	/**
	 * 问题描述
	 */
	private String question;

	/**
	 * 答案
	 */
	private String answer;

	/**
	 * 答案解释
	 */
	private String answerAnalysis;

	/**
	 * 回答
	 */
	private String userAnswer;

	/**
	 * 结果判断
	 */
	private Integer result;

	private List<Item4question> itemList;

	private Boolean isCollect;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getExamId() {
		return examId;
	}

	public void setExamId(Integer examId) {
		this.examId = examId;
	}

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getQuestionType() {
		return questionType;
	}

	public void setQuestionType(Integer questionType) {
		this.questionType = questionType;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getAnswerAnalysis() {
		return answerAnalysis;
	}

	public void setAnswerAnalysis(String answerAnalysis) {
		this.answerAnalysis = answerAnalysis;
	}

	public String getUserAnswer() {
		return userAnswer;
	}

	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public List<Item4question> getItemList() {
		return itemList;
	}

	public void setItemList(List<Item4question> itemList) {
		this.itemList = itemList;
	}

	public Boolean getCollect() {
		return isCollect;
	}

	public void setCollect(Boolean collect) {
		isCollect = collect;
	}
}
