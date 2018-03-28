package com.xgzx.admin.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.KeySequence;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author DaiDong
 * @since 2018-02-03 
 * @email 755144556@qq.com
 */
@KeySequence("SEQ_groups_message")
@TableName("groups_message")
public class GroupsMessage extends Model<GroupsMessage> {

    private static final long serialVersionUID = 1L;

    @TableId(value="groups_message_id", type= IdType.INPUT)
	private Integer groupsMessageId;

	@TableField("group_id")
	private Integer groupId;
	private String message;
	
	@TableField(exist = false)
	private String userName;
	
	@TableField(exist = false)
	private User user;
	
    /**
     * 0无效，1有效
     */
	@TableField("valid_tag")
	private Integer validTag;
	
	@TableField("user_id")
	private String userId;

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

	public Integer getGroupsMessageId() {
		return groupsMessageId;
	}

	public void setGroupsMessageId(Integer groupsMessageId) {
		this.groupsMessageId = groupsMessageId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getValidTag() {
		return validTag;
	}

	public void setValidTag(Integer validTag) {
		this.validTag = validTag;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
		return this.groupsMessageId;
	}

	@Override
	public String toString() {
		return "GroupsMessage{" +
			"groupsMessageId=" + groupsMessageId +
			", groupId=" + groupId +
			", message=" + message +
			", validTag=" + validTag +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			"}";
	}
}
