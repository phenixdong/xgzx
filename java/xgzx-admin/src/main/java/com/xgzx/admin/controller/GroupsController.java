package com.xgzx.admin.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.xgzx.admin.entity.Groups;
import com.xgzx.admin.entity.GroupsMessage;
import com.xgzx.admin.entity.GroupsUser;
import com.xgzx.admin.entity.User;
import com.xgzx.admin.mapper.GroupsMapper;
import com.xgzx.admin.mapper.GroupsMessageMapper;
import com.xgzx.admin.service.GroupsService;
import com.xgzx.admin.service.GroupsUserService;
import com.xgzx.base.BaseResult;
import com.xgzx.util.JsonObjUtils;

/**
 * @author DaiDong
 * @since 2018-01-09
 * @email 755144556@qq.com
 */
@RestController
@RequestMapping("/groups")
public class GroupsController {
	
	@Autowired
	GroupsService groupsService;
	@Autowired
	GroupsUserService groupsUserService;
	@Autowired
	GroupsMapper groupsMapper;
	@Autowired
	GroupsMessageMapper groupsMessageMapper;
	
	/**
     * 新增
     */
	@RequestMapping(value="/insert", method={RequestMethod.POST})
   	public BaseResult insert(@RequestBody Map<String, Object> param) {
		Groups groups = null;
		try {
			groups = JsonObjUtils.map2obj(param, Groups.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		groups.setValidTag(1);
		boolean success = groupsService.insert(groups); 
        if (success) {
        	return new BaseResult("1", "成功", groups);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }

	/**
     * 删除
     */
	@RequestMapping(value="/delete/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult delete(@PathVariable("id") String id) {
		boolean success = groupsService.deleteById(Long.valueOf(id)); 
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
		
		boolean success = groupsService.deleteBatchIds(idList); 
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
   	public BaseResult update(@RequestBody Map<String, Object> param) {
		Groups groups = null;
		try {
			groups = JsonObjUtils.map2obj(param, Groups.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		boolean success = groupsService.updateById(groups); 
        if (success) {
        	return new BaseResult("1", "成功", groups);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
    
    /**
     * 查询
     */
	@RequestMapping(value="/select/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult selectById(@PathVariable("id") String id) {
		Groups groups = groupsService.selectById(Long.valueOf(id)); 
		Page page = new Page<GroupsUser>(1, 1000);
		Map<String, Object> map = new HashMap<>();
		map.put("groupId", id);
		BaseResult result = groupsUserService.selectGroupsUserList(page, map);
		if ("1".equals(result.returnCode)) {
			Page retPage = (Page) result.getData();
			groups.setPage(retPage);
		}
		
        return new BaseResult("1", "成功", groups);
    }
	
	/**
     * 新增普通群，并加入群
     */
	@RequestMapping(value="/addGroups", method={RequestMethod.POST})
   	public BaseResult addGroups(@RequestBody Map<String, Object> param) throws Exception {
		return groupsService.addGroups(param);
    }
	
	/**
     * 退群
     */
	@RequestMapping(value="/exit", method={RequestMethod.POST})
   	public BaseResult exit(@RequestBody Map<String, Object> param) throws Exception {
		String userId = (String) param.get("userId");
		Integer groupId = (Integer) param.get("groupId");
		Map<String, Object> map = new HashMap<>();
		map.put("user_id", userId);
		map.put("group_id", groupId);
		boolean success = groupsUserService.deleteByMap(map);
		if (success) {
			return new BaseResult("1", "成功", null);
		} else {
			return new BaseResult("0", "失败", null);
		}
    }
	
	/**
     * 解散群组
     */
	@RequestMapping(value="/dismiss", method={RequestMethod.POST})
   	public BaseResult dismiss(@RequestBody Map<String, Object> param) throws Exception {
		String userId = (String) param.get("userId");
		Integer groupId = (Integer) param.get("groupId");
		Groups groups = new Groups();
		groups.setGroupId(groupId);
		groups.setValidTag(0);
		boolean success = groupsService.updateById(groups);
		if (success) {
			return new BaseResult("1", "成功", null);
		} else {
			return new BaseResult("0", "失败", null);
		}
    }
	
	/**
     * userId所属群组
     */
	@RequestMapping(value="/getUserGroups", method={RequestMethod.POST})
   	public BaseResult getUserGroups(@RequestBody Map<String, Object> param) throws Exception {
		String userId = (String) param.get("userId");
				
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		List<Groups> groupsList = groupsService.getUserGroups(map);
		
		for (Groups item : groupsList) {
			Map<String, Object> msgMap = new HashMap<>();
			msgMap.put("userId", userId);
			msgMap.put("groupId", item.getGroupId());
			List<GroupsMessage> msgList = groupsMessageMapper
					.getGroupsNewMessageList(msgMap);
			item.setMsgNumber(msgList.size());
		}
		
		return new BaseResult(groupsList);
    }
	
	/**
     * 邀请入群
     */
	@RequestMapping(value="/addGroupsUser", method={RequestMethod.POST})
   	public BaseResult addGroupsUser(@RequestBody Map<String, Object> param) throws Exception {
		return groupsUserService.addGroupsUser(param);
    }
	
	/**
     * 更新群名称
     */
	@RequestMapping(value="/updateGroupName", method={RequestMethod.POST})
   	public BaseResult updateGroupName(@RequestBody Map<String, Object> param) throws Exception {
		Groups groups =  JsonObjUtils.map2obj(param, "groups", Groups.class);
		groupsService.updateById(groups);
		return new BaseResult();		
    }
	
	/**
     * 删除群成员
     */
	@RequestMapping(value="/delGroupsUser", method={RequestMethod.POST})
   	public BaseResult delGroupsUser(@RequestBody Map<String, Object> param) throws Exception {
		return groupsUserService.delGroupsUser(param);
    }
	
	/**
     * 查询用户所在全部群组的群成员
     */
	@RequestMapping(value="/getGroupsMateList", method={RequestMethod.POST})
   	public BaseResult getGroupsMateList(@RequestBody Map<String, Object> param) throws Exception {
		List<User> userList = groupsService.getGroupsMateList(param);
		return new BaseResult(userList);
    }
    
    /**
     * 分页查询
     */
	@RequestMapping(value="/selectPage", method={RequestMethod.POST})
   	public BaseResult selectPage(@RequestBody Map<String, Object> param) throws Exception {
		Page<Groups> page = JsonObjUtils.map2obj(param, "page", Page.class);
		Map<String, Object> groupsMap = JsonObjUtils.map2obj(param, 
				"groups", Map.class);
		if (page == null) {
			page = new Page<Groups>(1, 10);
		}  
		List<String> timeRangeList = (List<String>) param.get("createTimeRange");
		if (null != timeRangeList && timeRangeList.size() > 0) {
			groupsMap.put("startTime", timeRangeList.get(0));
			groupsMap.put("endTime", timeRangeList.get(1));
		}
		return groupsService.selectGroupsList(page, groupsMap);
    }
	
	/**
     * 团购群分页查询
     */
	@RequestMapping(value="/bulkBuySelectPage", method={RequestMethod.POST})
   	public BaseResult bulkBuySelectPage(@RequestBody Map<String, Object> param) throws Exception {
		Page<Groups> page = JsonObjUtils.map2obj(param, "page", Page.class);
		Map<String, Object> groupsMap = JsonObjUtils.map2obj(param, 
				"groups", Map.class);
		if (page == null) {
			page = new Page<Groups>(1, 10);
		}  
		List<String> timeRangeList = (List<String>) param.get("createTimeRange");
		if (null != timeRangeList && timeRangeList.size() > 0) {
			groupsMap.put("startTime", timeRangeList.get(0));
			groupsMap.put("endTime", timeRangeList.get(1));
		}
		return groupsService.selectBulkBuyGroupsList(page, groupsMap);
    }
	
	/**
     * 群主发送消息
     */
	@RequestMapping(value="/sendMessage", method={RequestMethod.POST})
   	public BaseResult sendMessage(@RequestBody Map<String, Object> param) throws Exception {
		return groupsService.sendMessage(param);
    }
	
	/**
     * 查询群主发送的消息
     */
	@RequestMapping(value="/getSendMessageList", method={RequestMethod.POST})
   	public BaseResult getSendMessageList(@RequestBody Map<String, Object> param) throws Exception {
		return groupsService.getSendMessageList(param);
    }
    
    /**
     * 自定义查询
     */
    @RequestMapping(value="/customQuery/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult customQuery(@PathVariable("id") int id) {
		BaseResult baseResult = groupsService.customQuery(id); 
        return baseResult;
    }
	
}
