package com.javastudy.vocabease_common.entity.query;


import java.util.List;

/**
 * 测试问题表参数
 */
public class AppQuestion4examQuery extends BaseParam {

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

	private String userIdFuzzy;

	/**
	 * 问题id
	 */
	private Integer questionId;

	private List<String> questionIds;

	/**
	 * 作答内容
	 */
	private String answer;

	private String answerFuzzy;

	private Boolean isShowAnswer;

	/**
	 * 判断 0未作答 1正确 2错误
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

	public void setUserIdFuzzy(String userIdFuzzy){
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy(){
		return this.userIdFuzzy;
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

	public void setAnswerFuzzy(String answerFuzzy){
		this.answerFuzzy = answerFuzzy;
	}

	public String getAnswerFuzzy(){
		return this.answerFuzzy;
	}

	public void setResult(Integer result){
		this.result = result;
	}

	public Integer getResult(){
		return this.result;
	}

	public Boolean getShowAnswer() {
		return isShowAnswer;
	}

	public void setShowAnswer(Boolean showAnswer) {
		isShowAnswer = showAnswer;
	}

	public List<String> getQuestionIds() {
		return questionIds;
	}

	public void setQuestionIds(List<String> questionIds) {
		this.questionIds = questionIds;
	}
}
