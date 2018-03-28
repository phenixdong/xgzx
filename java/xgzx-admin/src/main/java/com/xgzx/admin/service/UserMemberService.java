package com.xgzx.admin.service;

import com.xgzx.admin.entity.UserMember;
import com.baomidou.mybatisplus.service.IService;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2017-12-24
 * @email 755144556@qq.com
 */
public interface UserMemberService extends IService<UserMember> {
	
	BaseResult customQuery(int id);
	
	int insertOrUpdateByUserId(UserMember userMember);
	
}
