package com.xgzx.admin.service;

import com.xgzx.admin.entity.Orders;
import com.xgzx.admin.entity.User;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2018-01-10
 * @email 755144556@qq.com
 */
public interface OrdersService extends IService<Orders> {
	
	BaseResult customQuery(int id);
	
	Page<Orders> selectOrdersList(Page<Orders> page, Map<String, Object> ordersMap);
	
	Map<String, Object> getOrdersInfo(String orderId);
}
