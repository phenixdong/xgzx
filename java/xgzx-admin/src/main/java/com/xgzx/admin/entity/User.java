package com.xgzx.admin.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.KeySequence;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author DaiDong
 * @since 2017-12-24 
 * @email 755144556@qq.com
 */
@KeySequence("SEQ_user")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    @TableId(value="user_id", type= IdType.INPUT)
	private String userId;

	@TableField("open_id")
	private String openId;
	
	@TableField("login_id")
	private String loginId;

	@TableField("recommender_user_id")
	private String recommenderUserId;
	private String password;
	
	@TableField(exist = false)
	private Integer memberTypeId;
	@TableField(exist = false)
	private String memberTypeName;
	@TableField(exist = false)
	private BigDecimal memberTypePrice;
	
	@TableField(exist = false)
	private String token;
	
	@TableField(exist = false)
	private Integer[] paidLessonIds;
	
	@TableField(exist = false)
	private Date startTime;
	@TableField(exist = false)
	private Date endTime;
	
	@TableField(exist = false)
	private boolean isMemberValid;
	
	@TableField("user_name")	
	private String userName;
	
	@TableField("nick_name")
	private String nickName;
	
    /**
     * 0无效，1有效
     */
	@TableField("valid_tag")
	private Integer validTag;
	
    /**
     * 状态，0锁定，1正常
     */
	@TableField("user_state")
	private Integer userState;
	
	private String mobile;
	
	private String email;
	
	@TableField("image_url")
	private String imageUrl;
	
	private String province;
	
	private String city;
	
    /**
     * 1男，2女，0未知
     */
	private Integer sex;
	
	@TableField("area_id")	
	private Integer areaId;
	
	@TableField("area_name")
	private String areaName;
	
	@TableField(value="create_time", fill=FieldFill.INSERT)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	
	@TableField("create_user_id")
	private Long createUserId;
	
	@TableField(value="update_time", fill=FieldFill.UPDATE)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
	
	@TableField("update_user_id")
	private Long updateUserId;
 
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getRecommenderUserId() {
		return recommenderUserId;
	}

	public void setRecommenderUserId(String recommenderUserId) {
		this.recommenderUserId = recommenderUserId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getValidTag() {
		return validTag;
	}

	public void setValidTag(Integer validTag) {
		this.validTag = validTag;
	}

	public Integer getUserState() {
		return userState;
	}

	public Integer[] getPaidLessonIds() {
		return paidLessonIds;
	}

	public void setPaidLessonIds(Integer[] paidLessonIds) {
		this.paidLessonIds = paidLessonIds;
	}

	public void setUserState(Integer userState) {
		this.userState = userState;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public boolean getIsMemberValid() {
		return isMemberValid;
	}

	public void setIsMemberValid(boolean isMemberValid) {
		this.isMemberValid = isMemberValid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getSex() {
		return sex;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public BigDecimal getMemberTypePrice() {
		return memberTypePrice;
	}

	public void setMemberTypePrice(BigDecimal memberTypePrice) {
		this.memberTypePrice = memberTypePrice;
	}

	public void setMemberValid(boolean isMemberValid) {
		this.isMemberValid = isMemberValid;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Override
	protected Serializable pkVal() {
		return this.userId;
	}

	public Integer getMemberTypeId() {
		return memberTypeId;
	}

	public void setMemberTypeId(Integer memberTypeId) {
		this.memberTypeId = memberTypeId;
	}

	public String getMemberTypeName() {
		return memberTypeName;
	}

	public void setMemberTypeName(String memberTypeName) {
		this.memberTypeName = memberTypeName;
	}

	@Override
	public String toString() {
		return "User{" +
			"userId=" + userId +
			", openId=" + openId +
			", loginId=" + loginId +
			", recommenderUserId=" + recommenderUserId +
			", password=" + password +
			", userName=" + userName +
			", nickName=" + nickName +
			", validTag=" + validTag +
			", userState=" + userState +
			", mobile=" + mobile +
			", email=" + email +
			", sex=" + sex +
			", imageUrl=" + imageUrl +
			", province=" + province +
			", city=" + city +
			", areaId=" + areaId +
			", areaName=" + areaName +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			"}";
	}
}
