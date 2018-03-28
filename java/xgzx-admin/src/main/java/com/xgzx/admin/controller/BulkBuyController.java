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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.xgzx.base.BaseResult;
import com.xgzx.base.BaseErrResult;
import com.xgzx.util.JsonObjUtils;
import com.xgzx.util.UuidKeyUtil;
import com.xgzx.exception.CommonError;
import com.xgzx.admin.service.BulkBuyService;
import com.xgzx.admin.service.UserMemberService;
import com.xgzx.admin.entity.BulkBuy;
import com.xgzx.admin.entity.UserMember;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author DaiDong
 * @since 2018-03-04
 * @email 755144556@qq.com
 */
@RestController
@RequestMapping("/bulkBuy")
public class BulkBuyController {
	
	@Autowired
	BulkBuyService bulkBuyService;

	/**
     * 新增
     */
	@RequestMapping(value="/insert", method={RequestMethod.POST})
   	public BaseResult insert(@RequestBody Map<String, Object> param) throws Exception {
		BulkBuy bulkBuy = JsonObjUtils.map2obj(param, BulkBuy.class);
		bulkBuy.setBulkBuyId(UuidKeyUtil.get32UUID());
		bulkBuy.setPassword(UuidKeyUtil.get4UUID());
		bulkBuy.setRegisterNumber(0);
		bulkBuy.setValidTag(1);
		boolean success = bulkBuyService.insert(bulkBuy); 
        if (success) {
        	return new BaseResult("1", "成功", bulkBuy);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
	
	/**
     * 注册
     */
	@RequestMapping(value="/register", method={RequestMethod.POST})
   	public BaseResult register(@RequestBody Map<String, Object> param) throws Exception {
		return bulkBuyService.register(param);
    }

	/**
     * 新增
     */
	@RequestMapping(value="/insertReturnId", method={RequestMethod.POST})
   	public BaseResult insertReturnId(@RequestBody Map<String, Object> param) throws Exception {
		BulkBuy bulkBuy = JsonObjUtils.map2obj(param, BulkBuy.class);
		int ret = bulkBuyService.insertReturnId(bulkBuy); 
        if (ret > 0) {
        	return new BaseResult("1", "成功", bulkBuy);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }

	/**
     * 删除
     */
	@RequestMapping(value="/delete/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult delete(@PathVariable("id") String id) {
		boolean success = bulkBuyService.deleteById(Long.valueOf(id)); 
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
		boolean success = bulkBuyService.deleteBatchIds(idList); 
        if (success) {
        	return new BaseResult("1", "成功", null);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
      	*/
      	for (String id : idList) {
   			BulkBuy bulkBuy = bulkBuyService.selectById(id);
   			bulkBuy.setValidTag(0);
			boolean success = bulkBuyService.updateById(bulkBuy);
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
		BulkBuy bulkBuy = JsonObjUtils.map2obj(param, BulkBuy.class);
		boolean success = bulkBuyService.updateById(bulkBuy); 
        if (success) {
        	return new BaseResult("1", "成功", bulkBuy);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
    
    /**
     * 查询
     */
	@RequestMapping(value="/select/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult selectById(@PathVariable("id") String id) {
		BulkBuy bulkBuy = bulkBuyService.selectById(id); 
        return new BaseResult("1", "成功", bulkBuy);
    }
    
    /**
     * 分页查询
     */
	@RequestMapping(value="/selectPage", method={RequestMethod.POST})
   	public BaseResult selectPage(@RequestBody Map<String, Object> param) throws Exception {
   		Page<BulkBuy> page = JsonObjUtils.map2obj(param, "page", Page.class);
   		BulkBuy bulkBuy = JsonObjUtils.map2obj(param, "bulkBuy", BulkBuy.class);
   		if (page == null) {
			page = new Page<BulkBuy>(1, 10);
		}  
		EntityWrapper wrapper = new EntityWrapper<>(bulkBuy);
		wrapper.orderBy("order_no", false);
   		wrapper.where("valid_tag = 1", "");
		Page<BulkBuy> pageBulkBuy = bulkBuyService.selectPage(page, wrapper); 
        return new BaseResult("1", "成功", pageBulkBuy);
    }
    
    /**
     * 自定义分页查询
     */
	@RequestMapping(value="/selectList", method={RequestMethod.POST})
   	public BaseResult selectList(@RequestBody Map<String, Object> param) throws Exception {
   		Page<BulkBuy> page = JsonObjUtils.map2obj(param, "page", Page.class);
   		Map<String, Object> map = JsonObjUtils.map2obj(param, "bulkBuy", Map.class);
   		if (page == null) {
			page = new Page<BulkBuy>(1, 10);
		}  
		List<String> timeRangeList = (List<String>) param.get("createTimeRange");
		if (null != timeRangeList && timeRangeList.size() > 0) {
			map.put("startTime", timeRangeList.get(0));
			map.put("endTime", timeRangeList.get(1));
		}
		map.put("orderSql", "create_time desc");
		return bulkBuyService.selectList(page, map); 
    }
    
}
