package com.javastudy.vocabease_common.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.javastudy.vocabease_common.entity.enums.DateTimePatternEnum;
import com.javastudy.vocabease_common.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * 小程序用户表
 */
public class AppAccount implements Serializable {


	@Serial
	private static final long serialVersionUID = 2243929529329013987L;
	/**
	 * 用户id
	 */
	private String userId;

	/**
	 * 用户邮箱
	 */
	private String email;

	/**
	 * 昵称
	 */
	private String nickName;

	/**
	 * 头像
	 */
	private String avatar;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 性别 0女 1男
	 */
	private Integer gender;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 最近登录时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastLoginTime;

	/**
	 * 最近使用的设备id
	 */
	private String lastUseDeviceId;

	/**
	 * 最近使用的设备品牌
	 */
	private String lastUseDeviceBrand;

	/**
	 * 最后登录ip
	 */
	private String lastLoginIp;

	/**
	 * 状态 0禁用 1启用
	 */
	private Integer status;


	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return this.email;
	}

	public void setNickName(String nickName){
		this.nickName = nickName;
	}

	public String getNickName(){
		return this.nickName;
	}

	public void setAvatar(String avatar){
		this.avatar = avatar;
	}

	public String getAvatar(){
		return this.avatar;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return this.password;
	}

	public void setGender(Integer gender){
		this.gender = gender;
	}

	public Integer getGender(){
		return this.gender;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public Date getCreateTime(){
		return this.createTime;
	}

	public void setLastLoginTime(Date lastLoginTime){
		this.lastLoginTime = lastLoginTime;
	}

	public Date getLastLoginTime(){
		return this.lastLoginTime;
	}

	public void setLastUseDeviceId(String lastUseDeviceId){
		this.lastUseDeviceId = lastUseDeviceId;
	}

	public String getLastUseDeviceId(){
		return this.lastUseDeviceId;
	}

	public void setLastUseDeviceBrand(String lastUseDeviceBrand){
		this.lastUseDeviceBrand = lastUseDeviceBrand;
	}

	public String getLastUseDeviceBrand(){
		return this.lastUseDeviceBrand;
	}

	public void setLastLoginIp(String lastLoginIp){
		this.lastLoginIp = lastLoginIp;
	}

	public String getLastLoginIp(){
		return this.lastLoginIp;
	}

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

	@Override
	public String toString (){
		return "用户id:"+(userId == null ? "空" : userId)+"，用户邮箱:"+(email == null ? "空" : email)+"，昵称:"+(nickName == null ? "空" : nickName)+"，头像:"+(avatar == null ? "空" : avatar)+"，密码:"+(password == null ? "空" : password)+"，性别 0女 1男:"+(gender == null ? "空" : gender)+"，创建时间:"+(createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，最近登录时间:"+(lastLoginTime == null ? "空" : DateUtil.format(lastLoginTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()))+"，最近使用的设备id:"+(lastUseDeviceId == null ? "空" : lastUseDeviceId)+"，最近使用的设备品牌:"+(lastUseDeviceBrand == null ? "空" : lastUseDeviceBrand)+"，最后登录ip:"+(lastLoginIp == null ? "空" : lastLoginIp)+"，状态 0禁用 1启用:"+(status == null ? "空" : status);
	}
}
