package com.xgzx.admin.service;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.xgzx.admin.entity.User;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2017-12-24
 * @email 755144556@qq.com
 */
public interface UserService extends IService<User> {
	
	BaseResult customQuery(int id); 
	
	BaseResult register(String openId, Map<String, Object> userInfoMap);
	
	BaseResult login(Map<String, Object> param);
	
	BaseResult selectUserList(Page<User> page, Map<String, Object> userMap);
	
	boolean isPaid(String userId, Integer lessonId);
	
}
