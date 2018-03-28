package com.xgzx.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xgzx.admin.entity.GroupsMessage;
import com.xgzx.admin.entity.GroupsUser;
import com.xgzx.admin.entity.User;
import com.xgzx.admin.mapper.GroupsMessageMapper;
import com.xgzx.admin.mapper.GroupsUserMapper;
import com.xgzx.admin.mapper.UserMapper;
import com.xgzx.admin.service.GroupsMessageService;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2018-02-03
 * @email 755144556@qq.com
 */
@Service
@Transactional
public class GroupsMessageServiceImpl extends ServiceImpl<GroupsMessageMapper, GroupsMessage> implements GroupsMessageService {
	
	@Autowired
	GroupsMessageMapper groupsMessageMapper;
	@Autowired
	GroupsUserMapper groupsUserMapper;
	@Autowired
	UserMapper userMapper;
	
	@Override
	public BaseResult customQuery(int id) {
		GroupsMessage groupsMessage = groupsMessageMapper.customQuery(id);
		return new BaseResult("1", "成功", groupsMessage);
	}

	@Override
	public BaseResult getReceivedMessage(Page<GroupsMessage> page, Map<String, Object> param) {
		// 消息列表
		List<GroupsMessage> list = groupsMessageMapper.getReceivedMessage(page, param);
		page.setRecords(list);
		
		// 上次查看消息时间
		GroupsUser groupsUser = new GroupsUser();
		groupsUser.setUserId((String) param.get("userId"));
		groupsUser.setGroupId((Integer) param.get("groupId"));
		groupsUser = groupsUserMapper.selectOne(groupsUser);
		
		Map<String, Object> data = new HashMap<>();
		data.put("page", page);
		data.put("groupsUser", groupsUser);
		return new BaseResult(data);
	}

	@Override
	public BaseResult getNewMessageGroups(Map<String, Object> param) {
		List<Map<String, Object>> list = groupsMessageMapper.getNewMessageGroups(param);
		for (Map<String, Object> item : list) {
			Map<String, Object> map = new HashMap<>();
			map.put("groupId", (Integer) item.get("groupId"));
			map.put("userId", (String) param.get("userId"));
			List<GroupsMessage> msgList = groupsMessageMapper.getGroupsNewMessageList(map);
			// 最新的一条消息
			String lastMsg = msgList.get(msgList.size() - 1).getMessage();
			item.put("lastMsg", lastMsg);
		}
		return new BaseResult(list);
	}

	@Override
	public List<GroupsMessage> getGroupsNewMessageList(Map<String, Object> param) {
		List<GroupsMessage> list = groupsMessageMapper.getGroupsNewMessageList(param);
		for (GroupsMessage item : list) {
			User user = userMapper.selectById(item.getUserId());
			item.setUser(user);
		}
		return list;
	}
	
}
