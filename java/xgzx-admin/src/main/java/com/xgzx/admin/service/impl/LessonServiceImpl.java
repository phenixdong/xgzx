package com.xgzx.admin.service.impl;

import com.xgzx.admin.entity.LessonContent;
import com.xgzx.admin.entity.LessonWatch;
import com.xgzx.admin.entity.User;
import com.xgzx.admin.entity.Lesson;
import com.xgzx.admin.mapper.LessonMapper;
import com.xgzx.admin.service.LessonContentService;
import com.xgzx.admin.service.LessonService;
import com.xgzx.admin.service.LessonWatchService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import com.xgzx.base.BaseErrResult;
import com.xgzx.base.BaseResult;
import com.xgzx.exception.CommonError;
import com.xgzx.util.JsonObjUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DaiDong
 * @since 2018-01-05
 * @email 755144556@qq.com
 */
@Service
@Transactional
public class LessonServiceImpl extends ServiceImpl<LessonMapper, Lesson> implements LessonService {
	
	@Autowired
	LessonMapper lessonMapper;
	@Autowired
	LessonContentService lessonContentService;
	@Autowired
	LessonWatchService lessonWatchService;
	
	@Override
	public BaseResult customQuery(int id) {
		Lesson lesson = lessonMapper.customQuery(id);
		return new BaseResult("1", "成功", lesson);
	}

	@Override
	public int insertReturnId(Lesson lesson) {
		return lessonMapper.insertReturnId(lesson);
	}

	@Override
	public BaseResult addWatchRecord(Map<String, Object> param) {
		String userId = (String) param.get("userId");
		Integer lessonId = (Integer) param.get("lessonId");
		// 增加用户-课程观看记录
		LessonWatch lessonWatch = new LessonWatch();
		lessonWatch.setLessonId(lessonId);
		lessonWatch.setUserId(userId);
		lessonWatchService.insert(lessonWatch);
		
		// 观看次数+1
		Lesson lesson = lessonMapper.selectById(lessonId);
		lesson.setWatchNo(lesson.getWatchNo() + 1);
		lessonMapper.updateById(lesson);
		return new BaseResult();
	}
	
	@Override
	public BaseResult selectLessonList(Page<Lesson> page, 
			Map<String, Object> lessonMap) {
		List<Lesson> list = lessonMapper.selectLessonList(page, lessonMap); 
		page.setRecords(list);
		return new BaseResult(page);
	}
	
	@Override
	public BaseResult getWatchList(Page<Lesson> page, Map<String, Object> lessonMap) {
		List<Lesson> list = lessonMapper.getWatchList(page, lessonMap); 
		page.setRecords(list);
		return new BaseResult(page);
	}
	
	@Override
	public BaseResult insert(Map<String, Object> param) {
		Lesson lesson = null;
		try {
			lesson = JsonObjUtils.map2obj(param, Lesson.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		// 新增课程
		lesson.setValidTag(1);
		lesson.setWatchNo(0);
		Date nowTime = new Date();
		lesson.setCreateTime(nowTime);
		lesson.setUpdateTime(nowTime);
		int ret = lessonMapper.insertReturnId(lesson);
        if (ret <= 0) {
        	return new BaseResult("0", "失败", null);
        }  
        int lessonId = lesson.getLessonId();
		
        // 新增目录
        List<LessonContent> contentList = null;
        try {
        	if (null != param.get("contentList")) {
        		contentList = JsonObjUtils.map2List(param, "contentList", LessonContent.class);
        	}
		} catch (Exception e) {
			e.printStackTrace();
			CommonError.CommonErr(new BaseErrResult("-1", e.getMessage()));
		}
        if (null != contentList && contentList.size() > 0) {
	        List<LessonContent> newList = new ArrayList<>();
	        for (LessonContent item : contentList) {
	        	item.setLessonId(lessonId);
	        	item.setValidTag(1);
	        	newList.add(item);
	        }
	        lessonContentService.insertBatch(newList);
        }
        
        return new BaseResult("1", "成功", lesson);
	}
	
	public BaseResult update(Map<String, Object> param) {
		Lesson lesson = null;
		try {
			lesson = JsonObjUtils.map2obj(param, Lesson.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		// 更新lesson
		int lessonId = lesson.getLessonId();
		int ret = lessonMapper.updateById(lesson); 
		if (ret <= 0) {
			return new BaseResult("0", "失败", null);
		}
        
		// 删除旧目录
		Map<String, Object> map = new HashMap<>();
		map.put("lesson_id", lessonId);
		lessonContentService.deleteByMap(map);
		
		// 新增新目录
		List<LessonContent> contentList = null;
        try {
        	if (null != param.get("lessonContentList")) {
        		contentList = JsonObjUtils.map2List(param, "lessonContentList", LessonContent.class);
        	}
		} catch (Exception e) {
			e.printStackTrace();
			CommonError.CommonErr(new BaseErrResult("-1", e.getMessage()));
		}
        if (null != contentList && contentList.size() > 0) {
	        List<LessonContent> newList = new ArrayList<>();
	        for (LessonContent item : contentList) {
	        	item.setLessonId(lessonId);
	        	item.setValidTag(1);
	        	newList.add(item);
	        }
	        lessonContentService.insertBatch(newList);
        }
        return new BaseResult("1", "成功", null);
	}
}
