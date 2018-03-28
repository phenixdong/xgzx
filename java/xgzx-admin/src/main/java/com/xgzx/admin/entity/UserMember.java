package com.xgzx.admin.entity;

import com.baomidou.mybatisplus.annotations.Version;
import com.baomidou.mybatisplus.annotations.KeySequence;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * @author DaiDong
 * @since 2017-12-24 
 * @email 755144556@qq.com
 */
@KeySequence("SEQ_user_member")
@TableName("user_member")
public class UserMember extends Model<UserMember> {

    private static final long serialVersionUID = 1L;

    @TableId(value="user_member_id", type= IdType.INPUT)
	private Long userMemberId;
    
	@TableField("user_id")
	private String userId;
	
	@TableField("member_type_id")
	private Integer memberTypeId;
	
	@TableField("valid_tag")
	private Integer validTag;
	
	@TableField("create_user_id")
	private Long createUserId;
	
	@TableField("buy_type")
	private Integer buyType;
	
	@TableField(value="create_time", fill=FieldFill.INSERT)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	
	@TableField("update_user_id")
	private Long updateUserId;
	
	@TableField(value="update_time", fill=FieldFill.UPDATE)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
	
	@TableField("start_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date startTime;
	
	@TableField("end_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date endTime;

	public Integer getValidTag() {
		return validTag;
	}

	public void setValidTag(Integer validTag) {
		this.validTag = validTag;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Integer getBuyType() {
		return buyType;
	}

	public void setBuyType(Integer buyType) {
		this.buyType = buyType;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getUserMemberId() {
		return userMemberId;
	}

	public void setUserMemberId(Long userMemberId) {
		this.userMemberId = userMemberId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
 
	public Integer getMemberTypeId() {
		return memberTypeId;
	}

	public void setMemberTypeId(Integer memberTypeId) {
		this.memberTypeId = memberTypeId;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.userMemberId;
	}

	@Override
	public String toString() {
		return "UserMember{" +
			"userMemberId=" + userMemberId +
			", userId=" + userId +
			", memberTypeId=" + memberTypeId +
			", createUserId=" + createUserId +
			", createTime=" + createTime +
			", updateUserId=" + updateUserId +
			", updateTime=" + updateTime +
			", startTime=" + startTime +
			", endTime=" + endTime +
			"}";
	}
}
