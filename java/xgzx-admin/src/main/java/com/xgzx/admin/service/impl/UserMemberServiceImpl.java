package com.xgzx.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xgzx.base.BaseResult;
import com.xgzx.admin.entity.UserMember;
import com.xgzx.admin.mapper.UserMemberMapper;
import com.xgzx.admin.service.UserMemberService;

/**
 * @author DaiDong
 * @since 2017-12-24
 * @email 755144556@qq.com
 */
@Service
@Transactional
public class UserMemberServiceImpl extends ServiceImpl<UserMemberMapper, UserMember> implements UserMemberService {
	
	@Autowired
	UserMemberMapper userMemberMapper;
	
	@Override
	public BaseResult customQuery(int id) {
		UserMember userMember = userMemberMapper.customQuery(id);
		return new BaseResult("1", "成功", userMember);
	}

	@Override
	public int insertOrUpdateByUserId(UserMember userMember) {
		Map<String, Object> map = new HashMap<>();
		map.put("user_id", userMember.getUserId());
		List<UserMember> list = userMemberMapper.selectByMap(map);
		if (null != list && list.size() > 0) {
			Long userMemberId = list.get(0).getUserMemberId();
			userMember.setUserMemberId(userMemberId);
			return userMemberMapper.updateById(userMember);
		} else {
			return userMemberMapper.insert(userMember);
		}
	}
}
