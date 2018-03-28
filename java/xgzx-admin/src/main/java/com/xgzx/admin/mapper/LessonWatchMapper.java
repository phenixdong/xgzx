package com.xgzx.admin.mapper;

import com.xgzx.admin.entity.LessonWatch;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * @author DaiDong
 * @since 2018-01-12
 * @email 755144556@qq.com
 */
public interface LessonWatchMapper extends BaseMapper<LessonWatch> {

	LessonWatch customQuery(int id);
	
	List<Map<String, Object>> getListByTime(Map<String, Object> param);
	
}