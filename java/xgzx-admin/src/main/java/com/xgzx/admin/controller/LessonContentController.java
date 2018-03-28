package com.xgzx.admin.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import com.xgzx.base.BaseResult;
import com.xgzx.util.JsonObjUtils;
import com.xgzx.admin.service.LessonContentService;
import com.xgzx.admin.entity.LessonContent;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author DaiDong
 * @since 2018-01-09
 * @email 755144556@qq.com
 */
@RestController
@RequestMapping("/lessonContent")
public class LessonContentController {
	
	@Autowired
	LessonContentService lessonContentService;

	/**
     * 新增
     */
	@RequestMapping(value="/insert", method={RequestMethod.POST})
   	public BaseResult insert(@RequestBody Map<String, Object> param) {
		LessonContent lessonContent = null;
		try {
			lessonContent = JsonObjUtils.map2obj(param, LessonContent.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		boolean success = lessonContentService.insert(lessonContent); 
        if (success) {
        	return new BaseResult("1", "成功", lessonContent);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }

	/**
     * 删除
     */
	@RequestMapping(value="/delete/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult delete(@PathVariable("id") String id) {
		boolean success = lessonContentService.deleteById(Long.valueOf(id)); 
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
		
		boolean success = lessonContentService.deleteBatchIds(idList); 
        if (success) {
        	return new BaseResult("1", "成功", null);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }

	/**
     * 修改
     */
	@RequestMapping(value="/update", method={RequestMethod.POST})
   	public BaseResult update(@RequestBody Map<String, Object> param) {
		LessonContent lessonContent = null;
		try {
			lessonContent = JsonObjUtils.map2obj(param, LessonContent.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		boolean success = lessonContentService.updateById(lessonContent); 
        if (success) {
        	return new BaseResult("1", "成功", lessonContent);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
    
    /**
     * 查询
     */
	@RequestMapping(value="/select/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult selectById(@PathVariable("id") String id) {
		LessonContent lessonContent = lessonContentService.selectById(Long.valueOf(id)); 
        return new BaseResult("1", "成功", lessonContent);
    }
    
    /**
     * 分页查询
     */
	@RequestMapping(value="/selectPage", method={RequestMethod.POST})
   	public BaseResult selectPage(@RequestBody Map<String, Object> param) {
   		Page<LessonContent> page = null;
   		LessonContent lessonContent = null;
   		try {
	   		page = JsonObjUtils.map2obj(param, "page", Page.class);
			lessonContent = JsonObjUtils.map2obj(param, "lessonContent", LessonContent.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		Page<LessonContent> pageLessonContent = lessonContentService.selectPage(page, 
				new EntityWrapper<>(lessonContent)); 
        return new BaseResult("1", "成功", pageLessonContent);
    }
    
    /**
     * 自定义查询
     */
    @RequestMapping(value="/customQuery/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult customQuery(@PathVariable("id") int id) {
		BaseResult baseResult = lessonContentService.customQuery(id); 
        return baseResult;
    }
	
}
