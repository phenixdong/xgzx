package com.xgzx.admin.service;

import com.xgzx.admin.entity.GroupsMessage;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2018-02-03
 * @email 755144556@qq.com
 */
public interface GroupsMessageService extends IService<GroupsMessage> {
	
	BaseResult customQuery(int id);
	
	BaseResult getReceivedMessage(Page<GroupsMessage> page, Map<String, Object> param);

	BaseResult getNewMessageGroups(Map<String, Object> param);
	
	List<GroupsMessage> getGroupsNewMessageList(Map<String, Object> param);
	
}
