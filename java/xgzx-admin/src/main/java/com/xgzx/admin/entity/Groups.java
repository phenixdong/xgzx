package com.xgzx.admin.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.KeySequence;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author DaiDong
 * @since 2018-01-09 
 * @email 755144556@qq.com
 */
@KeySequence("SEQ_groups")
public class Groups extends Model<Groups> {

    private static final long serialVersionUID = 1L;

    @TableId(value="group_id", type= IdType.INPUT)
	private Integer groupId;
    
    @TableField(exist = false)
    Page<GroupsUser> page;
    
    @TableField(exist = false)
    private Integer memberNumber;
    
    @TableField(exist = false)
    private Integer msgNumber;
    
    @TableField(exist = false)
    private String userName;

	@TableField("group_name")
	private String groupName;

	@TableField("master_user_id")
	private String masterUserId;

	private Integer type;
	
	@TableField("valid_tag")
	private Integer validTag;

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

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	 
	public String getMasterUserId() {
		return masterUserId;
	}

	public void setMasterUserId(String masterUserId) {
		this.masterUserId = masterUserId;
	}

	public Integer getValidTag() {
		return validTag;
	}

	public void setValidTag(Integer validTag) {
		this.validTag = validTag;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public Integer getMsgNumber() {
		return msgNumber;
	}

	public void setMsgNumber(Integer msgNumber) {
		this.msgNumber = msgNumber;
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
		return this.groupId;
	}

	public Integer getMemberNumber() {
		return memberNumber;
	}

	public void setMemberNumber(Integer memberNumber) {
		this.memberNumber = memberNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Page<GroupsUser> getPage() {
		return page;
	}

	public void setPage(Page<GroupsUser> page) {
		this.page = page;
	}

	@Override
	public String toString() {
		return "Groups{" +
			"groupId=" + groupId +
			", groupName=" + groupName +
			", masterUserId=" + masterUserId +
			", validTag=" + validTag +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			"}";
	}
}
