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
import com.xgzx.admin.mapper.GroupsUserMapper;
import com.xgzx.admin.service.GroupsUserService;
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
public class GroupsUserServiceImpl extends ServiceImpl<GroupsUserMapper, GroupsUser> implements GroupsUserService {
	
	@Autowired
	GroupsUserMapper groupsUserMapper;
	
	@Override
	public BaseResult customQuery(int id) {
		GroupsUser groupsUser = groupsUserMapper.customQuery(id);
		return new BaseResult("1", "成功", groupsUser);
	}
	
	@Override
	public BaseResult selectGroupsUserList(Page<GroupsUser> page, 
			Map<String, Object> groupsUserMap) {
		List<GroupsUser> list = groupsUserMapper.selectGroupsUserList(page, groupsUserMap); 
		page.setRecords(list);
		return new BaseResult(page);
	}

	@Override
	public BaseResult addGroupsUser(Map<String, Object> param) throws Exception {
		Integer groupId = (Integer) param.get("groupId");
		List<String> idList = JsonObjUtils.map2List(param, "ids", String.class);
		for (String id : idList) {
			Map<String, Object> map = new HashMap<>();
			map.put("group_id", groupId);
			map.put("user_id", id);
			List<GroupsUser> list = groupsUserMapper.selectByMap(map);
			if (null != list && list.size() > 0) {
				CommonError.CommonErr(new BaseErrResult("-1", "用户已加入本群"));
			}
			groupsUserMapper.addGroupsUser(map);
		}
		
		return new BaseResult();
	}
	
	@Override
	public BaseResult delGroupsUser(Map<String, Object> param) throws Exception {
		Integer groupId = (Integer) param.get("groupId");
		String userId = (String) param.get("userId");
		List<String> idList = JsonObjUtils.map2List(param, "ids", String.class);
		for (String id : idList) {
			Map<String, Object> map = new HashMap<>();
			map.put("group_id", groupId);
			map.put("user_id", id);
			if (userId.equals(id)) {
				continue;// 不删除用户自己
			}
			groupsUserMapper.deleteByMap(map);
		}
		return new BaseResult();
	}

}
