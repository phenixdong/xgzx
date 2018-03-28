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
 * @since 2018-01-09 
 * @email 755144556@qq.com
 */
@KeySequence("SEQ_rebate")
public class Rebate extends Model<Rebate> {

    private static final long serialVersionUID = 1L;

    @TableId(value="rebate_id", type= IdType.INPUT)
	private Integer rebateId;

	@TableField("user_id")
	private String userId;
	
	@TableField("withdraw_password")
	private String withdrawPassword;
	
	@TableField(exist = false)
	private RebateWxQrcode rebateWxQrcode;
	
	private BigDecimal number;
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

	public Integer getRebateId() {
		return rebateId;
	}

	public void setRebateId(Integer rebateId) {
		this.rebateId = rebateId;
	}

	public String getWithdrawPassword() {
		return withdrawPassword;
	}

	public void setWithdrawPassword(String withdrawPassword) {
		this.withdrawPassword = withdrawPassword;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	 

	public BigDecimal getNumber() {
		return number;
	}

	public void setNumber(BigDecimal number) {
		this.number = number;
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

	public RebateWxQrcode getRebateWxQrcode() {
		return rebateWxQrcode;
	}

	public void setRebateWxQrcode(RebateWxQrcode rebateWxQrcode) {
		this.rebateWxQrcode = rebateWxQrcode;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Override
	protected Serializable pkVal() {
		return this.rebateId;
	}

	@Override
	public String toString() {
		return "Rebate{" +
			"rebateId=" + rebateId +
			", userId=" + userId +
			", number=" + number +
			", validTag=" + validTag +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			"}";
	}
}
