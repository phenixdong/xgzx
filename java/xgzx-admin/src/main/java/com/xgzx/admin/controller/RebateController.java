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
import com.xgzx.admin.entity.Rebate;
import com.xgzx.admin.service.RebateService;
import com.xgzx.base.BaseResult;
import com.xgzx.util.JsonObjUtils;

/**
 * @author DaiDong
 * @since 2018-01-09
 * @email 755144556@qq.com
 */
@RestController
@RequestMapping("/rebate")
public class RebateController {
	
	@Autowired
	RebateService rebateService;

	/**
     * 新增
     */
	@RequestMapping(value="/insert", method={RequestMethod.POST})
   	public BaseResult insert(@RequestBody Map<String, Object> param) {
		Rebate rebate = null;
		try {
			rebate = JsonObjUtils.map2obj(param, Rebate.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		boolean success = rebateService.insert(rebate); 
        if (success) {
        	return new BaseResult("1", "成功", rebate);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }

	/**
     * 删除
     */
	@RequestMapping(value="/delete/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult delete(@PathVariable("id") String id) {
		boolean success = rebateService.deleteById(Long.valueOf(id)); 
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
		
		boolean success = rebateService.deleteBatchIds(idList); 
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
		Rebate rebate = null;
		try {
			rebate = JsonObjUtils.map2obj(param, Rebate.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		boolean success = rebateService.updateById(rebate); 
        if (success) {
        	return new BaseResult("1", "成功", rebate);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
    
    /**
     * 查询
     */
	@RequestMapping(value="/select/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult selectById(@PathVariable("id") String id) {
		Rebate rebate = rebateService.selectById(Long.valueOf(id)); 
        return new BaseResult("1", "成功", rebate);
    }
    
    /**
     * 分页查询
     */
	@RequestMapping(value="/selectPage", method={RequestMethod.POST})
   	public BaseResult selectPage(@RequestBody Map<String, Object> param) {
   		Page<Rebate> page = null;
   		Rebate rebate = null;
   		try {
	   		page = JsonObjUtils.map2obj(param, "page", Page.class);
			rebate = JsonObjUtils.map2obj(param, "rebate", Rebate.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		Page<Rebate> pageRebate = rebateService.selectPage(page, 
				new EntityWrapper<>(rebate)); 
        return new BaseResult("1", "成功", pageRebate);
    }
	
	/**
     * 查询userId对应的rebate
     */
	@RequestMapping(value="/getRebateInfo", method={RequestMethod.POST})
   	public BaseResult getRebateInfo(@RequestBody Map<String, Object> param) throws Exception {
		return rebateService.getRebateInfo(param);
    }
    
    /**
     * 自定义查询
     */
    @RequestMapping(value="/customQuery/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult customQuery(@PathVariable("id") int id) {
		BaseResult baseResult = rebateService.customQuery(id); 
        return baseResult;
    }
	
}
