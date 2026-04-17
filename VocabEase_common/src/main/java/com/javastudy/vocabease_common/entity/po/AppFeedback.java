package com.javastudy.vocabease_common.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javastudy.vocabease_common.entity.enums.DateTimePatternEnum;
import com.javastudy.vocabease_common.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * 问题反馈表
 */
public class AppFeedback implements Serializable {


	@Serial
	private static final long serialVersionUID = 1988407038008608720L;
	/**
	 * 反馈Id
	 */
	private Integer feedbackId;

	/**
	 * 用户Id
	 */
	private String userId;

	/**
	 * 用户昵称
	 */
	private String nickName;

	/**
	 * 反馈内容
	 */
	private String content;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 父反馈id
	 */
	private Integer pFeedbackId;

	/**
	 * 状态 2未回复 3已回复
	 */
	private Integer status;

	/**
	 * 类型 0用户 1管理
	 */
	private Integer sendType;

	/**
	 * 最后发送时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastSendTime;


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

	public void setNickName(String nickName){
		this.nickName = nickName;
	}

	public String getNickName(){
		return this.nickName;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return this.content;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Date getCreateTime(){
		return this.createTime;
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

	public void setLastSendTime(Date lastSendTime){
		this.lastSendTime = lastSendTime;
	}

	public Date getLastSendTime(){
		return this.lastSendTime;
	}

	@Override
	public String toString (){
		return "反馈Id:"+(feedbackId == null ? "空" : feedbackId)+"，用户Id:"+(userId == null ? "空" : userId)+"，用户昵称:"+(nickName == null ? "空" : nickName)+"，反馈内容:"+(content == null ? "空" : content)+"，创建时间:"+(createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，父反馈id:"+(pFeedbackId == null ? "空" : pFeedbackId)+"，状态 0未回复 1已回复:"+(status == null ? "空" : status)+"，类型 0用户 1管理:"+(sendType == null ? "空" : sendType)+"，最后发送时间:"+(lastSendTime == null ? "空" : DateUtil.format(lastSendTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
	}
}
