package com.xgzx.admin.mapper;

import com.xgzx.admin.entity.Lesson;
import com.xgzx.admin.entity.User;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * @author DaiDong
 * @since 2018-01-05
 * @email 755144556@qq.com
 */
public interface LessonMapper extends BaseMapper<Lesson> {

	Lesson customQuery(int id);
	
	int insertReturnId(Lesson lesson);
	
	List<Lesson> selectLessonList(Pagination page, Map<String, Object> lessonMap);
	
	List<Lesson> getWatchList(Pagination page, Map<String, Object> lessonMap);
	
}