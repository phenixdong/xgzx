package com.xgzx.admin.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.xgzx.admin.entity.User;
import com.xgzx.admin.service.UserService;
import com.xgzx.base.BaseErrResult;
import com.xgzx.base.BaseResult;
import com.xgzx.exception.CommonError;
import com.xgzx.util.JsonObjUtils;
import com.xgzx.util.UuidKeyUtil;

/**
 * @author DaiDong
 * @since 2017-12-24
 * @email 755144556@qq.com
 */
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService userService; 

	/**
     * 新增
     */
	@RequestMapping(value="/insert", method={RequestMethod.POST})
   	public BaseResult insert(@RequestBody Map<String, Object> param) {
		User user = null;
		try {
			user = JsonObjUtils.map2obj(param, "user", User.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		user.setUserId(UuidKeyUtil.get12UUID());
		boolean success = userService.insert(user); 
        if (success) {
        	return new BaseResult("1", "成功", user);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }

	/**
     * 删除
     */
	@RequestMapping(value="/delete/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult delete(@PathVariable("id") String id) {
		boolean success = userService.deleteById(Long.valueOf(id)); 
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
		
   		// boolean success = userService.deleteBatchIds(idList); 
   		for (String id : idList) {
   			User user = userService.selectById(id);
   			user.setValidTag(0);
			boolean success = userService.updateById(user);
			if (!success) {
				CommonError.CommonErr(new BaseErrResult("-1", "删除失败"));
			}
		} 
   		return new BaseResult("1", "成功", null);
    }

	/**
     * 锁定、解锁
     */
	@RequestMapping(value="/lock", method={RequestMethod.POST})
   	public BaseResult lock(@RequestBody Map<String, Object> param) {
		String userId = (String) param.get("userId");
		int userState = (Integer) param.get("userState");
		User user = new User();
		user.setUserId(userId);
		user.setUserState(userState);
		boolean success = userService.updateById(user); 
        if (success) {
        	return new BaseResult("1", "成功", user);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
	}
	
	/**
     * 修改
     */
	@RequestMapping(value="/update", method={RequestMethod.POST})
   	public BaseResult update(@RequestBody Map<String, Object> param) {
		User user = null;
		try {
			user = JsonObjUtils.map2obj(param, "user", User.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		boolean success = userService.updateById(user); 
        if (success) {
        	return new BaseResult("1", "成功", user);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
    
    /**
     * 查询
     */
	@RequestMapping(value="/select/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult selectById(@PathVariable("id") String id) {
		User user = userService.selectById(id); 
        return new BaseResult("1", "成功", user);
    }
    
    /**
     * 分页查询
     */
	@RequestMapping(value="/selectPage", method={RequestMethod.POST})
   	public BaseResult selectPage(@RequestBody Map<String, Object> param) throws Exception {
		Page<User> page = JsonObjUtils.map2obj(param, "page", Page.class);
		Map<String, Object> userMap = JsonObjUtils.map2obj(param, "user", Map.class);
		if (page == null) {
			page = new Page<User>(1, 10);
		}  
		List<String> timeRangeList = (List<String>) param.get("createTimeRange");
		if (null != timeRangeList && timeRangeList.size() > 0) {
			userMap.put("startTime", timeRangeList.get(0));
			userMap.put("endTime", timeRangeList.get(1));
		}
		return userService.selectUserList(page, userMap);
    }
	
    /**
     * 自定义查询
     */
    @RequestMapping(value="/customQuery/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult customQuery(@PathVariable("id") int id) {
		BaseResult baseResult = userService.customQuery(id); 
        return baseResult;
    }
    
    /**
     * 登录
     */
    @RequestMapping(value="/login", method={RequestMethod.POST})
   	public BaseResult login(@RequestBody Map<String, Object> param) {
    	return userService.login(param);
    }
    
    /**
     * 注册
     */
    @RequestMapping(value="/register", method={RequestMethod.POST})
   	public BaseResult register(@RequestBody Map<String, Object> param) {
    	return userService.register((String) param.get("openId"), param);
    }
	
}
