package com.xgzx.admin.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.xgzx.admin.entity.Lesson;
import com.xgzx.admin.entity.LessonContent;
import com.xgzx.admin.entity.Teacher;
import com.xgzx.admin.service.LessonContentService;
import com.xgzx.admin.service.LessonService;
import com.xgzx.admin.service.TeacherService;
import com.xgzx.admin.service.VideoService;
import com.xgzx.base.BaseErrResult;
import com.xgzx.base.BaseResult;
import com.xgzx.exception.CommonError;
import com.xgzx.util.JsonObjUtils;

/**
 * @author DaiDong
 * @since 2018-01-05
 * @email 755144556@qq.com
 */
@RestController
@RequestMapping("/lesson")
public class LessonController {
	
	@Autowired
	LessonService lessonService;
	@Autowired
	LessonContentService lessonContentService;
	@Autowired
	TeacherService teacherService;
	@Autowired
	VideoService viderService;

	/**
     * 增加观看记录
     */
	@RequestMapping(value="/addWatchRecord", method={RequestMethod.POST})
   	public BaseResult addWatchRecord(@RequestBody Map<String, Object> param) {
		return lessonService.addWatchRecord(param);
    }
	
	/**
     * 新增 
     */
	@RequestMapping(value="/insert", method={RequestMethod.POST})
   	public BaseResult insert(@RequestBody Map<String, Object> param) {
		return lessonService.insert(param);
    }

	/**
     * 删除
     */
	@RequestMapping(value="/delete/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult delete(@PathVariable("id") String id) {
		boolean success = lessonService.deleteById(Long.valueOf(id)); 
        if (success) {
        	return new BaseResult("1", "成功", null);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
    
    /**
     * 批量删除
     */
	@RequestMapping(value="/deleteBatch", method={RequestMethod.POST})
   	public BaseResult deleteBatch(@RequestBody Map<String, Object> param) {
		List<String> idList;
   		try {
			idList = JsonObjUtils.map2List(param, "ids", String.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
   		
		// boolean success = lessonService.deleteBatchIds(idList); 
   		for (String id : idList) {
   			Lesson lesson = lessonService.selectById(id);
   			lesson.setValidTag(0);
			boolean success = lessonService.updateById(lesson);
			if (!success) {
				CommonError.CommonErr(new BaseErrResult("-1", "删除失败"));
			}
		}
   		return new BaseResult("1", "成功", null);
    }

	/**
     * 修改
     */
	@RequestMapping(value="/update", method={RequestMethod.POST})
   	public BaseResult update(@RequestBody Map<String, Object> param) {
		return lessonService.update(param);
    }
    
    /**
     * 查询
     */
	@RequestMapping(value="/select/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult selectById(@PathVariable("id") String id) {
		Lesson lesson = lessonService.selectById(Long.valueOf(id));
		// 查询讲师
		Teacher teacher = teacherService.selectById(lesson.getTeacherId());
		lesson.setTeacher(teacher);
		// 查询课程目录
		Map<String, Object> map = new HashMap<>();
		map.put("lesson_id", lesson.getLessonId());
		List<LessonContent> lessonContentList = lessonContentService.selectByMap(map);
		lesson.setLessonContentList(lessonContentList);
		// 查询视频
		if (null != lesson.getVideoId() && !"0".equals(lesson.getVideoId())) {
			BaseResult result = viderService.getVideoInfo(lesson.getVideoId());
			if ("1".equals(result.returnCode)) {
				String resp = (String) result.getData();
				Map<String, Object> respMap = null;
				try {
					respMap = JsonObjUtils.json2map(resp);
				} catch (Exception e) {
					e.printStackTrace();
					return new BaseResult("0", e.getMessage(), null);
				}
				Map<String, Object> videoMap = (Map<String, Object>) respMap.get("Video");
				String title = (String)videoMap.get("Title");
				String coverUrl = (String)videoMap.get("CoverURL");
				lesson.getVideo().setTitle(title);
				lesson.getVideo().setCoverUrl(coverUrl);
			}  
		}
		return new BaseResult(lesson);
    }
    
    /**
     * 分页查询
     */
	@RequestMapping(value="/selectPage", method={RequestMethod.POST})
   	public BaseResult selectPage(@RequestBody Map<String, Object> param) throws Exception {
		Page<Lesson> page = JsonObjUtils.map2obj(param, "page", Page.class);
		Map<String, Object> lessonMap = JsonObjUtils.map2obj(param, "lesson", Map.class);
		if (page == null) {
			page = new Page<Lesson>(1, 10);
		}  
		List<String> timeRangeList = (List<String>) param.get("createTimeRange");
		if (null != timeRangeList && timeRangeList.size() > 0) {
			lessonMap.put("startTime", timeRangeList.get(0));
			lessonMap.put("endTime", timeRangeList.get(1));
		}
		// 排序，1推荐，2最热，3免费
		if (null != param.get("orderType")) {
			Integer orderType = (Integer) param.get("orderType");
			if (1 == orderType) {
				lessonMap.put("orderSql", "order_no desc");
			} else if (2 == orderType) {
				lessonMap.put("orderSql", "watch_no desc");
			} else if (3 == orderType) {
				// 收费类型，1免费，2会员免费，3单独收费
				lessonMap.put("chargeType", 1);
				lessonMap.put("orderSql", "order_no desc");
			}
		} else {
			lessonMap.put("orderSql", "order_no desc");
		}
		
		return lessonService.selectLessonList(page, lessonMap);
    }
	
	/**
     * 查询观看记录
     */
	@RequestMapping(value="/getWatchList", method={RequestMethod.POST})
   	public BaseResult getWatchList(@RequestBody Map<String, Object> param) throws Exception {
		Page<Lesson> page = JsonObjUtils.map2obj(param, "page", Page.class);
		if (page == null) {
			page = new Page<Lesson>(1, 10);
		}  
		String userId = (String) param.get("userId");
		Map<String, Object> lessonMap = new HashMap<>();
		lessonMap.put("userId", userId);
		return lessonService.getWatchList(page, lessonMap);
    }
    
    /**
     * 自定义查询
     */
    @RequestMapping(value="/customQuery/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult customQuery(@PathVariable("id") int id) {
		BaseResult baseResult = lessonService.customQuery(id); 
        return baseResult;
    }
	
}
