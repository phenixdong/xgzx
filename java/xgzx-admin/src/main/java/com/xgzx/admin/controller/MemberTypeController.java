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
import com.xgzx.admin.service.MemberTypeService;
import com.xgzx.admin.entity.LessonCategory;
import com.xgzx.admin.entity.MemberType;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author DaiDong
 * @since 2018-01-10
 * @email 755144556@qq.com
 */
@RestController
@RequestMapping("/memberType")
public class MemberTypeController {
	
	@Autowired
	MemberTypeService memberTypeService;

	/**
     * 新增
     */
	@RequestMapping(value="/insert", method={RequestMethod.POST})
   	public BaseResult insert(@RequestBody Map<String, Object> param) {
		MemberType memberType = null;
		try {
			memberType = JsonObjUtils.map2obj(param, MemberType.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		memberType.setValidTag(1);
		Date nowTime = new Date();
		memberType.setCreateTime(nowTime);
		memberType.setUpdateTime(nowTime);
		boolean success = memberTypeService.insert(memberType); 
        if (success) {
        	return new BaseResult("1", "成功", memberType);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }

	/**
     * 删除
     */
	@RequestMapping(value="/delete/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult delete(@PathVariable("id") String id) {
		boolean success = memberTypeService.deleteById(Long.valueOf(id)); 
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
		
		//boolean success = memberTypeService.deleteBatchIds(idList); 
   		for (String id : idList) {
   			MemberType memberType = memberTypeService.selectById(id);
   			memberType.setValidTag(0);
			boolean success = memberTypeService.updateById(memberType);
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
		MemberType memberType = null;
		try {
			memberType = JsonObjUtils.map2obj(param, MemberType.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		boolean success = memberTypeService.updateById(memberType); 
        if (success) {
        	return new BaseResult("1", "成功", memberType);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
    
    /**
     * 查询
     */
	@RequestMapping(value="/select/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult selectById(@PathVariable("id") String id) {
		MemberType memberType = memberTypeService.selectById(Long.valueOf(id)); 
        return new BaseResult("1", "成功", memberType);
    }
    
    /**
     * 分页查询
     */
	@RequestMapping(value="/selectPage", method={RequestMethod.POST})
   	public BaseResult selectPage(@RequestBody Map<String, Object> param) throws Exception {
		Page<MemberType> page = new Page();
		MemberType memberType = new MemberType();
		EntityWrapper wrapper = new EntityWrapper<>(memberType);
   		wrapper.where("valid_tag = 1", "");
   		wrapper.orderBy("price", true);
		Page<MemberType> pageMemberType = memberTypeService.selectPage(
				page, wrapper); 
		List<MemberType> list = pageMemberType.getRecords();
		for (MemberType item : list) {
			if (null != item.getDescription() && !"".equals(item.getDescription())) {
				List<String> descriptionLis = Arrays.asList(item.getDescription().split("；"));
				item.setDescriptionList(descriptionLis);
			}
		}
        return new BaseResult("1", "成功", list);
    }
    
    /**
     * 自定义查询
     */
    @RequestMapping(value="/customQuery/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult customQuery(@PathVariable("id") int id) {
		BaseResult baseResult = memberTypeService.customQuery(id); 
        return baseResult;
    }
	
}
