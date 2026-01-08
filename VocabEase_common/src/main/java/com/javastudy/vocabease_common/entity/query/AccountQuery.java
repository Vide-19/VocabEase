package com.javastudy.vocabease_common.entity.query;

/**
 * 参数
 */
public class AccountQuery extends BaseParam {


	/**
	 * 用户id
	 */
	private Integer userId;

	/**
	 * 用户名
	 */
	private String userName;

	private String userNameFuzzy;

	/**
	 * 手机号
	 */
	private String phone;

	private String phoneFuzzy;

	/**
	 * 密码
	 */
	private String password;

	private String passwordFuzzy;

	/**
	 * 职位
	 */
	private String position;

	private String positionFuzzy;

	/**
	 * 状态0/1
	 */
	private Integer status;

	/**
	 * 角色
	 */
	private String roles;

	private String rolesFuzzy;

	private Boolean rolesQuery;

	/**
	 * 创建时间
	 */
	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;

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

	public String getUserNameFuzzy() {
		return userNameFuzzy;
	}

	public void setUserNameFuzzy(String userNameFuzzy) {
		this.userNameFuzzy = userNameFuzzy;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhoneFuzzy() {
		return phoneFuzzy;
	}

	public void setPhoneFuzzy(String phoneFuzzy) {
		this.phoneFuzzy = phoneFuzzy;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordFuzzy() {
		return passwordFuzzy;
	}

	public void setPasswordFuzzy(String passwordFuzzy) {
		this.passwordFuzzy = passwordFuzzy;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPositionFuzzy() {
		return positionFuzzy;
	}

	public void setPositionFuzzy(String positionFuzzy) {
		this.positionFuzzy = positionFuzzy;
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

	public String getRolesFuzzy() {
		return rolesFuzzy;
	}

	public void setRolesFuzzy(String rolesFuzzy) {
		this.rolesFuzzy = rolesFuzzy;
	}

	public Boolean getRolesQuery() {
		return rolesQuery;
	}

	public void setRolesQuery(Boolean rolesQuery) {
		this.rolesQuery = rolesQuery;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}
}
