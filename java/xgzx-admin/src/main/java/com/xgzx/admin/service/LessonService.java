package com.xgzx.admin.service;

import com.xgzx.admin.entity.Lesson;
import com.xgzx.admin.entity.User;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2018-01-05
 * @email 755144556@qq.com
 */
public interface LessonService extends IService<Lesson> {
	
	BaseResult customQuery(int id);
	
	BaseResult insert(Map<String, Object> param);
	
	BaseResult selectLessonList(Page<Lesson> page, Map<String, Object> lessonrMap);
	
	BaseResult getWatchList(Page<Lesson> page,  Map<String, Object> lessonrMap);
	
	BaseResult update(Map<String, Object> param);
	
	int insertReturnId(Lesson lesson);
	
	BaseResult addWatchRecord(Map<String, Object> param);
}
