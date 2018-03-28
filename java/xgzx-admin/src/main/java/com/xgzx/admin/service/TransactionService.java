package com.xgzx.admin.service;

import com.xgzx.admin.entity.Transaction;
import com.xgzx.admin.entity.User;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2018-01-10
 * @email 755144556@qq.com
 */
public interface TransactionService extends IService<Transaction> {
	
	BaseResult customQuery(int id);
	
	BaseResult selectTransactionList(Page<Transaction> page, 
			Map<String, Object> transactionMap);
}
