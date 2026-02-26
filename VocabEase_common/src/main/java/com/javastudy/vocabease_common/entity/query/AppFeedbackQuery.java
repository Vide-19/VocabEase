package com.javastudy.vocabease_common.entity.query;

import java.util.Date;


/**
 * 问题反馈表参数
 */
public class AppFeedbackQuery extends BaseParam {


	/**
	 * 反馈Id
	 */
	private Integer feedbackId;

	/**
	 * 用户Id
	 */
	private String userId;

	private String userIdFuzzy;

	/**
	 * 用户昵称
	 */
	private String nickName;

	private String nickNameFuzzy;

	/**
	 * 反馈内容
	 */
	private String content;

	private String contentFuzzy;

	/**
	 * 创建时间
	 */
	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * 父反馈id
	 */
	private Integer pFeedbackId;

	/**
	 * 状态 0未回复 1已回复
	 */
	private Integer status;

	/**
	 * 类型 0用户 1管理
	 */
	private Integer sendType;

	/**
	 * 最后发送时间
	 */
	private String lastSendTime;

	private String lastSendTimeStart;

	private String lastSendTimeEnd;


	public void setFeedbackId(Integer feedbackId){
		this.feedbackId = feedbackId;
	}

	public Integer getFeedbackId(){
		return this.feedbackId;
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

	public void setNickName(String nickName){
		this.nickName = nickName;
	}

	public String getNickName(){
		return this.nickName;
	}

	public void setNickNameFuzzy(String nickNameFuzzy){
		this.nickNameFuzzy = nickNameFuzzy;
	}

	public String getNickNameFuzzy(){
		return this.nickNameFuzzy;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return this.content;
	}

	public void setContentFuzzy(String contentFuzzy){
		this.contentFuzzy = contentFuzzy;
	}

	public String getContentFuzzy(){
		return this.contentFuzzy;
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

	public void setpFeedbackId(Integer pFeedbackId){
		this.pFeedbackId = pFeedbackId;
	}

	public Integer getpFeedbackId(){
		return this.pFeedbackId;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setSendType(Integer sendType){
		this.sendType = sendType;
	}

	public Integer getSendType(){
		return this.sendType;
	}

	public void setLastSendTime(String lastSendTime){
		this.lastSendTime = lastSendTime;
	}

	public String getLastSendTime(){
		return this.lastSendTime;
	}

	public void setLastSendTimeStart(String lastSendTimeStart){
		this.lastSendTimeStart = lastSendTimeStart;
	}

	public String getLastSendTimeStart(){
		return this.lastSendTimeStart;
	}
	public void setLastSendTimeEnd(String lastSendTimeEnd){
		this.lastSendTimeEnd = lastSendTimeEnd;
	}

	public String getLastSendTimeEnd(){
		return this.lastSendTimeEnd;
	}

}
