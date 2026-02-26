package com.javastudy.vocabease_common.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javastudy.vocabease_common.entity.enums.DateTimePatternEnum;
import com.javastudy.vocabease_common.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * 用户收藏表
 */
public class AppCollect implements Serializable {


	@Serial
	private static final long serialVersionUID = 1855724618280338288L;
	/**
	 * 收藏id
	 */
	private Integer collectId;

	/**
	 * 用户id
	 */
	private String userId;

	/**
	 * 对象id
	 */
	private String objectId;

	/**
	 * 类型 0分享收藏 1单词收藏 2文章收藏 3问题收藏
	 */
	private Integer collectType;

	/**
	 * 收藏时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date collectTime;


	public void setCollectId(Integer collectId){
		this.collectId = collectId;
	}

	public Integer getCollectId(){
		return this.collectId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setObjectId(String objectId){
		this.objectId = objectId;
	}

	public String getObjectId(){
		return this.objectId;
	}

	public void setCollectType(Integer collectType){
		this.collectType = collectType;
	}

	public Integer getCollectType(){
		return this.collectType;
	}

	public void setCollectTime(Date collectTime){
		this.collectTime = collectTime;
	}

	public Date getCollectTime(){
		return this.collectTime;
	}

	@Override
	public String toString (){
		return "收藏id:"+(collectId == null ? "空" : collectId)+"，用户id:"+(userId == null ? "空" : userId)+"，对象id:"+(objectId == null ? "空" : objectId)+"，类型 0分享收藏 1单词收藏 2文章收藏 3问题收藏:"+(collectType == null ? "空" : collectType)+"，收藏时间:"+(collectTime == null ? "空" : DateUtil.format(collectTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
	}
}
