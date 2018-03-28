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

import com.xgzx.base.BaseErrResult;
import com.xgzx.base.BaseResult;
import com.xgzx.exception.CommonError;
import com.xgzx.util.JsonObjUtils;
import com.xgzx.admin.service.TeacherService;
import com.xgzx.admin.service.UserMemberService;
import com.xgzx.admin.entity.LessonCategory;
import com.xgzx.admin.entity.Teacher;
import com.xgzx.admin.entity.UserMember;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author DaiDong
 * @since 2018-01-05
 * @email 755144556@qq.com
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {
	
	@Autowired
	TeacherService teacherService;
	@Autowired
	UserMemberService userMemberService;

	/**
     * 新增
     */
	@RequestMapping(value="/insert", method={RequestMethod.POST})
   	public BaseResult insert(@RequestBody Map<String, Object> param) throws Exception {
		Teacher teacher  = JsonObjUtils.map2obj(param, Teacher.class);
		teacher.setValidTag(1);
		Date nowTime = new Date();
		teacher.setCreateTime(nowTime);
		teacher.setUpdateTime(nowTime);
		boolean success = teacherService.insert(teacher); 
        if (success) {
        	return new BaseResult("1", "成功", teacher);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }

	/**
     * 删除
     */
	@RequestMapping(value="/delete/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult delete(@PathVariable("id") String id) {
		//boolean success = teacherService.deleteById(Long.valueOf(id));
		Teacher teacher = teacherService.selectById(id);
		teacher.setValidTag(0);
		boolean success = teacherService.updateById(teacher);
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
		
		//boolean success = teacherService.deleteBatchIds(idList);
   		for (String id : idList) {
   			Teacher teacher = teacherService.selectById(id);
			teacher.setValidTag(0);
			boolean success = teacherService.updateById(teacher);
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
		Teacher teacher = null;
		try {
			teacher = JsonObjUtils.map2obj(param, Teacher.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		if ("".equals(teacher.getDescription())) {
			teacher.setDescription(" ");// 填入空格，解决填入之后，再次修改，不能清空的问题
		}
		if ("".equals(teacher.getPosition())) {
			teacher.setPosition(" ");// 填入空格，解决填入之后，再次修改，不能清空的问题
		}
		boolean success = teacherService.updateById(teacher); 
        if (success) {
        	return new BaseResult("1", "成功", teacher);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
    
    /**
     * 查询
     */
	@RequestMapping(value="/select/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult selectById(@PathVariable("id") String id) {
		Teacher teacher = teacherService.selectById(Long.valueOf(id)); 
        return new BaseResult("1", "成功", teacher);
    }
    
    /**
     * 分页查询
     */
	@RequestMapping(value="/selectPage", method={RequestMethod.POST})
   	public BaseResult selectPage(@RequestBody Map<String, Object> param) throws Exception {
   		Page<Teacher> page = JsonObjUtils.map2obj(param, "page", Page.class);
   		Teacher teacher = JsonObjUtils.map2obj(param, "teacher", Teacher.class);
   		EntityWrapper wrapper = new EntityWrapper<>(teacher);
   		wrapper.orderBy("update_time", false);
   		wrapper.where("valid_tag = 1", "");
		Page<Teacher> pageTeacher = teacherService.selectPage(page, wrapper); 
        return new BaseResult("1", "成功", pageTeacher);
    }
    
    /**
     * 自定义查询
     */
    @RequestMapping(value="/customQuery/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult customQuery(@PathVariable("id") int id) {
		BaseResult baseResult = teacherService.customQuery(id); 
        return baseResult;
    }
    
    /**
     * 统计讲师收益
     */
	@RequestMapping(value="/statisticRebate", method={RequestMethod.POST})
   	public BaseResult statisticRebate(@RequestBody Map<String, Object> param) throws Exception {
   		return teacherService.statisticRebate(param);
    }
	
}
