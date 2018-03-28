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
import com.xgzx.base.BaseResult;
import com.xgzx.base.BaseErrResult;
import com.xgzx.util.JsonObjUtils;
import com.xgzx.exception.CommonError;
import com.xgzx.admin.service.BulkBuyRegisterService;
import com.xgzx.admin.entity.BulkBuyRegister;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author DaiDong
 * @since 2018-03-17
 * @email 755144556@qq.com
 */
@RestController
@RequestMapping("/bulkBuyRegister")
public class BulkBuyRegisterController {
	
	@Autowired
	BulkBuyRegisterService bulkBuyRegisterService;

	/**
     * 新增
     */
	@RequestMapping(value="/insert", method={RequestMethod.POST})
   	public BaseResult insert(@RequestBody Map<String, Object> param) throws Exception {
		BulkBuyRegister bulkBuyRegister = JsonObjUtils.map2obj(param, BulkBuyRegister.class);
		boolean success = bulkBuyRegisterService.insert(bulkBuyRegister); 
        if (success) {
        	return new BaseResult("1", "成功", bulkBuyRegister);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }

	/**
     * 新增
     */
	@RequestMapping(value="/insertReturnId", method={RequestMethod.POST})
   	public BaseResult insertReturnId(@RequestBody Map<String, Object> param) throws Exception {
		BulkBuyRegister bulkBuyRegister = JsonObjUtils.map2obj(param, BulkBuyRegister.class);
		int ret = bulkBuyRegisterService.insertReturnId(bulkBuyRegister); 
        if (ret > 0) {
        	return new BaseResult("1", "成功", bulkBuyRegister);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }

	/**
     * 删除
     */
	@RequestMapping(value="/delete/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult delete(@PathVariable("id") String id) {
		boolean success = bulkBuyRegisterService.deleteById(Long.valueOf(id)); 
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
   	public BaseResult deleteBatch(@RequestBody Map<String, Object> param) throws Exception {
   		List<String> idList = JsonObjUtils.map2List(param, "ids", String.class);
		/* 物理删除
		boolean success = bulkBuyRegisterService.deleteBatchIds(idList); 
        if (success) {
        	return new BaseResult("1", "成功", null);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
      	*/
      	for (String id : idList) {
   			BulkBuyRegister bulkBuyRegister = bulkBuyRegisterService.selectById(id);
   			bulkBuyRegister.setValidTag(0);
			boolean success = bulkBuyRegisterService.updateById(bulkBuyRegister);
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
   	public BaseResult update(@RequestBody Map<String, Object> param) throws Exception {
		BulkBuyRegister bulkBuyRegister = JsonObjUtils.map2obj(param, BulkBuyRegister.class);
		boolean success = bulkBuyRegisterService.updateById(bulkBuyRegister); 
        if (success) {
        	return new BaseResult("1", "成功", bulkBuyRegister);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
    
    /**
     * 查询
     */
	@RequestMapping(value="/select/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult selectById(@PathVariable("id") String id) {
		BulkBuyRegister bulkBuyRegister = bulkBuyRegisterService.selectById(Long.valueOf(id)); 
        return new BaseResult("1", "成功", bulkBuyRegister);
    }
    
    /**
     * 分页查询
     */
	@RequestMapping(value="/selectPage", method={RequestMethod.POST})
   	public BaseResult selectPage(@RequestBody Map<String, Object> param) throws Exception {
   		Page<BulkBuyRegister> page = JsonObjUtils.map2obj(param, "page", Page.class);
   		BulkBuyRegister bulkBuyRegister = JsonObjUtils.map2obj(param, "bulkBuyRegister", BulkBuyRegister.class);
   		if (page == null) {
			page = new Page<BulkBuyRegister>(1, 10);
		}  
		EntityWrapper wrapper = new EntityWrapper<>(bulkBuyRegister);
		wrapper.orderBy("order_no", false);
   		wrapper.where("valid_tag = 1", "");
		Page<BulkBuyRegister> pageBulkBuyRegister = bulkBuyRegisterService.selectPage(page, wrapper); 
        return new BaseResult("1", "成功", pageBulkBuyRegister);
    }
    
    /**
     * 自定义分页查询
     */
	@RequestMapping(value="/selectList", method={RequestMethod.POST})
   	public BaseResult selectList(@RequestBody Map<String, Object> param) throws Exception {
   		Page<BulkBuyRegister> page = JsonObjUtils.map2obj(param, "page", Page.class);
   		Map<String, Object> map = JsonObjUtils.map2obj(param, "bulkBuyRegister", Map.class);
   		if (page == null) {
			page = new Page<BulkBuyRegister>(1, 10);
		}  
		List<String> timeRangeList = (List<String>) param.get("createTimeRange");
		if (null != timeRangeList && timeRangeList.size() > 0) {
			map.put("startTime", timeRangeList.get(0));
			map.put("endTime", timeRangeList.get(1));
		}
		map.put("orderSql", "create_time desc");
		return bulkBuyRegisterService.selectList(page, map); 
    }
    
}
