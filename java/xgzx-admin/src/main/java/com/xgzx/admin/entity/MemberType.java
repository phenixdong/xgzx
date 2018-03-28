package com.xgzx.admin.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
 * @since 2018-01-10 
 * @email 755144556@qq.com
 */
@KeySequence("SEQ_member_type")
@TableName("member_type")
public class MemberType extends Model<MemberType> {

    private static final long serialVersionUID = 1L;

    @TableId(value="member_type_id", type= IdType.INPUT)
	private Integer memberTypeId;

	@TableField("member_type_name")
	private String memberTypeName;
	
	@TableField(exist = false)
	private List<String> descriptionList;
	
    /**
     * 会员时长，单位：月
     */
	private Integer period;
	private BigDecimal price;
	private String description;
	
	@TableField("order_no")
	private Integer orderNo;

	@TableField("valid_tag")
	private Integer validTag;

	@TableField("create_user_id")
	private Long createUserId;

	@TableField(value="create_time", fill=FieldFill.INSERT)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	@TableField("update_user_id")
	private Long updateUserId;

	@TableField(value="update_time", fill=FieldFill.UPDATE)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

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

	public List<String> getDescriptionList() {
		return descriptionList;
	}

	public void setDescriptionList(List<String> descriptionList) {
		this.descriptionList = descriptionList;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	 

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getValidTag() {
		return validTag;
	}

	public void setValidTag(Integer validTag) {
		this.validTag = validTag;
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
		return this.memberTypeId;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	@Override
	public String toString() {
		return "MemberType{" +
			"memberTypeId=" + memberTypeId +
			", memberTypeName=" + memberTypeName +
			", period=" + period +
			", price=" + price +
			", description=" + description +
			", validTag=" + validTag +
			", createUserId=" + createUserId +
			", createTime=" + createTime +
			", updateUserId=" + updateUserId +
			", updateTime=" + updateTime +
			"}";
	}
}
