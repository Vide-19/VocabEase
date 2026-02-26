package com.javastudy.vocabease_common.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;


/**
 * 测试表
 */
public class AppExamVO implements Serializable {


	@Serial
	private static final long serialVersionUID = 8608319081972671910L;
	/**
	 * 测试id
	 */
	private Integer examId;

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
	 * 用时
	 */
	private BigDecimal useTime;

	private List<ExamQuestionVO> examQuestionList;

	public List<ExamQuestionVO> getExamQuestionList() {
		return examQuestionList;
	}

	public void setExamQuestionList(List<ExamQuestionVO> examQuestionList) {
		this.examQuestionList = examQuestionList;
	}

	public BigDecimal getUseTime() {
		if (startTime != null && endTime != null)
			return new BigDecimal(endTime.getTime() - startTime.getTime()).
					divide(new BigDecimal(1000 * 60), 2, RoundingMode.HALF_UP);
		return new BigDecimal(0);
	}


	public void setExamId(Integer examId){
		this.examId = examId;
	}

	public Integer getExamId(){
		return this.examId;
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

}
