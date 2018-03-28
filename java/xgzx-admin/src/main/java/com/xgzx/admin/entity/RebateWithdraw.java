package com.xgzx.admin.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * @since 2018-01-09 
 * @email 755144556@qq.com
 */
@KeySequence("SEQ_rebate_withdraw")
@TableName("rebate_withdraw")
public class RebateWithdraw extends Model<RebateWithdraw> {

    private static final long serialVersionUID = 1L;

    @TableId(value="rebate_withdraw_id", type= IdType.INPUT)
	private Integer rebateWithdrawId;

	@TableField("user_id")
	private String userId;
	
	private BigDecimal number;
	
	@TableField(exist = false)
	private String userName;
	
	@TableField(exist = false)
	private String qrcodeUrl;
	
	@TableField(exist = false)
	private User user;
	
	@TableField(exist = false)
	private Rebate rebate;

	@TableField("apply_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date applyTime;

	@TableField("pay_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date payTime;
    /**
     * 1未支付，2待领取，3已领取，4已退款
     */
	private Integer status;
	
	@TableField("mch_bill_no")
	private String mchBillNo;
	
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

	public Integer getRebateWithdrawId() {
		return rebateWithdrawId;
	}

	public void setRebateWithdrawId(Integer rebateWithdrawId) {
		this.rebateWithdrawId = rebateWithdrawId;
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

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getValidTag() {
		return validTag;
	}

	public Rebate getRebate() {
		return rebate;
	}

	public void setRebate(Rebate rebate) {
		this.rebate = rebate;
	}

	public User getUser() {
		return user;
	}

	public String getMchBillNo() {
		return mchBillNo;
	}

	public void setMchBillNo(String mchBillNo) {
		this.mchBillNo = mchBillNo;
	}

	public void setUser(User user) {
		this.user = user;
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

	public String getQrcodeUrl() {
		return qrcodeUrl;
	}

	public void setQrcodeUrl(String qrcodeUrl) {
		this.qrcodeUrl = qrcodeUrl;
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
		return this.rebateWithdrawId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
 

	@Override
	public String toString() {
		return "RebateWithdraw{" +
			"rebateWithdrawId=" + rebateWithdrawId +
			", userId=" + userId +
			", number=" + number +
			", applyTime=" + applyTime +
			", payTime=" + payTime +
			", status=" + status +
			", validTag=" + validTag +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			"}";
	}
}
