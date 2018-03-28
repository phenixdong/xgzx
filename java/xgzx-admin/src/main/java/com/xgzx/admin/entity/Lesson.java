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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * @author DaiDong
 * @since 2018-01-05 
 * @email 755144556@qq.com
 */
@KeySequence("SEQ_lesson")
public class Lesson extends Model<Lesson> {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
	private Video video = new Video();
    
    @TableField(exist = false)
	private List<LessonContent> lessonContentList;
    
    @TableField(exist = false)
    private Teacher teacher;
    
    @TableField(exist = false)
    private String coverUrl;
    
    @TableField(exist = false)
	private String teacherName;
    
    @TableField(exist = false)
	private String categoryName;
    
    @TableId(value="lesson_id", type= IdType.INPUT)
	private Integer lessonId;

	@TableField("lesson_name")
	private String lessonName;
	
	private String description;

	@TableField("category_id")
	private Integer categoryId;

	@TableField("content_image_url")
	private String contentImageUrl; 
	
	/**
     * 顺序
     */
	@TableField("order_no")
	private Integer orderNo;

	@TableField("teacher_id")
	private Integer teacherId;

	@TableField("video_id")
	private String videoId;
	private BigDecimal price;
    /**
     * 收费类型，1免费，2会员免费，3单独收费
     */

	@TableField("charge_type")
	private Integer chargeType;

	@TableField("watch_no")
	private Integer watchNo;

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

	public Integer getLessonId() {
		return lessonId;
	}

	public void setLessonId(Integer lessonId) {
		this.lessonId = lessonId;
	}

	public String getLessonName() {
		return lessonName;
	}

	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public Integer getChargeType() {
		return chargeType;
	}

	public void setChargeType(Integer chargeType) {
		this.chargeType = chargeType;
	}
	 
	public String getContentImageUrl() {
		return contentImageUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setContentImageUrl(String contentImageUrl) {
		this.contentImageUrl = contentImageUrl;
	}

	public Integer getWatchNo() {
		return watchNo;
	}

	public void setWatchNo(Integer watchNo) {
		this.watchNo = watchNo;
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

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	@Override
	protected Serializable pkVal() {
		return this.lessonId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public List<LessonContent> getLessonContentList() {
		return lessonContentList;
	}

	public void setLessonContentList(List<LessonContent> lessonContentList) {
		this.lessonContentList = lessonContentList;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	@Override
	public String toString() {
		return "Lesson{" +
			"lessonId=" + lessonId +
			", lessonName=" + lessonName +
			", categoryId=" + categoryId +
			", teacherId=" + teacherId +
			", videoId=" + videoId +
			", price=" + price +
			", chargeType=" + chargeType +
			", watchNo=" + watchNo +
			", validTag=" + validTag +
			", createTime=" + createTime +
			", createUserId=" + createUserId +
			", updateTime=" + updateTime +
			", updateUserId=" + updateUserId +
			"}";
	}
}
