package com.xgzx.admin.mapper;

import com.xgzx.admin.entity.Transaction;
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
public interface TransactionMapper extends BaseMapper<Transaction> {

	Transaction customQuery(int id);
	
	List<Transaction> selectTransactionList(Pagination page, 
			Map<String, Object> transactionMap);
	
	
}