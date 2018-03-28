package com.xgzx.admin.mapper;

import com.xgzx.admin.entity.Orders;
import com.xgzx.admin.entity.User;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * @author DaiDong
 * @since 2018-01-10
 * @email 755144556@qq.com
 */
public interface OrdersMapper extends BaseMapper<Orders> {

	Orders customQuery(int id);
	
	List<Orders> selectOrdersList(Pagination page, 
			Map<String, Object> ordersMap);
	
	/**
	 * 商品类型为”会员“的订单详情
	 */
	Map<String, Object> getMemberOrdersInfo(String orderId);
	
	/**
	 * 商品类型为”课程“的订单详情
	 */
	Map<String, Object> getLessonOrdersInfo(String orderId);
	
	/**
	 * 到期的订单
	 */
	List<Orders> getExpiryOrders(Map<String, Object> param);
	
	
	
	
}