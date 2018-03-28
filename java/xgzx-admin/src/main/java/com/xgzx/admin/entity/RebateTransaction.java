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
@KeySequence("SEQ_rebate_transaction")
@TableName("rebate_transaction")
public class RebateTransaction extends Model<RebateTransaction> {

    private static final long serialVersionUID = 1L;

    @TableId(value="rebate_transaction_id", type= IdType.INPUT)
	private Integer rebateTransactionId;

	@TableField("user_id")
	private String userId;
	private BigDecimal number;
	
	@TableField(exist = false)
	private String ordersUserName;// 订单的购买用户
	
	@TableField(exist = false)
	private BigDecimal ordersTotalFee;
	
	@TableField(exist = false)
	private String ordersProductName;
	
	@TableField(exist = false)
	private String userName;
	@TableField(exist = false)
	private String orderUserName;
	@TableField(exist = false)
	private Integer orderProductType;
	@TableField(exist = false)
	private Integer orderNumber;

	@TableField("order_id")
	private String orderId;
    /**
     * 0无效，1有效
     */
	@TableField("valid_tag")
	private Integer validTag;
	
	/**
     * 1收入，2支出
     */
	private Integer operation;
	
	/**
     * 1未处理，2已处理
     */
	private Integer status;

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

	public Integer getRebateTransactionId() {
		return rebateTransactionId;
	}

	public void setRebateTransactionId(Integer rebateTransactionId) {
		this.rebateTransactionId = rebateTransactionId;
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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getValidTag() {
		return validTag;
	}

	public String getOrdersUserName() {
		return ordersUserName;
	}

	public void setOrdersUserName(String ordersUserName) {
		this.ordersUserName = ordersUserName;
	}

	public BigDecimal getOrdersTotalFee() {
		return ordersTotalFee;
	}

	public void setOrdersTotalFee(BigDecimal ordersTotalFee) {
		this.ordersTotalFee = ordersTotalFee;
	}

	public String getOrdersProductName() {
		return ordersProductName;
	}

	public void setOrdersProductName(String ordersProductName) {
		this.ordersProductName = ordersProductName;
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
		return this.rebateTransactionId;
	}

	public String getOrderUserName() {
		return orderUserName;
	}

	public void setOrderUserName(String orderUserName) {
		this.orderUserName = orderUserName;
	}

	public Integer getOrderProductType() {
		return orderProductType;
	}

	public void setOrderProductType(Integer orderProductType) {
		this.orderProductType = orderProductType;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getOperation() {
		return operation;
	}

	public void setOperation(Integer operation) {
		this.operation = operation;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "RebateTransaction{" +
			"rebateTransactionId=" + rebateTransactionId +
			", userId=" + userId +
			", number=" + number +
			", orderId=" + orderId +
			", validTag=" + validTag +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			"}";
	}
	 
}
