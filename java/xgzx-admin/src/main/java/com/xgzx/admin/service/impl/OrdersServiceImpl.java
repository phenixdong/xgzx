package com.xgzx.admin.service.impl;

import com.xgzx.admin.entity.Orders;
import com.xgzx.admin.entity.User;
import com.xgzx.admin.mapper.OrdersMapper;
import com.xgzx.admin.service.OrdersService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.xgzx.base.BaseResult;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DaiDong
 * @since 2018-01-10
 * @email 755144556@qq.com
 */
@Service
@Transactional
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
	
	@Autowired
	OrdersMapper ordersMapper;
	
	@Override
	public BaseResult customQuery(int id) {
		Orders orders = ordersMapper.customQuery(id);
		return new BaseResult("1", "成功", orders);
	}
	
	@Override
	public Page<Orders> selectOrdersList(Page<Orders> page, 
			Map<String, Object> ordersMap) {
		List<Orders> list = ordersMapper.selectOrdersList(page, ordersMap); 
		page.setRecords(list);
		return page;
	}

	@Override
	public Map<String, Object> getOrdersInfo(String orderId) {
		Map<String, Object> map = null;
		Orders orders = ordersMapper.selectById(orderId);
		// 1会员，2课程
		if (1 == orders.getProductType()) {
			// 会员，查询会员类型
			map = ordersMapper.getMemberOrdersInfo(orderId);
		} else {
			// 课程，查询课程信息
			map = ordersMapper.getLessonOrdersInfo(orderId);
		}
		return map;		
	}
	
	
}
