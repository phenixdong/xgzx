package com.xgzx.admin.service.impl;

import com.xgzx.admin.entity.Transaction;
import com.xgzx.admin.entity.User;
import com.xgzx.admin.mapper.TransactionMapper;
import com.xgzx.admin.service.TransactionService;
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
public class TransactionServiceImpl extends ServiceImpl<TransactionMapper, Transaction> implements TransactionService {
	
	@Autowired
	TransactionMapper transactionMapper;
	
	@Override
	public BaseResult customQuery(int id) {
		Transaction transaction = transactionMapper.customQuery(id);
		return new BaseResult("1", "成功", transaction);
	}
	
	@Override
	public BaseResult selectTransactionList(Page<Transaction> page, 
			Map<String, Object> transactionMap) {
		List<Transaction> list = transactionMapper
				.selectTransactionList(page, transactionMap); 
		page.setRecords(list);
		return new BaseResult(page);
	}
	
}
