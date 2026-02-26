package com.javastudy.vocabease_common.entity.query;

import java.util.Date;


/**
 * 小程序设备表参数
 */
public class AppDeviceQuery extends BaseParam {


	/**
	 * 设备id
	 */
	private String deviceId;

	private String deviceIdFuzzy;

	/**
	 * 设备名称
	 */
	private String deviceBrand;

	private String deviceBrandFuzzy;

	/**
	 * 创建时间
	 */
	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * 最近使用时间
	 */
	private String lastUseTime;

	private String lastUseTimeStart;

	private String lastUseTimeEnd;

	/**
	 * 最近登录ip
	 */
	private String lastLoginIp;

	private String lastLoginIpFuzzy;


	public void setDeviceId(String deviceId){
		this.deviceId = deviceId;
	}

	public String getDeviceId(){
		return this.deviceId;
	}

	public void setDeviceIdFuzzy(String deviceIdFuzzy){
		this.deviceIdFuzzy = deviceIdFuzzy;
	}

	public String getDeviceIdFuzzy(){
		return this.deviceIdFuzzy;
	}

	public void setDeviceBrand(String deviceBrand){
		this.deviceBrand = deviceBrand;
	}

	public String getDeviceBrand(){
		return this.deviceBrand;
	}

	public void setDeviceBrandFuzzy(String deviceBrandFuzzy){
		this.deviceBrandFuzzy = deviceBrandFuzzy;
	}

	public String getDeviceBrandFuzzy(){
		return this.deviceBrandFuzzy;
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

	public void setLastUseTime(String lastUseTime){
		this.lastUseTime = lastUseTime;
	}

	public String getLastUseTime(){
		return this.lastUseTime;
	}

	public void setLastUseTimeStart(String lastUseTimeStart){
		this.lastUseTimeStart = lastUseTimeStart;
	}

	public String getLastUseTimeStart(){
		return this.lastUseTimeStart;
	}
	public void setLastUseTimeEnd(String lastUseTimeEnd){
		this.lastUseTimeEnd = lastUseTimeEnd;
	}

	public String getLastUseTimeEnd(){
		return this.lastUseTimeEnd;
	}

	public void setLastLoginIp(String lastLoginIp){
		this.lastLoginIp = lastLoginIp;
	}

	public String getLastLoginIp(){
		return this.lastLoginIp;
	}

	public void setLastLoginIpFuzzy(String lastLoginIpFuzzy){
		this.lastLoginIpFuzzy = lastLoginIpFuzzy;
	}

	public String getLastLoginIpFuzzy(){
		return this.lastLoginIpFuzzy;
	}

}
