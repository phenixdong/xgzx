package com.xgzx.admin.entity;

import com.baomidou.mybatisplus.annotations.Version;
import com.baomidou.mybatisplus.annotations.KeySequence;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * @author DaiDong
 * @since 2018-01-05 
 * @email 755144556@qq.com
 */
@KeySequence("SEQ_teacher")
public class Teacher extends Model<Teacher> {

    private static final long serialVersionUID = 1L;

    @TableId(value="teacher_id", type= IdType.INPUT)
	private Integer teacherId;

	@TableField("teacher_name")
	private String teacherName;

	@TableField("image_url")
	private String imageUrl;
	private String title;
	private String position;
	private String description;
	private String phone;
	
	@TableField(exist = false)
	private float monthMemberRebate;
	@TableField(exist = false)
	private float yearMemberRebate;
	@TableField(exist = false)
	private float bulkMemberRebate;
	@TableField(exist = false)
	private float totalRebate;
	
    /**
     * 1男，2女，0未知
     */
	private Integer sex;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date birthday;
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

	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getDescription() {
		return description;
	}

	public float getMonthMemberRebate() {
		return monthMemberRebate;
	}

	public void setMonthMemberRebate(float monthMemberRebate) {
		this.monthMemberRebate = monthMemberRebate;
	}

	public float getYearMemberRebate() {
		return yearMemberRebate;
	}

	public void setYearMemberRebate(float yearMemberRebate) {
		this.yearMemberRebate = yearMemberRebate;
	}

	public float getBulkMemberRebate() {
		return bulkMemberRebate;
	}

	public void setBulkMemberRebate(float bulkMemberRebate) {
		this.bulkMemberRebate = bulkMemberRebate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPhone() {
		return phone;
	}

	public float getTotalRebate() {
		return totalRebate;
	}

	public void setTotalRebate(float totalRebate) {
		this.totalRebate = totalRebate;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
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
		return this.teacherId;
	}

	@Override
	public String toString() {
		return "Teacher{" +
			"teacherId=" + teacherId +
			", teacherName=" + teacherName +
			", imageUrl=" + imageUrl +
			", description=" + description +
			", phone=" + phone +
			", sex=" + sex +
			", birthday=" + birthday +
			", validTag=" + validTag +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			"}";
	}
}
