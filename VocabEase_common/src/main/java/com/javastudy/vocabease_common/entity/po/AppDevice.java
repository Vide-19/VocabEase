package com.javastudy.vocabease_common.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javastudy.vocabease_common.entity.enums.DateTimePatternEnum;
import com.javastudy.vocabease_common.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * 小程序设备表
 */
public class AppDevice implements Serializable {


	@Serial
	private static final long serialVersionUID = 4313403181560424464L;
	/**
	 * 设备id
	 */
	private String deviceId;

	/**
	 * 设备名称
	 */
	private String deviceBrand;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 最近使用时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastUseTime;

	/**
	 * 最近登录ip
	 */
	private String lastLoginIp;


	public void setDeviceId(String deviceId){
		this.deviceId = deviceId;
	}

	public String getDeviceId(){
		return this.deviceId;
	}

	public void setDeviceBrand(String deviceBrand){
		this.deviceBrand = deviceBrand;
	}

	public String getDeviceBrand(){
		return this.deviceBrand;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Date getCreateTime(){
		return this.createTime;
	}

	public void setLastUseTime(Date lastUseTime){
		this.lastUseTime = lastUseTime;
	}

	public Date getLastUseTime(){
		return this.lastUseTime;
	}

	public void setLastLoginIp(String lastLoginIp){
		this.lastLoginIp = lastLoginIp;
	}

	public String getLastLoginIp(){
		return this.lastLoginIp;
	}

	@Override
	public String toString (){
		return "设备id:"+(deviceId == null ? "空" : deviceId)+"，设备名称:"+(deviceBrand == null ? "空" : deviceBrand)+"，创建时间:"+(createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，最近使用时间:"+(lastUseTime == null ? "空" : DateUtil.format(lastUseTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，最近登录ip:"+(lastLoginIp == null ? "空" : lastLoginIp);
	}
}
