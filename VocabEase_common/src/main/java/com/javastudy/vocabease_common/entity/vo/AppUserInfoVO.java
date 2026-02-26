package com.javastudy.vocabease_common.entity.vo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class AppUserInfoVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 704309655358675655L;
    /**
     * 用户id
     */
    private String userId;
    /**
     *邮箱
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
     * 性别 0女 1男
     */
    private Integer gender;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 最后登录时间
     */
    private Date lastLoginTime;
    /**
     * 手机品牌
     */
    private String lastUseDeviceBrand;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastUseDeviceBrand() {
        return lastUseDeviceBrand;
    }

    public void setLastUseDeviceBrand(String lastUseDeviceBrand) {
        this.lastUseDeviceBrand = lastUseDeviceBrand;
    }
}
