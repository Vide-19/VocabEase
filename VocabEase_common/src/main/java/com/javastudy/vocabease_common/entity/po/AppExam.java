package com.javastudy.vocabease_common.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javastudy.vocabease_common.entity.enums.DateTimePatternEnum;
import com.javastudy.vocabease_common.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * 测试表
 */
public class AppExam implements Serializable {


	@Serial
	private static final long serialVersionUID = 8608319081972671910L;
	/**
	 * 测试id
	 */
	private Integer examId;

	/**
	 * 用户id
	 */
	private String userId;

	/**
	 * 用户昵称
	 */
	private String nickName;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 开始时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	/**
	 * 结束时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;

	/**
	 * 状态 0未作答 1已作答
	 */
	private Integer status;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 考试分数
	 */
	private Integer score; // 新增字段

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
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

	public void setNickName(String nickName){
		this.nickName = nickName;
	}

	public String getNickName(){
		return this.nickName;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Date getCreateTime(){
		return this.createTime;
	}

	public void setStartTime(Date startTime){
		this.startTime = startTime;
	}

	public Date getStartTime(){
		return this.startTime;
	}

	public void setEndTime(Date endTime){
		this.endTime = endTime;
	}

	public Date getEndTime(){
		return this.endTime;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getRemark(){
		return this.remark;
	}

	@Override
	public String toString (){
		return "测试id:"+(examId == null ? "空" : examId)+"，用户id:"+(userId == null ? "空" : userId)+"，用户昵称:"+(nickName == null ? "空" : nickName)+"，创建时间:"+(createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，开始时间:"+(startTime == null ? "空" : DateUtil.format(startTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，结束时间:"+(endTime == null ? "空" : DateUtil.format(endTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，状态 0未作答 1已作答:"+(status == null ? "空" : status)+"，备注:"+(remark == null ? "空" : remark);
	}
}
