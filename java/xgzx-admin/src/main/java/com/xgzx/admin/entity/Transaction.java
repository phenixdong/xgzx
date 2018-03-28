package com.xgzx.admin.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.KeySequence;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author DaiDong
 * @since 2018-01-10 
 * @email 755144556@qq.com
 */
@KeySequence("SEQ_transaction")
public class Transaction extends Model<Transaction> {

    private static final long serialVersionUID = 1L;

    @TableId(value="transation_id", type= IdType.INPUT)
	private Integer transationId;

	@TableField("user_id")
	private String userId;
	
	@TableField(exist = false)
	private String userName;
	
	@TableField(exist = false)
	private String nickName;
	
    /**
     * 交易类型，1购买会员，2购买商品，3充值，4提现
     */
	private Integer type;
    /**
     * 1收入，2支出
     */
	private Integer operation;
	private BigDecimal number;

	@TableField("order_id")
	private String orderId;
    /**
     * 支付交易类型，1微信，2银行卡
     */

	@TableField("pay_transaction_type")
	private Integer payTransactionType;
    /**
     * 支付交易id
     */

	@TableField("pay_transaction_id")
	private String payTransactionId;
    /**
     * 转出用户
     */

	@TableField("src_user_id")
	private Long srcUserId;
    /**
     * 转出账户
     */

	@TableField("src_account_id")
	private Long srcAccountId;

	@TableField("valid_tag")
	private Integer validTag;

	@TableField("create_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	@TableField("create_user_id")
	private Long createUserId;

	@TableField("update_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	@TableField("update_user_id")
	private Long updateUserId;

	public Integer getTransationId() {
		return transationId;
	}

	public void setTransationId(Integer transationId) {
		this.transationId = transationId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getOperation() {
		return operation;
	}

	public void setOperation(Integer operation) {
		this.operation = operation;
	}

	 

	public BigDecimal getNumber() {
		return number;
	}

	public void setNumber(BigDecimal number) {
		this.number = number;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getPayTransactionType() {
		return payTransactionType;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setPayTransactionType(Integer payTransactionType) {
		this.payTransactionType = payTransactionType;
	}

	public String getPayTransactionId() {
		return payTransactionId;
	}

	public void setPayTransactionId(String payTransactionId) {
		this.payTransactionId = payTransactionId;
	}

	public Long getSrcUserId() {
		return srcUserId;
	}

	public void setSrcUserId(Long srcUserId) {
		this.srcUserId = srcUserId;
	}

	public Long getSrcAccountId() {
		return srcAccountId;
	}

	public void setSrcAccountId(Long srcAccountId) {
		this.srcAccountId = srcAccountId;
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
		return this.transationId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "Transaction{" +
			"transationId=" + transationId +
			", userId=" + userId +
			", type=" + type +
			", operation=" + operation +
			", number=" + number +
			", orderId=" + orderId +
			", payTransactionType=" + payTransactionType +
			", payTransactionId=" + payTransactionId +
			", srcUserId=" + srcUserId +
			", srcAccountId=" + srcAccountId +
			", validTag=" + validTag +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			"}";
	}
}
