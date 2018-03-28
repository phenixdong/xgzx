package com.xgzx.admin.service;

import com.xgzx.admin.entity.AdminUser;
import com.baomidou.mybatisplus.service.IService;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2018-01-19
 * @email 755144556@qq.com
 */
public interface AdminUserService extends IService<AdminUser> {
	
	BaseResult customQuery(int id);
}
