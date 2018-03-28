package com.xgzx.admin.controller;


import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xgzx.admin.entity.AdminUser;
import com.xgzx.admin.service.AdminUserService;
import com.xgzx.admin.util.ImageCode;
import com.xgzx.base.BaseResult;
import com.xgzx.util.EncryptUtils;
import com.xgzx.util.JsonObjUtils; 
 

/**
 * @author DaiDong
 * @since 2018-01-19
 * @email 755144556@qq.com
 */
@RestController
@RequestMapping("/adminUser")
public class AdminUserController {
	
	@Autowired
	AdminUserService adminUserService;
	@Autowired
	HttpServletRequest request;
	@Autowired
	HttpServletResponse response;

	/**
     * 登录
     */
	@RequestMapping(value="/login", method={RequestMethod.POST})
   	public BaseResult login(@RequestBody Map<String, Object> param) {
		String loginId = (String) param.get("loginId");
		String password = (String) param.get("password");
		String checkCode = (String) param.get("checkCode");
		
		String localCheckCode = (String) request.getSession().getAttribute("checkCode");
		if (!checkCode.equals(localCheckCode)) {
			request.getSession().setAttribute("checkCode", "");
			return new BaseResult("0", "验证码错误", null);
		}
		
		// 根据loginId查询
		AdminUser adminUser = new AdminUser();
		adminUser.setLoginId(loginId);
		adminUser = adminUserService.selectOne(
				new EntityWrapper<AdminUser>(adminUser));
		if (null == adminUser) {
			return new BaseResult("0", "用户名或密码错误", null);
		}
		// 比较密码
		String passwordEncodeStr = EncryptUtils.encodeMD5String(
    			EncryptUtils.encodeBase64String(password));
		if (!passwordEncodeStr.equals(adminUser.getPassword())) {
			return new BaseResult("0", "用户名或密码错误", null);
		}
		
		// 存储loginId，用于鉴权登录状态
		request.getSession().setAttribute("loginId", adminUser.getLoginId());
		
     	return new BaseResult("1", "成功", adminUser);
    }
	
	/**
     * 登录
     */
	@RequestMapping(value="/changePassword", method={RequestMethod.POST})
   	public BaseResult changePassword(@RequestBody Map<String, Object> param) {
		String loginId = (String) param.get("loginId");
		String oldPassword = (String) param.get("oldPassword");
		String newPassword = (String) param.get("newPassword");
				
		// 根据loginId查询
		AdminUser adminUser = new AdminUser();
		adminUser.setLoginId(loginId);
		adminUser = adminUserService.selectOne(
				new EntityWrapper<AdminUser>(adminUser));
		if (null == adminUser) {
			return new BaseResult("0", "用户名或密码错误", null);
		}
		
		// 比较密码
		String passwordEncodeStr = EncryptUtils.encodeMD5String(
    			EncryptUtils.encodeBase64String(oldPassword));
		if (!passwordEncodeStr.equals(adminUser.getPassword())) {
			return new BaseResult("0", "用户名或密码错误", null);
		}
		
		String newPasswordEncodeStr = EncryptUtils.encodeMD5String(
    			EncryptUtils.encodeBase64String(newPassword));
		adminUser.setPassword(newPasswordEncodeStr);
		boolean success = adminUserService.updateById(adminUser);
		if (!success) {
			return new BaseResult("0", "更新失败", null);
		}
     	return new BaseResult("1", "成功", adminUser);
    }
	
	@RequestMapping(value = "/checkCode")
	public String checkCode() throws Exception {
		OutputStream os = response.getOutputStream();
		Map<String, Object> imageMap = ImageCode.getImageCode(60, 20, os);
		String checkCode = imageMap.get("strEnsure").toString().toLowerCase();
		request.getSession().setAttribute("checkCode", checkCode);
		ImageIO.write((BufferedImage) imageMap.get("image"), "JPEG", os);
		return "ok";
	}
	
	/**
     * 登录
     */
	@RequestMapping(value="/logout")
   	public BaseResult logout() {
		request.getSession().removeAttribute("loginId");
		return new BaseResult();
	}
	
	/**
     * 新增
     */
	@RequestMapping(value="/insert", method={RequestMethod.POST})
   	public BaseResult insert(@RequestBody Map<String, Object> param) {
		AdminUser adminUser = null;
		try {
			adminUser = JsonObjUtils.map2obj(param, AdminUser.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		boolean success = adminUserService.insert(adminUser); 
        if (success) {
        	return new BaseResult("1", "成功", adminUser);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }

	/**
     * 删除
     */
	@RequestMapping(value="/delete/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult delete(@PathVariable("id") String id) {
		boolean success = adminUserService.deleteById(Long.valueOf(id)); 
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
		
		boolean success = adminUserService.deleteBatchIds(idList); 
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
		AdminUser adminUser = JsonObjUtils.map2obj(param, AdminUser.class);
		boolean success = adminUserService.updateById(adminUser); 
        if (success) {
        	return new BaseResult("1", "成功", adminUser);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
    
    /**
     * 查询
     */
	@RequestMapping(value="/select/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult selectById(@PathVariable("id") String id) {
		AdminUser adminUser = adminUserService.selectById(Long.valueOf(id)); 
        return new BaseResult("1", "成功", adminUser);
    }
    
    /**
     * 分页查询
     */
	@RequestMapping(value="/selectPage", method={RequestMethod.POST})
   	public BaseResult selectPage(@RequestBody Map<String, Object> param) {
   		Page<AdminUser> page = null;
   		AdminUser adminUser = null;
   		try {
	   		page = JsonObjUtils.map2obj(param, "page", Page.class);
			adminUser = JsonObjUtils.map2obj(param, "adminUser", AdminUser.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		Page<AdminUser> pageAdminUser = adminUserService.selectPage(page, 
				new EntityWrapper<>(adminUser)); 
        return new BaseResult("1", "成功", pageAdminUser);
    }
    
    /**
     * 自定义查询
     */
    @RequestMapping(value="/customQuery/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult customQuery(@PathVariable("id") int id) {
		BaseResult baseResult = adminUserService.customQuery(id); 
        return baseResult;
    }
	
}
