package com.xgzx.admin.mapper;

import com.xgzx.admin.entity.AdminUser;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * @author DaiDong
 * @since 2018-01-19
 * @email 755144556@qq.com
 */
public interface AdminUserMapper extends BaseMapper<AdminUser> {

	AdminUser customQuery(int id);
	
}