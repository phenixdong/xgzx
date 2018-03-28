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
import com.xgzx.admin.service.LessonService;
import com.xgzx.admin.service.MemberTypeService;
import com.xgzx.admin.service.OrdersService;
import com.xgzx.admin.entity.Lesson;
import com.xgzx.admin.entity.MemberType;
import com.xgzx.admin.entity.Orders;
import com.xgzx.admin.entity.User;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author DaiDong
 * @since 2018-01-10
 * @email 755144556@qq.com
 */
@RestController
@RequestMapping("/orders")
public class OrdersController {
	
	@Autowired
	OrdersService ordersService;
	@Autowired
	MemberTypeService memberTypeService;
	@Autowired
	LessonService lessonService;

	/**
     * 新增
     */
	@RequestMapping(value="/insert", method={RequestMethod.POST})
   	public BaseResult insert(@RequestBody Map<String, Object> param) {
		Orders orders = null;
		try {
			orders = JsonObjUtils.map2obj(param, Orders.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		boolean success = ordersService.insert(orders); 
        if (success) {
        	return new BaseResult("1", "成功", orders);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }

	/**
     * 删除
     */
	@RequestMapping(value="/delete/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult delete(@PathVariable("id") String id) {
		boolean success = ordersService.deleteById(Long.valueOf(id)); 
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
		
		boolean success = ordersService.deleteBatchIds(idList); 
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
		Orders orders = null;
		try {
			orders = JsonObjUtils.map2obj(param, Orders.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		boolean success = ordersService.updateById(orders); 
        if (success) {
        	return new BaseResult("1", "成功", orders);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
    
    /**
     * 查询
     */
	@RequestMapping(value="/select/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult selectById(@PathVariable("id") String id) {
		Orders orders = ordersService.selectById(Long.valueOf(id)); 
        return new BaseResult("1", "成功", orders);
    }
    
    /**
     * 分页查询
     */
	@RequestMapping(value="/selectPage", method={RequestMethod.POST})
   	public BaseResult selectPage(@RequestBody Map<String, Object> param) throws Exception {
		Page<Orders> page = JsonObjUtils.map2obj(param, "page", Page.class);
		Map<String, Object> ordersMap = JsonObjUtils.map2obj(param, "orders", Map.class);
		if (page == null) {
			page = new Page<Orders>(1, 10);
		}  
		List<String> timeRangeList = (List<String>) param.get("createTimeRange");
		if (null != timeRangeList && timeRangeList.size() > 0) {
			ordersMap.put("startTime", timeRangeList.get(0));
			ordersMap.put("endTime", timeRangeList.get(1));
		}
		Page<Orders> resultPage = ordersService.selectOrdersList(page, ordersMap);
		List<Orders> ordersList = resultPage.getRecords();
		if (null != ordersList && ordersList.size() > 0) {
			for (Orders item : ordersList) {
				// 商品类型，1会员，2商品
				if (1 == item.getProductType()) {
					// 购买的会员详情
					MemberType memberType = memberTypeService.selectById(
							item.getProductId());
					item.setMemberType(memberType);
				} else {
					// 购买的课程详情
					Lesson lesson = lessonService.selectById(item.getProductId());
					item.setLesson(lesson);
				}
			}
		}
		return new BaseResult(resultPage);
    }
    
    /**
     * 自定义查询
     */
    @RequestMapping(value="/customQuery/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult customQuery(@PathVariable("id") int id) {
		BaseResult baseResult = ordersService.customQuery(id); 
        return baseResult;
    }
	
}
