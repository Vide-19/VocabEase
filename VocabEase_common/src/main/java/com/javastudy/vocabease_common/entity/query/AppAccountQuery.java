package com.javastudy.vocabease_common.entity.query;

/**
 * 小程序用户表参数
 */
public class AppAccountQuery extends BaseParam {


	/**
	 * 用户id
	 */
	private String userId;

	private String userIdFuzzy;

	private String openId;

	private String openIdFuzzy;

	/**
	 * 用户邮箱
	 */
	private String email;

	private String emailFuzzy;

	/**
	 * 昵称
	 */
	private String nickName;

	private String nickNameFuzzy;

	/**
	 * 头像
	 */
	private String avatar;

	private String avatarFuzzy;

	/**
	 * 密码
	 */
	private String password;

	private String passwordFuzzy;

	/**
	 * 性别 0女 1男
	 */
	private Integer gender;

	/**
	 * 创建时间
	 */
	private String createTime;

	private String createTimeStart;

	private String createTimeEnd;

	/**
	 * 最近登录时间
	 */
	private String lastLoginTime;

	private String lastLoginTimeStart;

	private String lastLoginTimeEnd;

	/**
	 * 最近使用的设备id
	 */
	private String lastUseDeviceId;

	private String lastUseDeviceIdFuzzy;

	/**
	 * 最近使用的设备品牌
	 */
	private String lastUseDeviceBrand;

	private String lastUseDeviceBrandFuzzy;

	/**
	 * 最后登录ip
	 */
	private String lastLoginIp;

	private String lastLoginIpFuzzy;

	/**
	 * 状态 0禁用 1启用
	 */
	private Integer status;

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getOpenIdFuzzy() {
		return openIdFuzzy;
	}

	public void setOpenIdFuzzy(String openIdFuzzy) {
		this.openIdFuzzy = openIdFuzzy;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return this.userId;
	}

	public void setUserIdFuzzy(String userIdFuzzy){
		this.userIdFuzzy = userIdFuzzy;
	}

	public String getUserIdFuzzy(){
		return this.userIdFuzzy;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return this.email;
	}

	public void setEmailFuzzy(String emailFuzzy){
		this.emailFuzzy = emailFuzzy;
	}

	public String getEmailFuzzy(){
		return this.emailFuzzy;
	}

	public void setNickName(String nickName){
		this.nickName = nickName;
	}

	public String getNickName(){
		return this.nickName;
	}

	public void setNickNameFuzzy(String nickNameFuzzy){
		this.nickNameFuzzy = nickNameFuzzy;
	}

	public String getNickNameFuzzy(){
		return this.nickNameFuzzy;
	}

	public void setAvatar(String avatar){
		this.avatar = avatar;
	}

	public String getAvatar(){
		return this.avatar;
	}

	public void setAvatarFuzzy(String avatarFuzzy){
		this.avatarFuzzy = avatarFuzzy;
	}

	public String getAvatarFuzzy(){
		return this.avatarFuzzy;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return this.password;
	}

	public void setPasswordFuzzy(String passwordFuzzy){
		this.passwordFuzzy = passwordFuzzy;
	}

	public String getPasswordFuzzy(){
		return this.passwordFuzzy;
	}

	public void setGender(Integer gender){
		this.gender = gender;
	}

	public Integer getGender(){
		return this.gender;
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

	public void setLastLoginTime(String lastLoginTime){
		this.lastLoginTime = lastLoginTime;
	}

	public String getLastLoginTime(){
		return this.lastLoginTime;
	}

	public void setLastLoginTimeStart(String lastLoginTimeStart){
		this.lastLoginTimeStart = lastLoginTimeStart;
	}

	public String getLastLoginTimeStart(){
		return this.lastLoginTimeStart;
	}
	public void setLastLoginTimeEnd(String lastLoginTimeEnd){
		this.lastLoginTimeEnd = lastLoginTimeEnd;
	}

	public String getLastLoginTimeEnd(){
		return this.lastLoginTimeEnd;
	}

	public void setLastUseDeviceId(String lastUseDeviceId){
		this.lastUseDeviceId = lastUseDeviceId;
	}

	public String getLastUseDeviceId(){
		return this.lastUseDeviceId;
	}

	public void setLastUseDeviceIdFuzzy(String lastUseDeviceIdFuzzy){
		this.lastUseDeviceIdFuzzy = lastUseDeviceIdFuzzy;
	}

	public String getLastUseDeviceIdFuzzy(){
		return this.lastUseDeviceIdFuzzy;
	}

	public void setLastUseDeviceBrand(String lastUseDeviceBrand){
		this.lastUseDeviceBrand = lastUseDeviceBrand;
	}

	public String getLastUseDeviceBrand(){
		return this.lastUseDeviceBrand;
	}

	public void setLastUseDeviceBrandFuzzy(String lastUseDeviceBrandFuzzy){
		this.lastUseDeviceBrandFuzzy = lastUseDeviceBrandFuzzy;
	}

	public String getLastUseDeviceBrandFuzzy(){
		return this.lastUseDeviceBrandFuzzy;
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

	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return this.status;
	}

}
