package com.xgzx.admin.mapper;

import com.xgzx.admin.entity.Account;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * @author DaiDong
 * @since 2018-01-10
 * @email 755144556@qq.com
 */
public interface AccountMapper extends BaseMapper<Account> {

	Account customQuery(int id);
	
	Account selectByUserId(String userId);
	
}