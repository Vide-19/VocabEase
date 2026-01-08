package com.javastudy.vocabease_common.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.javastudy.vocabease_common.entity.annotation.VerifyParam;
import com.javastudy.vocabease_common.entity.enums.DateTimePatternEnum;
import com.javastudy.vocabease_common.entity.enums.VerifyRegexEnum;
import com.javastudy.vocabease_common.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


/**
 * 账户信息
 */
public class Account implements Serializable {

	/**
	 * 用户id
	 */
	private Integer userId;

	/**
	 * 用户名
	 */
	@VerifyParam(required = true, max = 20)
	private String userName;

	/**
	 * 手机号
	 */
	@VerifyParam(required = true, regex = VerifyRegexEnum.PHONE)
	private String phone;

	/**
	 * 密码
	 */
	@JsonIgnore
	@VerifyParam(regex = VerifyRegexEnum.PASSWORD)
	private String password;

	/**
	 * 职位
	 */
	private String position;

	/**
	 * 状态0/1
	 */
	private Integer status;

	/**
	 * 角色
	 */
	@VerifyParam(required = true)
	private String roles;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	private String roleNames;

	@Override
	public String toString (){
		return "用户id:"+(userId == null ? "空" : userId)+"，用户名:"+(userName == null ? "空" : userName)+"，手机号:"+(phone == null ? "空" : phone)+"，密码:"+(password == null ? "空" : password)+"，职位:"+(position == null ? "空" : position)+"，状态0/1:"+(status == null ? "空" : status)+"，角色:"+(roles == null ? "空" : roles)+"，创建时间:"+(createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern()));
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}
}
