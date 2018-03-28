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
import java.util.Arrays;
import com.xgzx.base.BaseResult;
import com.xgzx.util.JsonObjUtils;
import com.xgzx.admin.service.AccountService;
import com.xgzx.admin.entity.Account;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author DaiDong
 * @since 2018-01-10
 * @email 755144556@qq.com
 */
@RestController
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	AccountService accountService;

	/**
     * 新增
     */
	@RequestMapping(value="/insert", method={RequestMethod.POST})
   	public BaseResult insert(@RequestBody Map<String, Object> param) {
		Account account = null;
		try {
			account = JsonObjUtils.map2obj(param, Account.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		boolean success = accountService.insert(account); 
        if (success) {
        	return new BaseResult("1", "成功", account);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }

	/**
     * 删除
     */
	@RequestMapping(value="/delete/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult delete(@PathVariable("id") String id) {
		boolean success = accountService.deleteById(Long.valueOf(id)); 
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
		
		boolean success = accountService.deleteBatchIds(idList); 
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
		Account account = null;
		try {
			account = JsonObjUtils.map2obj(param, Account.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		boolean success = accountService.updateById(account); 
        if (success) {
        	return new BaseResult("1", "成功", account);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
    
    /**
     * 查询
     */
	@RequestMapping(value="/select/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult selectById(@PathVariable("id") String id) {
		Account account = accountService.selectById(Long.valueOf(id)); 
        return new BaseResult("1", "成功", account);
    }
    
    /**
     * 分页查询
     */
	@RequestMapping(value="/selectPage", method={RequestMethod.POST})
   	public BaseResult selectPage(@RequestBody Map<String, Object> param) {
   		Page<Account> page = null;
   		Account account = null;
   		try {
	   		page = JsonObjUtils.map2obj(param, "page", Page.class);
			account = JsonObjUtils.map2obj(param, "account", Account.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		Page<Account> pageAccount = accountService.selectPage(page, 
				new EntityWrapper<>(account)); 
        return new BaseResult("1", "成功", pageAccount);
    }
    
    /**
     * 自定义查询
     */
    @RequestMapping(value="/customQuery/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult customQuery(@PathVariable("id") int id) {
		BaseResult baseResult = accountService.customQuery(id); 
        return baseResult;
    }
	
}
