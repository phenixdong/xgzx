package com.xgzx.admin.service.impl;

import com.xgzx.admin.entity.AdminUser;
import com.xgzx.admin.mapper.AdminUserMapper;
import com.xgzx.admin.service.AdminUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.xgzx.base.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DaiDong
 * @since 2018-01-19
 * @email 755144556@qq.com
 */
@Service
@Transactional
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {
	
	@Autowired
	AdminUserMapper adminUserMapper;
	
	@Override
	public BaseResult customQuery(int id) {
		AdminUser adminUser = adminUserMapper.customQuery(id);
		return new BaseResult("1", "成功", adminUser);
	}
}
