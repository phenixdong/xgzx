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
 * @since 2018-01-10 
 * @email 755144556@qq.com
 */
@KeySequence("SEQ_orders")
public class Orders extends Model<Orders> {

    private static final long serialVersionUID = 1L;

    @TableId(value="order_id", type= IdType.INPUT)
	private String orderId;

	@TableField("user_id")
	private String userId;
	
	@TableField(exist = false)
	private MemberType memberType; 
	
	@TableField(exist = false)
	private Lesson lesson; 
	
    /**
     * 商品id，多个商品用逗号分隔
     */

	@TableField("product_id")
	private String productId;
    /**
     * 商品类型，1会员，2商品
     */

	@TableField("product_type")
	private Integer productType;
    /**
     * 0未支付，1已支付
     */

	@TableField("pay_state")
	private Integer payState;
    /**
     * 总价
     */

	@TableField("total_fee")
	private BigDecimal totalFee;
    /**
     * 微信交易类型，JSAPI，NATIVE，APP
     */

	@TableField("trade_type")
	private String tradeType;
    /**
     * 付款银行
     */

	@TableField("bank_type")
	private String bankType;
    /**
     * 是否关注公众号
     */

	@TableField("is_subscribe")
	private Integer isSubscribe;
    /**
     * 推荐人openId
     */

	@TableField("recommender_user_id")
	private String recommenderUserId;

	@TableField("valid_tag")
	private Integer validTag;

	@TableField(value="create_time", fill=FieldFill.INSERT)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
    /**
     * 支付完成时间
     */

	@TableField("end_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date endTime;

	@TableField("create_user_id")
	private Long createUserId;

	@TableField(value="update_time", fill=FieldFill.UPDATE)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	@TableField("update_user_id")
	private Long updateUserId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

	public Integer getPayState() {
		return payState;
	}

	public void setPayState(Integer payState) {
		this.payState = payState;
	}

	 

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getBankType() {
		return bankType;
	}

	public MemberType getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberType memberType) {
		this.memberType = memberType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public Integer getIsSubscribe() {
		return isSubscribe;
	}

	public void setIsSubscribe(Integer isSubscribe) {
		this.isSubscribe = isSubscribe;
	}
 
	public String getRecommenderUserId() {
		return recommenderUserId;
	}

	public void setRecommenderUserId(String recommenderUserId) {
		this.recommenderUserId = recommenderUserId;
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

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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
		return this.orderId;
	}

	@Override
	public String toString() {
		return "Orders{" +
			"orderId=" + orderId +
			", userId=" + userId +
			", productId=" + productId +
			", productType=" + productType +
			", payState=" + payState +
			", totalFee=" + totalFee +
			", tradeType=" + tradeType +
			", bankType=" + bankType +
			", isSubscribe=" + isSubscribe +
			", recommenderUserId=" + recommenderUserId +
			", validTag=" + validTag +
			", createTime=" + createTime +
			", endTime=" + endTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			"}";
	}
}
