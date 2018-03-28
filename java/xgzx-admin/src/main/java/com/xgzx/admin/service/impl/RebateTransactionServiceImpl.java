package com.xgzx.admin.service.impl;

import com.xgzx.admin.entity.RebateTransaction;
import com.xgzx.admin.mapper.RebateTransactionMapper;
import com.xgzx.admin.service.OrdersService;
import com.xgzx.admin.service.RebateTransactionService;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.xgzx.base.BaseResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DaiDong
 * @since 2018-01-09
 * @email 755144556@qq.com
 */
@Service
@Transactional
public class RebateTransactionServiceImpl extends ServiceImpl<RebateTransactionMapper, RebateTransaction> implements RebateTransactionService {
	
	@Autowired
	RebateTransactionMapper rebateTransactionMapper;
	@Autowired
	OrdersService ordersService;
	
	@Override
	public BaseResult customQuery(int id) {
		RebateTransaction rebateTransaction = rebateTransactionMapper.customQuery(id);
		return new BaseResult("1", "成功", rebateTransaction);
	}

	@Override
	public BaseResult selectRebateTransactionList(Page<RebateTransaction> page, 
			Map<String, Object> transactionMap) {
		// 查询返点记录
		List<RebateTransaction> list = rebateTransactionMapper
				.selectRebateTransactionList(page, transactionMap);
		
		// 查询返点来源订单
		for (RebateTransaction item : list) {
			Map<String, Object> map = ordersService.getOrdersInfo(item.getOrderId());
			item.setOrdersUserName((String) map.get("ordersUserName"));
			item.setOrdersTotalFee((BigDecimal) map.get("ordersTotalFee"));
			item.setOrdersProductName((String) map.get("ordersProductName"));
		}
		page.setRecords(list);
		return new BaseResult(page);
	}
}
