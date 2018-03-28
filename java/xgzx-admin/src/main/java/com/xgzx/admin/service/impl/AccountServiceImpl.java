package com.xgzx.admin.service.impl;

import com.xgzx.admin.entity.Account;
import com.xgzx.admin.mapper.AccountMapper;
import com.xgzx.admin.service.AccountService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.xgzx.base.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DaiDong
 * @since 2018-01-10
 * @email 755144556@qq.com
 */
@Service
@Transactional
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
	
	@Autowired
	AccountMapper accountMapper;
	
	@Override
	public BaseResult customQuery(int id) {
		Account account = accountMapper.customQuery(id);
		return new BaseResult("1", "成功", account);
	}

	@Override
	public Account selectByUserId(String userId) {
		return accountMapper.selectByUserId(userId);
	}
}
