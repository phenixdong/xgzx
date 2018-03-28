package com.xgzx.admin.mapper;

import com.xgzx.admin.entity.Groups;
import com.xgzx.admin.entity.GroupsMessage;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * @author DaiDong
 * @since 2018-02-03
 * @email 755144556@qq.com
 */
public interface GroupsMessageMapper extends BaseMapper<GroupsMessage> {

	GroupsMessage customQuery(int id);
	
	List<GroupsMessage> getReceivedMessage(Pagination page, 
			Map<String, Object> param);
	
	List<Map<String, Object>> getNewMessageGroups(Map<String, Object> param);
	
	List<GroupsMessage> getGroupsNewMessageList(Map<String, Object> param);
	
	List<GroupsMessage> getSendMessageList(Pagination page, Map<String, Object> param);

}