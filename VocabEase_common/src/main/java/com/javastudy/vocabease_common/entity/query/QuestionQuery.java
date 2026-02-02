package com.javastudy.vocabease_common.entity.query;

/**
 * 问题表参数
 */
public class QuestionQuery extends BaseParam {

	/**
	 * 问题id
	 */
	private Integer questionId;

	private String[] questionIds;

	private Integer currentId;

	private Integer nextType;

	/**
	 * 标题
	 */
	private String title;

	private String titleFuzzy;

	/**
	 * 问题描述
	 */
	private String question;

	private String questionFuzzy;

	/**
	 * 答案
	 */
	private String answer;

	/**
	 * 答案解释
	 */
	private String answerAnalysis;

	private String answerAnalysisFuzzy;

	private Boolean isQueryAnswer;

	/**
	 * 等级
	 */
	private Integer level;

	/**
	 * 创建时间
	 */
	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * 状态 0未发布 1已发布
	 */
	private Integer status;

	/**
	 * 作者id
	 */
	private String createrId;

	private String createrIdFuzzy;

	/**
	 * 浏览量
	 */
	private Integer readCount;

	/**
	 * 收藏数
	 */
	private Integer collectCount;

	/**
	 * 发布类别 0内部 1外部
	 */
	private Integer postType;

	/**
	 * 问题类型 0判断 1单选 2多选 3填空
	 */
	private Integer questionType;


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

	public void setTitleFuzzy(String titleFuzzy){
		this.titleFuzzy = titleFuzzy;
	}

	public String getTitleFuzzy(){
		return this.titleFuzzy;
	}

	public void setQuestion(String question){
		this.question = question;
	}

	public String getQuestion(){
		return this.question;
	}

	public void setQuestionFuzzy(String questionFuzzy){
		this.questionFuzzy = questionFuzzy;
	}

	public String getQuestionFuzzy(){
		return this.questionFuzzy;
	}

	public void setAnswerAnalysis(String answerAnalysis){
		this.answerAnalysis = answerAnalysis;
	}

	public String getAnswerAnalysis(){
		return this.answerAnalysis;
	}

	public void setAnswerAnalysisFuzzy(String answerAnalysisFuzzy){
		this.answerAnalysisFuzzy = answerAnalysisFuzzy;
	}

	public String getAnswerAnalysisFuzzy(){
		return this.answerAnalysisFuzzy;
	}

	public void setLevel(Integer level){
		this.level = level;
	}

	public Integer getLevel(){
		return this.level;
	}

	public void setCreateTime(String createTime){
		this.createTime = createTime;
	}

	public String getCreateTime(){
		return this.createTime;
	}

	public void setCreateTimeStart(String createTimeStart){
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeStart(){
		return this.createTimeStart;
	}
	public void setCreateTimeEnd(String createTimeEnd){
		this.createTimeEnd = createTimeEnd;
	}

	public String getCreateTimeEnd(){
		return this.createTimeEnd;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setCreaterId(String createrId){
		this.createrId = createrId;
	}

	public String getCreaterId(){
		return this.createrId;
	}

	public void setCreaterIdFuzzy(String createrIdFuzzy){
		this.createrIdFuzzy = createrIdFuzzy;
	}

	public String getCreaterIdFuzzy(){
		return this.createrIdFuzzy;
	}

	public void setReadCount(Integer readCount){
		this.readCount = readCount;
	}

	public Integer getReadCount(){
		return this.readCount;
	}

	public void setCollectCount(Integer collectCount){
		this.collectCount = collectCount;
	}

	public Integer getCollectCount(){
		return this.collectCount;
	}

	public void setPostType(Integer postType){
		this.postType = postType;
	}

	public Integer getPostType(){
		return this.postType;
	}

	public void setQuestionType(Integer questionType){
		this.questionType = questionType;
	}

	public Integer getQuestionType(){
		return this.questionType;
	}

	public Boolean getQueryAnswer() {return isQueryAnswer;}

	public void setQueryAnswer(Boolean queryAnswer) {isQueryAnswer = queryAnswer;}

	public String[] getQuestionIds() {return questionIds;}

	public void setQuestionIds(String[] questionIds) {this.questionIds = questionIds;}

	public Integer getCurrentId() {
		return currentId;
	}

	public void setCurrentId(Integer currentId) {
		this.currentId = currentId;
	}

	public Integer getNextType() {
		return nextType;
	}

	public void setNextType(Integer nextType) {
		this.nextType = nextType;
	}
}
