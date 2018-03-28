package com.xgzx.admin.service;

import com.xgzx.admin.entity.RebateTransaction;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2018-01-09
 * @email 755144556@qq.com
 */
public interface RebateTransactionService extends IService<RebateTransaction> {
	
	BaseResult customQuery(int id);
	
	BaseResult selectRebateTransactionList(Page<RebateTransaction> page, Map<String, Object> transactionMap);
}
