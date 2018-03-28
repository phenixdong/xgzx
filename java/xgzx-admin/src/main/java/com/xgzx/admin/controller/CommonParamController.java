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

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xgzx.admin.entity.CommonParam;
import com.xgzx.admin.service.CommonParamService;
import com.xgzx.base.BaseResult;
import com.xgzx.util.JsonObjUtils;

/**
 * @author DaiDong
 * @since 2018-01-14
 * @email 755144556@qq.com
 */
@RestController
@RequestMapping("/commonParam")
public class CommonParamController {
	
	@Autowired
	CommonParamService commonParamService;

	/**
     * 修改推荐人返点
     */
	@RequestMapping(value="/updateRebateRate", method={RequestMethod.POST})
   	public BaseResult updateRebateRate(@RequestBody Map<String, Object> param) {
		Integer rebateRate = (Integer) param.get("rebateRate");
		
		CommonParam commonParam = new CommonParam();
		commonParam.setParamName("rebateRate");
		EntityWrapper wrapper = new EntityWrapper<>(commonParam);
		commonParam = commonParamService.selectOne(wrapper);
		
		// 更新
		commonParam.setParamValue(rebateRate + "");
		boolean success = commonParamService.updateById(commonParam);
        if (success) {
        	return new BaseResult("1", "成功", rebateRate);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
	
	/**
     * 修改讲师返点
     */
	@RequestMapping(value="/updateTeacherRebateRate", method={RequestMethod.POST})
   	public BaseResult updateTeacherRebateRate(@RequestBody Map<String, Object> param) {
		Integer teacherRebateRate = (Integer) param.get("teacherRebateRate");
		
		CommonParam commonParam = new CommonParam();
		commonParam.setParamName("teacherRebateRate");
		EntityWrapper wrapper = new EntityWrapper<>(commonParam);
		commonParam = commonParamService.selectOne(wrapper);
		
		// 更新
		commonParam.setParamValue(teacherRebateRate + "");
		boolean success = commonParamService.updateById(commonParam);
        if (success) {
        	return new BaseResult("1", "成功", teacherRebateRate);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
	
	/**
     * 全部配置信息，用map返回
     */
	@RequestMapping(value="/getAll", method={RequestMethod.GET})
   	public BaseResult getAll() {
		List<CommonParam> list = commonParamService.selectList(new EntityWrapper<>(new CommonParam()));
		Map<String, Object> map = new HashMap<>();
		for (CommonParam item : list) {
			if ("rebateRate".equals(item.getParamName()) 
				|| "teacherRebateRate".equals(item.getParamName())) {
				// rebateRate的值，要String转数字
				map.put(item.getParamName(), Integer.valueOf(item.getParamValue()));
			} else {
				map.put(item.getParamName(), item.getParamValue());
			}
		}
		return new BaseResult(map);
    }
	
	/**
     * 新增
     */
	@RequestMapping(value="/insert", method={RequestMethod.POST})
   	public BaseResult insert(@RequestBody Map<String, Object> param) {
		CommonParam commonParam = null;
		try {
			commonParam = JsonObjUtils.map2obj(param, CommonParam.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		boolean success = commonParamService.insert(commonParam); 
        if (success) {
        	return new BaseResult("1", "成功", commonParam);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }

	/**
     * 删除
     */
	@RequestMapping(value="/delete/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult delete(@PathVariable("id") String id) {
		boolean success = commonParamService.deleteById(Long.valueOf(id)); 
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
		
		boolean success = commonParamService.deleteBatchIds(idList); 
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
   	public BaseResult update(@RequestBody Map<String, Object> param) throws Exception {
		CommonParam commonParam =  JsonObjUtils.map2obj(param, CommonParam.class);
		boolean success = commonParamService.updateById(commonParam); 
        if (success) {
        	return new BaseResult("1", "成功", commonParam);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
    
    /**
     * 查询
     */
	@RequestMapping(value="/select/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult selectById(@PathVariable("id") String id) {
		CommonParam commonParam = commonParamService.selectById(Long.valueOf(id)); 
        return new BaseResult("1", "成功", commonParam);
    }
    
    /**
     * 分页查询
     */
	@RequestMapping(value="/selectPage", method={RequestMethod.POST})
   	public BaseResult selectPage(@RequestBody Map<String, Object> param) throws Exception {
   		Page<CommonParam> page = JsonObjUtils.map2obj(param, "page", Page.class);
   		CommonParam commonParam = JsonObjUtils.map2obj(param, "commonParam", CommonParam.class);
		Page<CommonParam> pageCommonParam = commonParamService.selectPage(page, 
				new EntityWrapper<>(commonParam)); 
        return new BaseResult("1", "成功", pageCommonParam);
    }
    
    /**
     * 自定义查询
     */
    @RequestMapping(value="/customQuery/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult customQuery(@PathVariable("id") int id) {
		BaseResult baseResult = commonParamService.customQuery(id); 
        return baseResult;
    }
	
}
