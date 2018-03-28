package com.xgzx.admin.service;

import com.xgzx.admin.entity.Account;
import com.baomidou.mybatisplus.service.IService;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2018-01-10
 * @email 755144556@qq.com
 */
public interface AccountService extends IService<Account> {
	
	BaseResult customQuery(int id);
	
	Account selectByUserId(String userId);
}
