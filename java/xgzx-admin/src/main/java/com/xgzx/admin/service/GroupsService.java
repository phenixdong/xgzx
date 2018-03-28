package com.xgzx.admin.service;

import com.xgzx.admin.entity.Groups;
import com.xgzx.admin.entity.RebateWithdraw;
import com.xgzx.admin.entity.User;

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
public interface GroupsService extends IService<Groups> {
	
	BaseResult customQuery(int id);
	
	List<User> getGroupsMateList(Map<String, Object> param);
	
	BaseResult addGroups(Map<String, Object> param);
	
	BaseResult selectGroupsList(Page<Groups> page, Map<String, Object> groupsMap);

	BaseResult selectBulkBuyGroupsList(Page<Groups> page, Map<String, Object> groupsMap);

	List<Groups> getUserGroups(Map<String, Object> groupsMap);
	
	BaseResult sendMessage(Map<String, Object> param);
	
	BaseResult getSendMessageList(Map<String, Object> param) throws Exception;
	
}
