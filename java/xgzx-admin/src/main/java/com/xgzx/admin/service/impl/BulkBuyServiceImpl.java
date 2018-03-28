package com.xgzx.admin.service.impl;

import com.xgzx.admin.entity.BulkBuy;
import com.xgzx.admin.entity.BulkBuyRegister;
import com.xgzx.admin.entity.Groups;
import com.xgzx.admin.entity.GroupsUser;
import com.xgzx.admin.entity.User;
import com.xgzx.admin.entity.UserMember;
import com.xgzx.admin.mapper.BulkBuyMapper;
import com.xgzx.admin.mapper.BulkBuyRegisterMapper;
import com.xgzx.admin.mapper.GroupsMapper;
import com.xgzx.admin.mapper.UserMapper;
import com.xgzx.admin.service.BulkBuyService;
import com.xgzx.admin.service.GroupsUserService;
import com.xgzx.admin.service.UserMemberService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import com.xgzx.base.BaseErrResult;
import com.xgzx.base.BaseResult;
import com.xgzx.exception.CommonError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DaiDong
 * @since 2018-03-04
 * @email 755144556@qq.com
 */
@Service
@Transactional
public class BulkBuyServiceImpl extends ServiceImpl<BulkBuyMapper, BulkBuy> implements BulkBuyService {
	
	@Autowired
	BulkBuyMapper bulkBuyMapper;
	@Autowired
	BulkBuyRegisterMapper bulkBuyRegisterMapper;
	@Autowired
	UserMemberService userMemberService;
	@Autowired
	GroupsMapper groupsMapper;
	@Autowired
	UserMapper userMapper;
	@Autowired
	GroupsUserService groupsUserService;
	
	@Override
	public int insertReturnId(BulkBuy bulkBuy) {
		return bulkBuyMapper.insertReturnId(bulkBuy);
	}
	
	@Override
	public BaseResult selectList(Page<BulkBuy> page, Map<String, Object> map) {
		List<BulkBuy> list = bulkBuyMapper.selectList(page, map); 
		page.setRecords(list);
		return new BaseResult(page);
	}
	
	@Override
	public BaseResult register(Map<String, Object> param) {
		String userId = (String) param.get("userId");
		String bulkBuyId = (String) param.get("bulkBuyId");
		String password = (String) param.get("password");
		
		BulkBuy bulkBuy = bulkBuyMapper.selectById(bulkBuyId);
		// 团购数量是否已使用完
		if (bulkBuy.getRegisterNumber() >= bulkBuy.getNumber()) {
			CommonError.CommonErr(new BaseErrResult("-1", "集团用户已满"));
		}
		// 验证密码
		if (!password.equals(bulkBuy.getPassword())) {
			CommonError.CommonErr(new BaseErrResult("-1", "密码错误"));
		}
		
		// 团购记录+1
		Map<String, Object> map = new HashMap<>();
		map.put("user_id", userId);
		map.put("member_type_id", 2);
		List<UserMember> list = userMemberService.selectByMap(map);
		if (null == list || 0 == list.size()) {
			bulkBuy.setRegisterNumber(bulkBuy.getRegisterNumber() + 1);
			bulkBuyMapper.updateById(bulkBuy);
		} else {
			CommonError.CommonErr(new BaseErrResult("-1", "用户已是会员"));
		}
		
		// 新增团购注册记录
		BulkBuyRegister bulkBuyRegister = new BulkBuyRegister();
		bulkBuyRegister.setBulkBuyId(bulkBuyId);
		bulkBuyRegister.setUserId(userId);
		bulkBuyRegister.setValidTag(1);
		bulkBuyRegisterMapper.insert(bulkBuyRegister);
		
		// 升级包年会员
		Date startTime = new Date();
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(startTime);
		rightNow.add(Calendar.MONTH, 12);
		Date endTime = rightNow.getTime();
		UserMember userMember = new UserMember();	
		userMember.setUserId(userId);
		userMember.setMemberTypeId(2);// 1包月会员，2包年会员
		userMember.setStartTime(startTime);
		userMember.setEndTime(endTime);
		userMember.setBuyType(2);// 1单次购买，2团购
		userMember.setValidTag(1);
		userMemberService.insertOrUpdateByUserId(userMember);
		
		// 新增团购群组
		Groups groups = new Groups();
		groups.setMasterUserId(bulkBuyId);
		groups.setType(2);// 1普通群， 2团购群
		groups = groupsMapper.selectOne(groups);
		if (null == groups) {
			groups = new Groups();
			groups.setMasterUserId(bulkBuyId);
			groups.setType(2);// 1普通群， 2团购群
			groups.setValidTag(1);
			groups.setCreateTime(new Date());
			groups.setGroupName(bulkBuy.getCustomerName() + "的群组");
			Integer ret = groupsMapper.insertReturnId(groups);
			if (ret <= 0) {
				CommonError.CommonErr(new BaseErrResult("-1", "群组创建失败"));
			}
		}
		int groupId = groups.getGroupId();
		System.out.println("群组ID: " + groupId);
		
		// 新增用户-群组关系
		Map<String, Object> groupMemberMap = new HashMap<>();
		groupMemberMap.put("group_id", groupId);
		groupMemberMap.put("user_id", userId);
		List<GroupsUser> groupsUserList = groupsUserService.selectByMap(groupMemberMap);
		if (null == groupsUserList || 0 == groupsUserList.size()) {
			// 新增
			GroupsUser groupsUser = new GroupsUser();
			groupsUser.setGroupId(groupId);
			groupsUser.setUserId(userId);
			groupsUser.setValidTag(1);
			groupsUser.setMessageReadTime(new Date());
			groupsUserService.insert(groupsUser);
		}
		System.out.println("userId " + userId + " 加入群组");
		
		return new BaseResult("1", "成功", null);
	}
	 
}
