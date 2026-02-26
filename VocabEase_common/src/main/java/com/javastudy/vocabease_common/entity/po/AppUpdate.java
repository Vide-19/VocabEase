package com.javastudy.vocabease_common.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javastudy.vocabease_common.entity.enums.DateTimePatternEnum;
import com.javastudy.vocabease_common.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * 小程序发布表
 */
public class AppUpdate implements Serializable {


	@Serial
	private static final long serialVersionUID = 6504803140014224523L;
	/**
	 * id
	 */
	private Integer id;

	/**
	 * 版本号
	 */
	private String version;

	/**
	 * 更新描述
	 */
	private String updateDesc;

	/**
	 * 更新类型 0全更新 1局部热更新
	 */
	private Integer updateType;

	/**
	 * 状态 0未发布 1灰度发布 2全网发布
	 */
	private Integer status;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 灰度设备id
	 */
	private String grayscaleDevice;

	private String[] updateDescArray;

	public String[] getUpdateDescArray() {
		if (!updateDesc.isEmpty())
			return updateDesc.split("\\|");
		return updateDescArray;
	}

	public void setUpdateDescArray(String[] updateDescArray) {
		this.updateDescArray = updateDescArray;
	}

	public void setId(Integer id){
		this.id = id;
	}

	public Integer getId(){
		return this.id;
	}

	public void setVersion(String version){
		this.version = version;
	}

	public String getVersion(){
		return this.version;
	}

	public void setUpdateDesc(String updateDesc){
		this.updateDesc = updateDesc;
	}

	public String getUpdateDesc(){
		return this.updateDesc;
	}

	public void setUpdateType(Integer updateType){
		this.updateType = updateType;
	}

	public Integer getUpdateType(){
		return this.updateType;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Date getCreateTime(){
		return this.createTime;
	}

	public void setGrayscaleDevice(String grayscaleDevice){
		this.grayscaleDevice = grayscaleDevice;
	}

	public String getGrayscaleDevice(){
		return this.grayscaleDevice;
	}

	@Override
	public String toString (){
		return "id:"+(id == null ? "空" : id)+"，版本号:"+(version == null ? "空" : version)+"，更新描述:"+(updateDesc == null ? "空" : updateDesc)+"，更新类型 0全更新 1局部热更新:"+(updateType == null ? "空" : updateType)+"，状态 0未发布 1灰度发布 2全网发布:"+(status == null ? "空" : status)+"，创建时间:"+(createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，灰度设备id:"+(grayscaleDevice == null ? "空" : grayscaleDevice);
	}
}
