package com.xgzx.admin.mapper;

import com.xgzx.admin.entity.Groups;
import com.xgzx.admin.entity.GroupsUser;
import com.xgzx.admin.entity.User;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * @author DaiDong
 * @since 2018-01-09
 * @email 755144556@qq.com
 */
public interface GroupsUserMapper extends BaseMapper<GroupsUser> {

	GroupsUser customQuery(int id);
	
	List<User> getGroupsMateList(Map<String, Object> map);
	
	int addGroupsUser(Map<String, Object> map); 
	
	List<GroupsUser> selectGroupsUserList(Pagination page, 
			Map<String, Object> groupsUserMap);
	
	List<Groups> getBelongedGroups(Map<String, Object> map);
	
}