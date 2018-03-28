package com.xgzx.admin.mapper;

import com.xgzx.admin.entity.UserMember;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * @author DaiDong
 * @since 2017-12-24
 * @email 755144556@qq.com
 */
public interface UserMemberMapper extends BaseMapper<UserMember> {

	UserMember customQuery(int id);
	
}