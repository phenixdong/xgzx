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
import com.xgzx.admin.service.RebateTransactionService;
import com.xgzx.admin.entity.RebateTransaction;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author DaiDong
 * @since 2018-01-09
 * @email 755144556@qq.com
 */
@RestController
@RequestMapping("/rebateTransaction")
public class RebateTransactionController {
	
	@Autowired
	RebateTransactionService rebateTransactionService;

	/**
     * 新增
     */
	@RequestMapping(value="/insert", method={RequestMethod.POST})
   	public BaseResult insert(@RequestBody Map<String, Object> param) {
		RebateTransaction rebateTransaction = null;
		try {
			rebateTransaction = JsonObjUtils.map2obj(param, RebateTransaction.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		boolean success = rebateTransactionService.insert(rebateTransaction); 
        if (success) {
        	return new BaseResult("1", "成功", rebateTransaction);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }

	/**
     * 删除
     */
	@RequestMapping(value="/delete/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult delete(@PathVariable("id") String id) {
		boolean success = rebateTransactionService.deleteById(Long.valueOf(id)); 
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
		
		boolean success = rebateTransactionService.deleteBatchIds(idList); 
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
		RebateTransaction rebateTransaction = null;
		try {
			rebateTransaction = JsonObjUtils.map2obj(param, RebateTransaction.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		boolean success = rebateTransactionService.updateById(rebateTransaction); 
        if (success) {
        	return new BaseResult("1", "成功", rebateTransaction);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
    
    /**
     * 查询
     */
	@RequestMapping(value="/select/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult selectById(@PathVariable("id") String id) {
		RebateTransaction rebateTransaction = rebateTransactionService.selectById(Long.valueOf(id)); 
        return new BaseResult("1", "成功", rebateTransaction);
    }
    
    /**
     * 分页查询
     */
	@RequestMapping(value="/selectPage", method={RequestMethod.POST})
   	public BaseResult selectPage(@RequestBody Map<String, Object> param) throws Exception {
		Page<RebateTransaction> page = JsonObjUtils.map2obj(param, "page", Page.class);
		Map<String, Object> transactionMap = JsonObjUtils.map2obj(param, 
				"rebateTransaction", Map.class);
		if (page == null) {
			page = new Page<RebateTransaction>(1, 10);
		}  
		List<String> timeRangeList = (List<String>) param.get("createTimeRange");
		if (null != timeRangeList && timeRangeList.size() > 0) {
			transactionMap.put("startTime", timeRangeList.get(0));
			transactionMap.put("endTime", timeRangeList.get(1));
		}
		return rebateTransactionService.selectRebateTransactionList(page, transactionMap);
    }
    
    /**
     * 自定义查询
     */
    @RequestMapping(value="/customQuery/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult customQuery(@PathVariable("id") int id) {
		BaseResult baseResult = rebateTransactionService.customQuery(id); 
        return baseResult;
    }
	
}
