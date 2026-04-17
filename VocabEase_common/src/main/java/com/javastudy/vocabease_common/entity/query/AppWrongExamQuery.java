package com.javastudy.vocabease_common.entity.query;


/**
 * 我的错题本表参数
 */
public class AppWrongExamQuery extends BaseParam {


	/**
	 * 主键ID
	 */
	private Integer id;

	/**
	 * 用户ID
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 * 关联的试卷ID (AppExam表)
	 */
	private Integer examId;

	/**
	 * 关联的题目ID (Question表)
	 */
	private Integer questionId;
	private String[] questionIds;

	/**
	 * 答错时间
	 */
	private String wrongTime;

	private String wrongTimeStart;

	private String wrongTimeEnd;

	/**
	 * 用户当时的答案
	 */
	private String userAnswer;

	private String userAnswerFuzzy;

	/**
	 * 是否已复习 (0:未复习, 1:已复习)
	 */
	private Integer isReviewed;

	public String[] getQuestionIds() {
		return questionIds;
	}

	public void setQuestionIds(String[] questionIds) {
		this.questionIds = questionIds;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getId(){
		return this.id;
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

	public void setExamId(Integer examId){
		this.examId = examId;
	}

	public Integer getExamId(){
		return this.examId;
	}

	public void setQuestionId(Integer questionId){
		this.questionId = questionId;
	}

	public Integer getQuestionId(){
		return this.questionId;
	}

	public void setWrongTime(String wrongTime){
		this.wrongTime = wrongTime;
	}

	public String getWrongTime(){
		return this.wrongTime;
	}

	public void setWrongTimeStart(String wrongTimeStart){
		this.wrongTimeStart = wrongTimeStart;
	}

	public String getWrongTimeStart(){
		return this.wrongTimeStart;
	}
	public void setWrongTimeEnd(String wrongTimeEnd){
		this.wrongTimeEnd = wrongTimeEnd;
	}

	public String getWrongTimeEnd(){
		return this.wrongTimeEnd;
	}

	public void setUserAnswer(String userAnswer){
		this.userAnswer = userAnswer;
	}

	public String getUserAnswer(){
		return this.userAnswer;
	}

	public void setUserAnswerFuzzy(String userAnswerFuzzy){
		this.userAnswerFuzzy = userAnswerFuzzy;
	}

	public String getUserAnswerFuzzy(){
		return this.userAnswerFuzzy;
	}

	public void setIsReviewed(Integer isReviewed){
		this.isReviewed = isReviewed;
	}

	public Integer getIsReviewed(){
		return this.isReviewed;
	}

}
