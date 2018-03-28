package com.xgzx.admin.entity;

import com.baomidou.mybatisplus.annotations.Version;
import com.baomidou.mybatisplus.annotations.KeySequence;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * @author DaiDong
 * @since 2018-03-17 
 * @email 755144556@qq.com
 */
@KeySequence("SEQ_bulk_buy_register")
@TableName("bulk_buy_register")
public class BulkBuyRegister extends Model<BulkBuyRegister> {

    private static final long serialVersionUID = 1L;

    @TableId(value="bulk_buy_register_id", type= IdType.INPUT)
	private Integer bulkBuyRegisterId;
	
	@TableField("bulk_buy_id")
	private String bulkBuyId;
	
	@TableField("user_id")
	private String userId;
	
    /**
     * 0无效，1有效
     */
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
	

	public Integer getBulkBuyRegisterId() {
		return bulkBuyRegisterId;
	}

	public void setBulkBuyRegisterId(Integer bulkBuyRegisterId) {
		this.bulkBuyRegisterId = bulkBuyRegisterId;
	}

	public String getBulkBuyId() {
		return bulkBuyId;
	}

	public void setBulkBuyId(String bulkBuyId) {
		this.bulkBuyId = bulkBuyId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
		return this.bulkBuyRegisterId;
	}

	@Override
	public String toString() {
		return "BulkBuyRegister{" +
			"bulkBuyRegisterId=" + bulkBuyRegisterId +
			", bulkBuyId=" + bulkBuyId +
			", userId=" + userId +
			", validTag=" + validTag +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			"}";
	}
}
