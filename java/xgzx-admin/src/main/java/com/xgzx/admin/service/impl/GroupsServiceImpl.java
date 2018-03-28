package com.xgzx.admin.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xgzx.admin.entity.Groups;
import com.xgzx.admin.entity.GroupsMessage;
import com.xgzx.admin.entity.GroupsUser;
import com.xgzx.admin.entity.User;
import com.xgzx.admin.mapper.GroupsMapper;
import com.xgzx.admin.mapper.GroupsMessageMapper;
import com.xgzx.admin.mapper.GroupsUserMapper;
import com.xgzx.admin.service.GroupsMessageService;
import com.xgzx.admin.service.GroupsService;
import com.xgzx.base.BaseErrResult;
import com.xgzx.base.BaseResult;
import com.xgzx.exception.CommonError;
import com.xgzx.util.JsonObjUtils;

/**
 * @author DaiDong
 * @since 2018-01-09
 * @email 755144556@qq.com
 */
@Service
@Transactional
public class GroupsServiceImpl extends ServiceImpl<GroupsMapper, Groups> implements GroupsService {
	
	@Autowired
	GroupsMapper groupsMapper;
	@Autowired
	GroupsUserMapper groupsUserMapper;
	@Autowired
	GroupsMessageMapper groupsMessageMapper;
	@Autowired
	GroupsMessageService groupsMessageService;
	
	@Override
	public BaseResult customQuery(int id) {
		Groups groups = groupsMapper.customQuery(id);
		return new BaseResult("1", "成功", groups);
	}
	
	@Override
	public BaseResult selectGroupsList(Page<Groups> page, Map<String, Object> groupsMap) {
		List<Groups> list = groupsMapper.selectGroupsList(page, groupsMap); 
		page.setRecords(list);
		return new BaseResult(page);
	}
	
	@Override
	public BaseResult selectBulkBuyGroupsList(Page<Groups> page, Map<String, Object> groupsMap) {
		List<Groups> list = groupsMapper.selectBulkBuyGroupsList(page, groupsMap); 
		page.setRecords(list);
		return new BaseResult(page);
	}
	
	@Override
	public List<Groups> getUserGroups(Map<String, Object> groupsMap) {
		List<Groups> list = groupsMapper.getBelongedGroups(groupsMap);
		return list;
	}

	/*  
	 * 群主发消息
	 */
	@Override
	public BaseResult sendMessage(Map<String, Object> param) {
		String userId = (String) param.get("userId");
		String message = (String) param.get("message");
		
		Groups groups = new Groups();
		groups.setMasterUserId(userId);
		groups = groupsMapper.selectOne(groups);
		Integer groupId = groups.getGroupId();
		
		GroupsMessage groupsMessage = new GroupsMessage();
		groupsMessage.setGroupId(groupId);
		groupsMessage.setMessage(message);
		groupsMessage.setValidTag(1);
		int ret = groupsMessageMapper.insert(groupsMessage);
		if (ret <= 0) {
			CommonError.CommonErr(new BaseErrResult("-1", "新增失败"));
		}
		
		return new BaseResult();
	}

	@Override
	public BaseResult getSendMessageList(Map<String, Object> param) throws Exception {
		Page<GroupsMessage> page = JsonObjUtils.map2obj(param, "page", Page.class);
		
		GroupsMessage groupsMessage = new GroupsMessage();
		if (null != param.get("userId")) {
			String userId = (String) param.get("userId");
			Groups groups = new Groups();
			groups.setMasterUserId(userId);
			groups = groupsMapper.selectOne(groups);
			Integer groupId = groups.getGroupId();
			param.put("groupId", groupId);
		}
		
		List<GroupsMessage> list = groupsMessageMapper.getSendMessageList(page, param);
		page.setRecords(list);
		return new BaseResult(page);
	}

	@Override
	public BaseResult addGroups(Map<String, Object> param) {
		String userId = (String) param.get("userId");
		String groupName = (String) param.get("groupName");
		Integer type = (Integer) param.get("type");
		Groups groups = new Groups();
		groups.setMasterUserId(userId);
		groups.setGroupName(groupName);
		groups.setType(type);
		groups.setValidTag(1);
		int ret = groupsMapper.insertReturnId(groups);
        if (ret <= 0) {
        	CommonError.CommonErr(new BaseErrResult("-1", "新增群失败"));
      	} 
        
        GroupsUser groupsUser = new GroupsUser();
        groupsUser.setGroupId(groups.getGroupId());
        groupsUser.setUserId(userId);
        groupsUser.setMessageReadTime(new Date());
        groupsUser.setMasterTag(1);
        groupsUser.setValidTag(1);
        ret = groupsUserMapper.insert(groupsUser);
        if (ret <= 0) {
        	CommonError.CommonErr(new BaseErrResult("-1", "新增成员失败"));
      	} 
        
        return new BaseResult("1", "成功", null);
	}

	@Override
	public List<User> getGroupsMateList(Map<String, Object> param) {
		String userId = (String) param.get("userId");
		Integer currentGroupId = (Integer) param.get("currentGroupId");
		
		// 用户所在全部群组
		Map<String, Object> map = new HashMap<>();
		map.put("user_id", userId);
		map.put("valid_tag", 1);
		List<GroupsUser> groupsUserList = groupsUserMapper.selectByMap(map);
		StringBuilder idStr = new StringBuilder("(");
		for (GroupsUser item : groupsUserList) {
			idStr.append(item.getGroupId() + ",");
		}
		String ids = idStr.substring(0, idStr.lastIndexOf(",")) + ")";
		
		// 当前群组已有的成员
		Map<String, Object> currentMap = new HashMap<>();
		currentMap.put("group_id", currentGroupId);
		currentMap.put("valid_tag", 1);
		List<GroupsUser> currentGroupsUserList = groupsUserMapper.selectByMap(currentMap);
		StringBuilder currentUserIdStr = new StringBuilder("(");
		for (GroupsUser item : currentGroupsUserList) {
			currentUserIdStr.append("'" + item.getUserId() + "',");
		}
		String currentUserIds = currentUserIdStr.substring(0, currentUserIdStr.lastIndexOf(",")) + ")";
		
		// 全部ids群组中的成员，过滤掉当前群组中已有的成员
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("userId", userId);
		queryMap.put("ids", ids);
		queryMap.put("currentUserIds", currentUserIds);
		List<User> userList = groupsUserMapper.getGroupsMateList(queryMap);
		
		return userList;
	}
	
	
	
	
}
