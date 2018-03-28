package com.xgzx.admin.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import java.util.Map;
import java.util.List;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import com.xgzx.base.BaseResult;
import com.xgzx.util.JsonObjUtils;
import com.xgzx.util.UuidKeyUtil;
import com.xgzx.admin.service.GroupsUserService;
import com.xgzx.admin.service.LessonService;
import com.xgzx.admin.service.MemberTypeService;
import com.xgzx.admin.service.OrdersService;
import com.xgzx.admin.service.UserService;
import com.xgzx.admin.entity.Groups;
import com.xgzx.admin.entity.GroupsUser;
import com.xgzx.admin.entity.Lesson;
import com.xgzx.admin.entity.MemberType;
import com.xgzx.admin.entity.Orders;
import com.xgzx.admin.entity.User;
import com.xgzx.admin.mapper.GroupsMapper;
import com.xgzx.admin.mapper.GroupsUserMapper;
import com.xgzx.admin.mapper.UserMapper;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author DaiDong
 * @since 2018-01-10
 * @email 755144556@qq.com
 */
@RestController
@RequestMapping("/test")
public class TestDataController {
	
	@Autowired
	OrdersService ordersService;
	@Autowired
	UserService userService;
	@Autowired
	GroupsMapper groupsMapper;
	@Autowired
	UserMapper userMapper;
	@Autowired
	GroupsUserMapper groupsUserMapper;

	 
	/**
     * 生成群组
     * http://localhost:9001/admin/test/addGroups
     */
    @RequestMapping(value="/addGroups", method={RequestMethod.GET})
   	public BaseResult addGroups() {
    	List<Map<String, Object>> recommenderList = userMapper.testGetMasterList();
    	for (Map<String, Object> map : recommenderList) {
    		String recommenderUserId = map.get("id") + "";
    		String recommenderUserName = map.get("name") + "的群组";
    		
    		Groups groups = new Groups();
    		HashMap<String, Object> queryMap = new HashMap<>();
    		queryMap.put("master_user_id", recommenderUserId);
    		List<Groups> queryList = groupsMapper.selectByMap(queryMap);
    		if (null == queryList || 0 == queryList.size()) {
	    		groups.setGroupName(recommenderUserName);
	    		groups.setMasterUserId(recommenderUserId);
	    		groups.setValidTag(1);
	    		groupsMapper.insertReturnId(groups);
    		} else {
    			groups = queryList.get(0);
    		}
    		
    		Integer groupsId = groups.getGroupId();
    		
    		HashMap<String, Object> queryUserMap = new HashMap<>();
    		queryUserMap.put("recommender_user_id", recommenderUserId);
    		List<User> groupsUserList = userMapper.selectByMap(queryUserMap);
    		for (User item : groupsUserList) {
    			HashMap<String, Object> queryGroupsUserMap = new HashMap<>();
    			queryGroupsUserMap.put("group_id", groupsId);
    			queryGroupsUserMap.put("user_id", item.getUserId());
        		List<GroupsUser> queryGroupsUserList = groupsUserMapper.selectByMap(queryGroupsUserMap);
    			if (null == queryGroupsUserList || 0 == queryGroupsUserList.size()) {
	    			GroupsUser groupsUser = new GroupsUser();
	    			groupsUser.setGroupId(groupsId);
	    			groupsUser.setUserId(item.getUserId());
	    			groupsUser.setMessageReadTime(new Date());
	    			groupsUser.setValidTag(1);
	    			groupsUserMapper.insert(groupsUser);
    			}
    		}
    	}
        return new BaseResult();
    }
	
    /**
     * 生成测试数据
     * http://localhost:9001/admin/test/addOrders
     */
    @RequestMapping(value="/addOrders", method={RequestMethod.GET})
   	public BaseResult addOrders() {
    	List<User> userList = userService.selectList(new EntityWrapper<>(new User()));
    	for (User user : userList) {
    		Orders orders = new Orders();
    		orders.setOrderId(new UuidKeyUtil().get32UUID());
    		orders.setUserId(user.getUserId());
    		orders.setProductId("1");
    		orders.setProductType(1);
    		orders.setPayState(1);
    		orders.setTotalFee(new BigDecimal(10));
    		orders.setValidTag(1);
    		orders.setCreateTime(new Date());
        	ordersService.insert(orders);
    	}
        return new BaseResult();
    }
}
