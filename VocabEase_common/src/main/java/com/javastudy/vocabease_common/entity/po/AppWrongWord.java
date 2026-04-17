package com.javastudy.vocabease_common.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javastudy.vocabease_common.entity.enums.DateTimePatternEnum;
import com.javastudy.vocabease_common.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * 用户错题本表
 */
public class AppWrongWord implements Serializable {

	@Serial
	private static final long serialVersionUID = 4409936290232921074L;
	/**
	 * 
	 */
	private Integer id;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 单词ID
	 */
	private Integer wordId;

	/**
	 * 错误次数
	 */
	private Integer wrongCount;

	/**
	 * 最后错误时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastWrongTime;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 最后复习时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastReviewTime;
	private Date NextReviewTime;

	private Word wordInfo;

	public Word getWordInfo() {
		return wordInfo;
	}

	public void setWordInfo(Word wordInfo) {
		this.wordInfo = wordInfo;
	}

	public Date getNextReviewTime() {
		return NextReviewTime;
	}

	public void setNextReviewTime(Date nextReviewTime) {
		NextReviewTime = nextReviewTime;
	}

	public Date getLastReviewTime() {
		return lastReviewTime;
	}

	public void setLastReviewTime(Date lastReviewTime) {
		this.lastReviewTime = lastReviewTime;
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

	public void setWordId(Integer wordId){
		this.wordId = wordId;
	}

	public Integer getWordId(){
		return this.wordId;
	}

	public void setWrongCount(Integer wrongCount){
		this.wrongCount = wrongCount;
	}

	public Integer getWrongCount(){
		return this.wrongCount;
	}

	public void setLastWrongTime(Date lastWrongTime){
		this.lastWrongTime = lastWrongTime;
	}

	public Date getLastWrongTime(){
		return this.lastWrongTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Date getCreateTime(){
		return this.createTime;
	}

	@Override
	public String toString (){
		return "id:"+(id == null ? "空" : id)+"，用户ID:"+(userId == null ? "空" : userId)+"，单词ID:"+(wordId == null ? "空" : wordId)+"，错误次数:"+(wrongCount == null ? "空" : wrongCount)+"，最后错误时间:"+(lastWrongTime == null ? "空" : DateUtil.format(lastWrongTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，createTime:"+(createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
	}
}
