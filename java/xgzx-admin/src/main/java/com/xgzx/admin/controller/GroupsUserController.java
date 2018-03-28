package com.xgzx.admin.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xgzx.admin.entity.Groups;
import com.xgzx.admin.entity.GroupsMessage;
import com.xgzx.admin.entity.GroupsUser;
import com.xgzx.admin.mapper.GroupsMapper;
import com.xgzx.admin.mapper.GroupsMessageMapper;
import com.xgzx.admin.mapper.GroupsUserMapper;
import com.xgzx.admin.service.GroupsMessageService;
import com.xgzx.admin.service.GroupsUserService;
import com.xgzx.base.BaseResult;
import com.xgzx.util.JsonObjUtils;

/**
 * @author DaiDong
 * @since 2018-01-09
 * @email 755144556@qq.com
 */
@RestController
@RequestMapping("/groupsUser")
public class GroupsUserController {
	
	@Autowired
	GroupsUserService groupsUserService;
	@Autowired
	GroupsUserMapper groupsUserMapper;
	@Autowired
	GroupsMapper groupsMapper;
	@Autowired
	GroupsMessageService groupsMessageService;

	/**
     * 新增
     */
	@RequestMapping(value="/insert", method={RequestMethod.POST})
   	public BaseResult insert(@RequestBody Map<String, Object> param) {
		GroupsUser groupsUser = null;
		try {
			groupsUser = JsonObjUtils.map2obj(param, GroupsUser.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		groupsUser.setMessageReadTime(new Date());
		boolean success = groupsUserService.insert(groupsUser); 
        if (success) {
        	return new BaseResult("1", "成功", groupsUser);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }

	/**
     * 删除
     */
	@RequestMapping(value="/delete/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult delete(@PathVariable("id") String id) {
		boolean success = groupsUserService.deleteById(Long.valueOf(id)); 
        if (success) {
        	return new BaseResult("1", "成功", null);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
    
    /**
     * 批量删除
     */
	@RequestMapping(value="/deleteBatch", method={RequestMethod.POST})
   	public BaseResult deleteBatch(@RequestBody Map<String, Object> param) {
   		List<String> idList;
   		try {
			idList = JsonObjUtils.map2List(param, "ids", String.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		
		boolean success = groupsUserService.deleteBatchIds(idList); 
        if (success) {
        	return new BaseResult("1", "成功", null);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }

	/**
     * 修改
     */
	@RequestMapping(value="/update", method={RequestMethod.POST})
   	public BaseResult update(@RequestBody Map<String, Object> param) throws Exception {
		GroupsUser groupsUser = JsonObjUtils.map2obj(param, GroupsUser.class);
		groupsUser.setMessageReadTime(new Date());
		boolean success = groupsUserService.updateById(groupsUser); 
        if (success) {
        	return new BaseResult("1", "成功", groupsUser);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
    
	/**
     * 修改
     */
	@RequestMapping(value="/updateReadTime", method={RequestMethod.POST})
   	public BaseResult updateReadTime(@RequestBody Map<String, Object> param) throws Exception {
		GroupsUser groupsUser = JsonObjUtils.map2obj(param, GroupsUser.class);
		groupsUser = groupsUserMapper.selectOne(groupsUser);
		if (null != groupsUser) {
			groupsUser.setMessageReadTime(new Date());
			
			int ret = groupsUserMapper.updateById(groupsUser);
	        if (ret > 0) {
	        	return new BaseResult("1", "成功", groupsUser);
	      	} else {
	      		return new BaseResult("0", "失败", null);
	      	}
		}
		return new BaseResult("1", "成功", null);
    }
	
    /**
     * 查询
     */
	@RequestMapping(value="/select/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult selectById(@PathVariable("id") String id) {
		GroupsUser groupsUser = groupsUserService.selectById(Long.valueOf(id)); 
        return new BaseResult("1", "成功", groupsUser);
    }
    
	/**
     * userId管理群组的成员
     */
//	@RequestMapping(value="/getUserListByUserId", method={RequestMethod.POST})
//   	public BaseResult getUserListByUserId(@RequestBody Map<String, Object> param) throws Exception {
//		// 查询群组
//		String userId = (String) param.get("userId");
//		Groups groups = new Groups();
//		groups.setMasterUserId(userId);
//		groups = groupsMapper.selectOne(groups);
//		// 查询群组成员
//		Page<GroupsUser> page = JsonObjUtils.map2obj(param, "page", Page.class);
//		Map<String, Object> map = new HashMap<>();
//		map.put("groupId", groups.getGroupId());
//		BaseResult result = groupsUserService.selectGroupsUserList(page, map);
//		if ("1".equals(result.returnCode)) {
//			Page retPage = (Page) result.getData();
//			groups.setPage(retPage);
//		}
//		
//        return new BaseResult("1", "成功", groups);
//    }
	
	/**
     * 群组成员，返回值为Groups
     */
//	@RequestMapping(value="/getUserListByGroupId", method={RequestMethod.POST})
//   	public BaseResult getUserListByGroupId(@RequestBody Map<String, Object> param) throws Exception {
//		Integer groupId = (Integer) param.get("groupId");
//		Page<GroupsUser> page = JsonObjUtils.map2obj(param, "page", Page.class);
//		
//		Groups groups = new Groups();
//		groups.setGroupId(groupId);
//		
//		Map<String, Object> map = new HashMap<>();
//		map.put("groupId", groupId);
//		BaseResult result = groupsUserService.selectGroupsUserList(page, map);
//		if ("1".equals(result.returnCode)) {
//			Page retPage = (Page) result.getData();
//			groups.setPage(retPage);
//		}
//		
//        return new BaseResult("1", "成功", groups);
//    }
	
    /**
     * 分页查询
     */
	@RequestMapping(value="/selectPage", method={RequestMethod.POST})
   	public BaseResult selectPage(@RequestBody Map<String, Object> param) throws Exception {
		Page<GroupsUser> page = JsonObjUtils.map2obj(param, "page", Page.class);
		Map<String, Object> groupsUserMap = JsonObjUtils.map2obj(param, 
				"groupsUser", Map.class);
		if (page == null) {
			page = new Page<GroupsUser>(1, 10);
		}  
		List<String> timeRangeList = (List<String>) param.get("createTimeRange");
		if (null != timeRangeList && timeRangeList.size() > 0) {
			groupsUserMap.put("startTime", timeRangeList.get(0));
			groupsUserMap.put("endTime", timeRangeList.get(1));
		}
		return groupsUserService.selectGroupsUserList(page, groupsUserMap);
    }
    
    /**
     * 自定义查询
     */
    @RequestMapping(value="/customQuery/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult customQuery(@PathVariable("id") int id) {
		BaseResult baseResult = groupsUserService.customQuery(id); 
        return baseResult;
    }
	
}
