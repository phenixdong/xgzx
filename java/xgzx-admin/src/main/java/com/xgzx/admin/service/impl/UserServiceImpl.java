package com.xgzx.admin.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xgzx.admin.entity.Account;
import com.xgzx.admin.entity.Groups;
import com.xgzx.admin.entity.GroupsUser;
import com.xgzx.admin.entity.PaidLesson;
import com.xgzx.admin.entity.Rebate;
import com.xgzx.admin.entity.User;
import com.xgzx.admin.entity.UserMember;
import com.xgzx.admin.mapper.GroupsMapper;
import com.xgzx.admin.mapper.PaidLessonMapper;
import com.xgzx.admin.mapper.UserMapper;
import com.xgzx.admin.service.AccountService;
import com.xgzx.admin.service.GroupsUserService;
import com.xgzx.admin.service.RebateService;
import com.xgzx.admin.service.UserMemberService;
import com.xgzx.admin.service.UserService;
import com.xgzx.base.BaseErrResult;
import com.xgzx.base.BaseResult;
import com.xgzx.exception.CommonError;
import com.xgzx.util.UuidKeyUtil;

/**
 * @author DaiDong
 * @since 2017-12-24
 * @email 755144556@qq.com
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
	
	@Autowired 
	HttpServletRequest request;
	@Autowired
	UserMapper userMapper;
	@Autowired
	AccountService accountService;
	@Autowired
	RebateService rebateService;
	@Autowired
	UserMemberService userMemberService; 
	@Autowired
	PaidLessonMapper paidLessonMapper;
	@Autowired
	GroupsMapper groupsMapper;
	@Autowired
	GroupsUserService groupsUserService;
	
	@Override
	public BaseResult customQuery(int id) {
		User user = userMapper.customQuery(id);
		return new BaseResult("1", "成功", user);
	}
	
	@Override
	public boolean isPaid(String userId, Integer lessonId) {
		// 10分钟之内，发起过支付请求的，都允许通过，防止支付消息接收延迟
		if (null != request.getSession().getAttribute("doUnifiedOrderTime")) {
			Long doUnifiedOrderTime = (Long) request.getSession().getAttribute("doUnifiedOrderTime");
			if (System.currentTimeMillis() - doUnifiedOrderTime < 10 * 60 * 1000) {
				return true;
			}
		}
		
		// 是否会员
		EntityWrapper wrapper = new EntityWrapper<>(new UserMember());
   		wrapper.where("user_id = {0}", userId);
		UserMember userMember = userMemberService.selectOne(wrapper);
		if (null != userMember && userMember.getEndTime().after(new Date())) {
			return true;
		}
		
		// 是否单独购买
		Map<String, Object> paidLessonMap = new HashMap<>();
		paidLessonMap.put("user_id", userId);
		paidLessonMap.put("lesson_id", lessonId);
		paidLessonMap.put("valid_tag", 1);
		List<PaidLesson> paidLessonList = paidLessonMapper.selectByMap(paidLessonMap);
		if (null != paidLessonList && paidLessonList.size() > 0) {
			return true;
		}
		
		return false;
	}

	@Override
	public BaseResult selectUserList(Page<User> page, Map<String, Object> userMap) {
		List<User> list = userMapper.selectUserList(page, userMap); 
		page.setRecords(list);
		return new BaseResult(page);
	}
 
 	@Override
	public BaseResult register(String openId, Map<String, Object> userInfoMap) {
		String nickName = (String) userInfoMap.get("nickname"); 
        Integer sex = (Integer) userInfoMap.get("sex"); 
        String province = (String) userInfoMap.get("province");
        String city = (String) userInfoMap.get("city");
        String imgUrl = (String) userInfoMap.get("headimgurl");
        
    	User user = new User();
    	user.setOpenId(openId);
    	user.setValidTag(1);
    	user = userMapper.selectOne(user);
    	// 新用户，注册
    	if (null == user) {
    		// 存用户表
    		user = new User();
    		String userId = UuidKeyUtil.get12UUID();
    		user.setUserId(userId);
    		user.setOpenId(openId);
    		user.setValidTag(1);
    		user.setUserName(nickName);
    		user.setNickName(nickName);
    		user.setSex(sex);
    		user.setProvince(province);
    		user.setCity(city);
    		user.setImageUrl(imgUrl);
    		Date nowTime = new Date();
    		user.setCreateTime(nowTime);
    		user.setUpdateTime(nowTime);
    		Integer ret = userMapper.insert(user);
    		if (ret <= 0) {
    			CommonError.CommonErr(new BaseErrResult("-1", "用户注册失败"));
    		}
    		    		
    		// 新增用户资产账户
    		Account account = new Account();
    		account.setUserId(userId);
    		account.setNumber(new BigDecimal(0));
    		account.setValidTag(1);
    		accountService.insert(account);
    		
    		// 新增返点账户
    		Rebate rebate = new Rebate();
    		rebate.setUserId(userId);
    		rebate.setNumber(new BigDecimal(0));
    		rebate.setValidTag(1);
    		rebateService.insert(rebate);
    		
    	// 老用户，更新用户信息
    	} else {
    		user.setNickName(nickName);
    		user.setUserName(nickName);
    		user.setSex(sex);
    		user.setProvince(province);
    		user.setCity(city);
    		user.setImageUrl(imgUrl);
    		userMapper.updateById(user);
    	}
    	return new BaseResult(user);
	}
	
	/*  
	 * 登录（新用户注册在getOpenId中），
	 * 返回用户信息和会员身份
	 */
	@Override
	public BaseResult login(Map<String, Object> param) {
		System.out.println("login");
		if (null == param.get("openId")) {
			return new BaseResult("0", "openId为空", null);
		}
		String openId = (String) param.get("openId");
		System.out.println("login openId " + openId);
		
		Map<String, Object> map = new HashMap<>();
		map.put("openId", openId);
		User user = userMapper.getUserInfo(map);
		if (null == user) {
			return new BaseResult("0", "没有该用户", null);
		}
		// 会员是否到期
		if (null == user.getEndTime()) {
			// 非会员
			user.setIsMemberValid(false);
			user.setMemberTypeId(0);// 没有会员类型
		} else {
			if (user.getEndTime().before(new Date())) {
				user.setIsMemberValid(false);
				user.setMemberTypeId(0);// 没有会员类型
			} else {
				user.setIsMemberValid(true);
			}
		}
		
		// 购买课程列表
		Map<String, Object> paidLessonMap = new HashMap<>();
		paidLessonMap.put("user_id", user.getUserId());
		paidLessonMap.put("valid_tag", 1);
		List<PaidLesson> paidLessonList = paidLessonMapper.selectByMap(paidLessonMap);
		if (null != paidLessonList && paidLessonList.size() > 0) {
			Integer[] ids = new Integer[paidLessonList.size()];
			for (int i = 0; i < paidLessonList.size(); i++) {
				ids[i] = paidLessonList.get(i).getLessonId();
			}
			user.setPaidLessonIds(ids);
		}
		
		// 用户没有推荐人，则记录当前推荐人
		if (null != param.get("recommenderUserId") && !"".equals(param.get("recommenderUserId"))) {
			if (null == user.getRecommenderUserId()) {
				// 推荐人不能是自己
				String recommenderUserId = (String) param.get("recommenderUserId");
				if (!user.getUserId().equals(recommenderUserId)) {
					user.setRecommenderUserId((String) param.get("recommenderUserId"));
					userMapper.updateById(user);
					
					// 加入推荐人群组
					Groups groups = new Groups();
					groups.setMasterUserId(recommenderUserId);
					groups = groupsMapper.selectOne(groups);
					if (null == groups) {// 没有群组，新建推荐人群组
						User recommenderUser = userMapper.selectById(recommenderUserId);
						groups = new Groups();
						groups.setMasterUserId(recommenderUserId);
						groups.setType(1);// 1普通群， 2团购群
						groups.setValidTag(1);
						groups.setCreateTime(new Date());
						groups.setGroupName(recommenderUser.getUserName() + "的群组");
						Integer ret = groupsMapper.insertReturnId(groups);
						if (ret <= 0) {
							CommonError.CommonErr(new BaseErrResult("-1", "群组创建失败"));
						}
						
						// 新增用户（该用户为推荐人）-群组关系
						Map<String, Object> recomGroupMemberMap = new HashMap<>();
						recomGroupMemberMap.put("group_id", groups.getGroupId());
						recomGroupMemberMap.put("user_id", user.getUserId());
						List<GroupsUser> recomGroupsUserList = groupsUserService.selectByMap(recomGroupMemberMap);
						if (null == recomGroupsUserList || 0 == recomGroupsUserList.size()) {
							// 新增
							GroupsUser recomGroupsUser = new GroupsUser();
							recomGroupsUser.setGroupId(groups.getGroupId());
							recomGroupsUser.setUserId(recommenderUserId);
							recomGroupsUser.setValidTag(1);
							recomGroupsUser.setMessageReadTime(new Date());
							groupsUserService.insert(recomGroupsUser);
						}
						System.out.println("userId " + user.getUserId() + " 加入群组");
					}
					
					int groupId = groups.getGroupId();
					System.out.println("群组ID: " + groupId);
					
					// 新增用户-群组关系
					Map<String, Object> groupMemberMap = new HashMap<>();
					groupMemberMap.put("group_id", groupId);
					groupMemberMap.put("user_id", user.getUserId());
					List<GroupsUser> groupsUserList = groupsUserService.selectByMap(groupMemberMap);
					if (null == groupsUserList || 0 == groupsUserList.size()) {
						// 新增
						GroupsUser groupsUser = new GroupsUser();
						groupsUser.setGroupId(groupId);
						groupsUser.setUserId(user.getUserId());
						groupsUser.setValidTag(1);
						groupsUser.setMessageReadTime(new Date());
						groupsUserService.insert(groupsUser);
					}
					System.out.println("userId " + user.getUserId() + " 加入群组");
				}
			}
		}
    	System.out.println("login user " + user.toString());
    	
    	// 登录状态，用于登录鉴权
    	request.getSession().setAttribute("loginId", user.getUserId());
        
    	// token 
		String token = UuidKeyUtil.get32UUID();
		request.getSession().setAttribute("token", token);
		user.setToken(token);		 
    			
    	return new BaseResult(user);
	}
 
 
}
