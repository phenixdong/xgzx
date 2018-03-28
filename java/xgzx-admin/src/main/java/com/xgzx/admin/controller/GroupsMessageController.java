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

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xgzx.admin.entity.GroupsMessage;
import com.xgzx.admin.entity.GroupsUser;
import com.xgzx.admin.entity.User;
import com.xgzx.admin.service.GroupsMessageService;
import com.xgzx.admin.service.GroupsUserService;
import com.xgzx.admin.service.UserService;
import com.xgzx.base.BaseResult;
import com.xgzx.util.JsonObjUtils;

/**
 * @author DaiDong
 * @since 2018-02-03
 * @email 755144556@qq.com
 */
@RestController
@RequestMapping("/groupsMessage")
public class GroupsMessageController {
	
	@Autowired
	GroupsMessageService groupsMessageService;
	@Autowired
	UserService userService;
	@Autowired
	GroupsUserService groupsUserService;

	/**
     * 新增
     */
	@RequestMapping(value="/insert", method={RequestMethod.POST})
   	public BaseResult insert(@RequestBody Map<String, Object> param) throws Exception {
		GroupsMessage groupsMessage = groupsMessage = JsonObjUtils.map2obj(param, GroupsMessage.class);
		
		// 查询是否为群成员
		Map<String, Object> groupsUserMap = new HashMap<>();
		groupsUserMap.put("user_id", groupsMessage.getUserId());
		groupsUserMap.put("group_id", groupsMessage.getGroupId());
		groupsUserMap.put("valid_tag", 1);
		List<GroupsUser> groupsUserList = groupsUserService.selectByMap(groupsUserMap);
		if (null == groupsUserList || 0 == groupsUserList.size()) {
			return new BaseResult("0", "非群成员", null);
		}
		
		// 新增
		groupsMessage.setValidTag(1);
		boolean success = groupsMessageService.insert(groupsMessage); 
        if (success) {
        	return new BaseResult("1", "成功", groupsMessage);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }

	/**
     * 查询群成员收到的消息
     */
	@RequestMapping(value="/getReceivedMessageList", method={RequestMethod.POST})
   	public BaseResult getReceivedMessageList(@RequestBody Map<String, Object> param) throws Exception {
		Page<GroupsMessage> page = JsonObjUtils.map2obj(param, "page", Page.class);
		return groupsMessageService.getReceivedMessage(page, param);
    }
	
	/**
     * 查询最新消息
     */
	@RequestMapping(value="/getGroupsNewMessageList", method={RequestMethod.POST})
   	public BaseResult getGroupsNewMessageList(@RequestBody Map<String, Object> param) throws Exception {
		List<GroupsMessage> list = groupsMessageService.getGroupsNewMessageList(param);
		return new BaseResult(list);
    }
	
	/**
     * 统计新消息数量
     */
	@RequestMapping(value="/getNewMessageGroups", method={RequestMethod.POST})
   	public BaseResult getNewMessageGroups(@RequestBody Map<String, Object> param) throws Exception {
		return groupsMessageService.getNewMessageGroups(param);
    }
	
	/**
     * 删除
     */
	@RequestMapping(value="/delete/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult delete(@PathVariable("id") String id) {
		boolean success = groupsMessageService.deleteById(Long.valueOf(id)); 
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
		
		boolean success = groupsMessageService.deleteBatchIds(idList); 
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
		GroupsMessage groupsMessage = null;
		try {
			groupsMessage = JsonObjUtils.map2obj(param, GroupsMessage.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", "失败", e.getMessage());
		}
		boolean success = groupsMessageService.updateById(groupsMessage); 
        if (success) {
        	return new BaseResult("1", "成功", groupsMessage);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
    
    /**
     * 查询
     */
	@RequestMapping(value="/select/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult selectById(@PathVariable("id") String id) {
		GroupsMessage groupsMessage = groupsMessageService.selectById(Long.valueOf(id)); 
        return new BaseResult("1", "成功", groupsMessage);
    }
    
    /**
     * 分页查询
     */
	@RequestMapping(value="/selectPage", method={RequestMethod.POST})
   	public BaseResult selectPage(@RequestBody Map<String, Object> param) throws Exception {
   		Page<GroupsMessage> page = JsonObjUtils.map2obj(param, "page", Page.class);
   		GroupsMessage groupsMessage = new GroupsMessage();
   		Integer groupId = (Integer) param.get("groupId");
   		groupsMessage.setGroupId(groupId);
   		EntityWrapper wrapper = new EntityWrapper<>(groupsMessage);
   		wrapper.orderBy("create_time", false);
		Page<GroupsMessage> pageGroupsMessage = groupsMessageService
				.selectPage(page, wrapper);
		
		for (GroupsMessage item : pageGroupsMessage.getRecords()) {
			User user = userService.selectById(item.getUserId());
			item.setUser(user);
		}
        return new BaseResult("1", "成功", pageGroupsMessage);
    }
    
    /**
     * 自定义查询
     */
    @RequestMapping(value="/customQuery/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult customQuery(@PathVariable("id") int id) {
		BaseResult baseResult = groupsMessageService.customQuery(id); 
        return baseResult;
    }
	
}
