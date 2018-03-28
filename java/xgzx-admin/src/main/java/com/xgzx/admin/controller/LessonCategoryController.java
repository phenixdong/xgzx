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
import java.util.Date;
import java.util.HashMap;

import com.xgzx.base.BaseErrResult;
import com.xgzx.base.BaseResult;
import com.xgzx.exception.CommonError;
import com.xgzx.util.JsonObjUtils;
import com.xgzx.admin.service.LessonCategoryService;
import com.xgzx.admin.entity.LessonCategory;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author DaiDong
 * @since 2018-01-09
 * @email 755144556@qq.com
 */
@RestController
@RequestMapping("/lessonCategory")
public class LessonCategoryController {
	
	@Autowired
	LessonCategoryService lessonCategoryService;

	/**
     * 新增
     */
	@RequestMapping(value="/insert", method={RequestMethod.POST})
   	public BaseResult insert(@RequestBody Map<String, Object> param) throws Exception {
		LessonCategory lessonCategory = JsonObjUtils.map2obj(param, LessonCategory.class);
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("category_name", lessonCategory.getCategoryName());
		queryMap.put("valid_tag", 1);
		List<LessonCategory> list = lessonCategoryService.selectByMap(queryMap);
		if (null != list && list.size() > 0) {
			return new BaseResult("0", "名称不能重复", null);
		}
		lessonCategory.setValidTag(1);
		Date nowTime = new Date();
		lessonCategory.setCreateTime(nowTime);
		lessonCategory.setUpdateTime(nowTime);
		boolean success = lessonCategoryService.insert(lessonCategory); 
        if (success) {
        	return new BaseResult("1", "成功", lessonCategory);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }

	/**
     * 删除
     */
	@RequestMapping(value="/delete/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult delete(@PathVariable("id") String id) {
		//boolean success = lessonCategoryService.deleteById(Long.valueOf(id)); 
		LessonCategory lessonCategory = lessonCategoryService.selectById(id);
		lessonCategory.setValidTag(0);
		boolean success = lessonCategoryService.updateById(lessonCategory);
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
    	
		//boolean success = lessonCategoryService.deleteBatchIds(idList);
		for (String id : idList) {
			LessonCategory lessonCategory = lessonCategoryService.selectById(id);
			lessonCategory.setValidTag(0);
			boolean success = lessonCategoryService.updateById(lessonCategory);
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
		LessonCategory lessonCategory = null;
		try {
			lessonCategory = JsonObjUtils.map2obj(param, LessonCategory.class);
			lessonCategory.setValidTag(1);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("category_name", lessonCategory.getCategoryName());
		queryMap.put("valid_tag", 1);
		List<LessonCategory> list = lessonCategoryService.selectByMap(queryMap);
		if (null != list && list.size() > 0) {
			LessonCategory item = list.get(0);
			if (!item.getLessonCategoryId().equals(lessonCategory.getLessonCategoryId())) {
				return new BaseResult("0", "名称不能重复", null);
			}
		}
		if ("".equals(lessonCategory.getDescription())) {
			lessonCategory.setDescription(" ");// 填入空格，解决填入之后，再次修改，不能清空的问题
		}
		boolean success = lessonCategoryService.updateById(lessonCategory); 
        if (success) {
        	return new BaseResult("1", "成功", lessonCategory);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
    
    /**
     * 查询
     */
	@RequestMapping(value="/select/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult selectById(@PathVariable("id") String id) {
		LessonCategory lessonCategory = lessonCategoryService.selectById(Long.valueOf(id)); 
        return new BaseResult("1", "成功", lessonCategory);
    }
    
    /**
     * 分页查询
     */
	@RequestMapping(value="/selectPage", method={RequestMethod.POST})
   	public BaseResult selectPage(@RequestBody Map<String, Object> param) {
   		Page<LessonCategory> page = null;
   		LessonCategory lessonCategory = null;
   		try {
	   		page = JsonObjUtils.map2obj(param, "page", Page.class);
			lessonCategory = JsonObjUtils.map2obj(param, "lessonCategory", LessonCategory.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		} 
   		EntityWrapper wrapper = new EntityWrapper<>(lessonCategory);
   		wrapper.orderBy("order_no", false);
   		wrapper.where("valid_tag = 1", "");
		Page<LessonCategory> pageCategory = lessonCategoryService.selectPage(page, wrapper); 
        return new BaseResult("1", "成功", pageCategory);
    }
    
    /**
     * 自定义查询
     */
    @RequestMapping(value="/customQuery/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult customQuery(@PathVariable("id") int id) {
		BaseResult baseResult = lessonCategoryService.customQuery(id); 
        return baseResult;
    }
	
}
