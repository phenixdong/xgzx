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

import com.xgzx.base.BaseResult;
import com.xgzx.util.JsonObjUtils;
import com.xgzx.admin.service.RebateWithdrawService;
import com.xgzx.admin.entity.RebateWithdraw;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author DaiDong
 * @since 2018-01-09
 * @email 755144556@qq.com
 */
@RestController
@RequestMapping("/rebateWithdraw")
public class RebateWithdrawController {
	
	@Autowired
	RebateWithdrawService rebateWithdrawService;

	/**
     * 新增
     */
	@RequestMapping(value="/insert", method={RequestMethod.POST})
   	public BaseResult insertRebateWithdraw(@RequestBody Map<String, Object> param) throws Exception {
		return rebateWithdrawService.insertRebateWithdraw(param);
    }

	/**
     * 删除
     */
	@RequestMapping(value="/delete", method={RequestMethod.POST})
   	public BaseResult deleteRebateWithdraw(@RequestBody Map<String, Object> param) throws Exception {
		return rebateWithdrawService.deleteRebateWithdraw(param);
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
		
		boolean success = rebateWithdrawService.deleteBatchIds(idList); 
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
		RebateWithdraw rebateWithdraw = JsonObjUtils.map2obj(param, RebateWithdraw.class);
		if (2 == rebateWithdraw.getStatus()) {
			rebateWithdraw.setPayTime(new Date());
		}
		boolean success = rebateWithdrawService.updateById(rebateWithdraw); 
        if (success) {
        	return new BaseResult("1", "成功", rebateWithdraw);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
	
	/**
     * 支付给取现账户
     */
	@RequestMapping(value="/pay", method={RequestMethod.POST})
   	public BaseResult pay(@RequestBody Map<String, Object> param) throws Exception {
		return rebateWithdrawService.pay(param);
    }
	
	/**
     * 更新状态为已完成
     */
	@RequestMapping(value="/finishWithdraw", method={RequestMethod.POST})
   	public BaseResult finishWithdraw(@RequestBody Map<String, Object> param) throws Exception {
		RebateWithdraw rebateWithdraw = new RebateWithdraw();
		rebateWithdraw.setRebateWithdrawId((Integer) param.get("rebateWithdrawId"));
		rebateWithdraw.setStatus(3);// 1未支付，2待领取，3已领取，4已退款
		rebateWithdraw.setPayTime(new Date());
		boolean success = rebateWithdrawService.updateById(rebateWithdraw);
		if (success) {
			return new BaseResult();
		} else {
			return new BaseResult("0", "失败", null);
		}
    }
	
	
    /**
     * 查询
     */
	@RequestMapping(value="/select/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult selectById(@PathVariable("id") String id) {
		RebateWithdraw rebateWithdraw = rebateWithdrawService.selectById(Long.valueOf(id)); 
        return new BaseResult("1", "成功", rebateWithdraw);
    }
    
    /**
     * 分页查询
     */
	@RequestMapping(value="/selectPage", method={RequestMethod.POST})
   	public BaseResult selectPage(@RequestBody Map<String, Object> param) throws Exception {
		Page<RebateWithdraw> page = JsonObjUtils.map2obj(param, "page", Page.class);
		Map<String, Object> rebateWithdrawMap = JsonObjUtils.map2obj(param, 
				"rebateWithdraw", Map.class);
		if (page == null) {
			page = new Page<RebateWithdraw>(1, 10);
		}  
		List<String> timeRangeList = (List<String>) param.get("createTimeRange");
		if (null != timeRangeList && timeRangeList.size() > 0) {
			rebateWithdrawMap.put("startTime", timeRangeList.get(0));
			rebateWithdrawMap.put("endTime", timeRangeList.get(1));
		}
		return rebateWithdrawService.selectRebateWithdrawList(page, rebateWithdrawMap);
    }
    
    /**
     * 自定义查询
     */
    @RequestMapping(value="/customQuery/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult customQuery(@PathVariable("id") int id) {
		BaseResult baseResult = rebateWithdrawService.customQuery(id); 
        return baseResult;
    }
	
}
