package com.javastudy.vocabease_common.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.enums.DateTimePatternEnum;
import com.javastudy.vocabease_common.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 问题表
 */
public class Question implements Serializable {


	@Serial
	private static final long serialVersionUID = -8044696147942209378L;

	/**
	 * 问题id
	 */
	private Integer questionId;

	/**
	 * 标题
	 */
	@VerifyParam(required = true)
	private String title;

	/**
	 * 问题描述
	 */
	private String question;

	/**
	 * 答案
	 */
	@VerifyParam(required = true)
	private String answer;

	/**
	 * 答案解释
	 */
	@VerifyParam(required = true)
	private String answerAnalysis;

	/**
	 * 等级
	 */
	@VerifyParam(required = true)
	private Integer level;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 状态 0未发布 1已发布
	 */
	private Integer status;

	/**
	 * 作者id
	 */
	private String createrId;

	/**
	 * 浏览量
	 */
	private Integer readCount;

	/**
	 * 收藏数
	 */
	private Integer collectCount;

	private Integer collectId;

	/**
	 * 发布类别 0内部 1外部
	 */
	private Integer postType;

	/**
	 * 问题类型 0判断 1单选 2多选 3填空
	 */
	@VerifyParam(required = true)
	private Integer questionType;

	List<Item4question> itemList;

	private String questionItemList;

	private String categoryName;

	private Integer categoryId;

	public String getQuestionItemList() {
		return questionItemList;
	}

	public void setQuestionItemList(String questionItemList) {
		this.questionItemList = questionItemList;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<Item4question> getItemList() {return itemList;}

	public void setItemList(List<Item4question> itemList) {this.itemList = itemList;}

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

	public void setQuestion(String question){
		this.question = question;
	}

	public String getQuestion(){
		return this.question;
	}

	public void setAnswerAnalysis(String answerAnalysis){
		this.answerAnalysis = answerAnalysis;
	}

	public String getAnswerAnalysis(){
		return this.answerAnalysis;
	}

	public void setLevel(Integer level){
		this.level = level;
	}

	public Integer getLevel(){
		return this.level;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Date getCreateTime(){
		return this.createTime;
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

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Integer getCollectId() {
		return collectId;
	}

	public void setCollectId(Integer collectId) {
		this.collectId = collectId;
	}

	@Override
	public String toString (){
		return "问题id:"+(questionId == null ? "空" : questionId)+"，标题:"+(title == null ? "空" : title)+"，问题描述:"+(question == null ? "空" : question)+"，答案解释:"+(answerAnalysis == null ? "空" : answerAnalysis)+"，等级:"+(level == null ? "空" : level)+"，创建时间:"+(createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，状态 0未发布 1已发布:"+(status == null ? "空" : status)+"，作者id:"+(createrId == null ? "空" : createrId)+"，浏览量:"+(readCount == null ? "空" : readCount)+"，收藏数:"+(collectCount == null ? "空" : collectCount)+"，发布类别 0内部 1外部:"+(postType == null ? "空" : postType)+"，问题类型 0判断 1单选 2多选 3填空:"+(questionType == null ? "空" : questionType);
	}
}
