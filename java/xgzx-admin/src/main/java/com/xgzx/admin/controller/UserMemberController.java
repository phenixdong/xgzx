package com.xgzx.admin.controller;


import java.util.Arrays;
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
import com.xgzx.admin.entity.MemberType;
import com.xgzx.admin.entity.Rebate;
import com.xgzx.admin.entity.UserMember;
import com.xgzx.admin.service.MemberTypeService;
import com.xgzx.admin.service.UserMemberService;
import com.xgzx.base.BaseResult;
import com.xgzx.util.JsonObjUtils;

/**
 * @author DaiDong
 * @since 2017-12-24
 * @email 755144556@qq.com
 */
@RestController
@RequestMapping("/userMember")
public class UserMemberController {
	
	@Autowired
	UserMemberService userMemberService;
	@Autowired
	MemberTypeService memberTypeService;

	/**
     * 新增
     */
	@RequestMapping(value="/insert", method={RequestMethod.POST})
   	public BaseResult insert(@RequestBody Map<String, Object> param) {
		UserMember userMember = null;
		try {
			userMember = JsonObjUtils.map2obj(param, "userMember", UserMember.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		boolean success = userMemberService.insert(userMember); 
        if (success) {
        	return new BaseResult("1", "成功", userMember);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
	
	/**
     * 查询userId的会员信息
     */
	@RequestMapping(value="/getMemberInfo", method={RequestMethod.POST})
   	public BaseResult getMemberInfo(@RequestBody Map<String, Object> param) throws Exception {
		String userId = (String) param.get("userId");
		// 会员信息
		UserMember userMember = new UserMember();
		userMember.setUserId(userId);
		EntityWrapper wrapper = new EntityWrapper<>(userMember);
		userMember = userMemberService.selectOne(wrapper);
		
		// 会员类型
		MemberType memberType = null;
		if (null != userMember) {
			memberType = new MemberType();
			memberType.setMemberTypeId(userMember.getMemberTypeId());
			EntityWrapper memberTypeWrapper = new EntityWrapper<>(memberType);
			memberType = memberTypeService.selectOne(memberTypeWrapper);
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("userMember", userMember);
		map.put("memberType", memberType);
		return new BaseResult(map);
    }

	/**
     * 删除
     */
	@RequestMapping(value="/delete/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult delete(@PathVariable("id") String id) {
		boolean success = userMemberService.deleteById(Long.valueOf(id)); 
        if (success) {
        	return new BaseResult("1", "成功", null);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
    
    /**
     * 批量删除
     */
	@RequestMapping(value="/deleteBatch/{ids}", method={RequestMethod.POST})
   	public BaseResult deleteBatch(@PathVariable("ids") String ids) {
   		List<String> idList = Arrays.asList(ids.split("\\,"));
		boolean success = userMemberService.deleteBatchIds(idList); 
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
		UserMember userMember = null;
		try {
			userMember = JsonObjUtils.map2obj(param, "userMember", UserMember.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		boolean success = userMemberService.updateById(userMember); 
        if (success) {
        	return new BaseResult("1", "成功", userMember);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
    
    /**
     * 查询
     */
	@RequestMapping(value="/select/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult selectById(@PathVariable("id") String id) {
		UserMember userMember = userMemberService.selectById(Long.valueOf(id)); 
        return new BaseResult("1", "成功", userMember);
    }
    
    /**
     * 分页查询
     */
	@RequestMapping(value="/selectPage", method={RequestMethod.POST})
   	public BaseResult selectPage(@RequestBody Map<String, Object> param) throws Exception {
   		Page<UserMember> page = JsonObjUtils.map2obj(param, "page", Page.class);
   		UserMember userMember = JsonObjUtils.map2obj(param, "userMember", UserMember.class);
		Page<UserMember> pageUserMember = userMemberService.selectPage(page, 
				new EntityWrapper<>(userMember)); 
        return new BaseResult("1", "成功", pageUserMember);
    }
    
    /**
     * 自定义查询
     */
    @RequestMapping(value="/customQuery/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult customQuery(@PathVariable("id") int id) {
		BaseResult baseResult = userMemberService.customQuery(id); 
        return baseResult;
    }
	
    /**
     * 升级会员
     */
//    @RequestMapping(value="/upgradeMember", method={RequestMethod.POST})
//   	public BaseResult upgradeMember(@RequestBody Map<String, Object> param) {
//    	Long userId = Long.valueOf((String) param.get("userId"));
//    	Integer memberId = Integer.valueOf((String) param.get("memberId"));
//    	//查询会员类型
//    	Member member = memberService.selectById(memberId);
//    	Date startTime = new Date();
//    	Calendar rightNow = Calendar.getInstance();
//    	rightNow.setTime(startTime);
//    	rightNow.add(Calendar.MONTH, member.getPeriod());
//    	Date endTime = rightNow.getTime();
//    	
//    	//存表
//    	UserMember userMember = new UserMember();
//    	userMember.setUserId(userId);
//    	userMember.setMemberId(memberId);
//    	userMember.setStartTime(startTime);
//    	userMember.setEndTime(endTime);
//    	userMemberService.insert(userMember);
//    	
//    	return new BaseResult("1", "成功", null);
//    }
//    
}
