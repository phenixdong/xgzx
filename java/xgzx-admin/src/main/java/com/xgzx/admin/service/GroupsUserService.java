package com.xgzx.admin.service;

import com.xgzx.admin.entity.GroupsMessage;
import com.xgzx.admin.entity.GroupsUser;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2018-01-09
 * @email 755144556@qq.com
 */
public interface GroupsUserService extends IService<GroupsUser> {
	
	BaseResult customQuery(int id);
	
	BaseResult addGroupsUser(Map<String, Object> map) throws Exception;
	
	BaseResult delGroupsUser(Map<String, Object> map) throws Exception;
	
	BaseResult selectGroupsUserList(Page<GroupsUser> page, 
			Map<String, Object> groupsUserMap);
	
}
