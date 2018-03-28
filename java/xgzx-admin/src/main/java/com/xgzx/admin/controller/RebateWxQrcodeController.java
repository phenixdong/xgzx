package com.xgzx.admin.controller;


import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

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
import com.xgzx.admin.entity.RebateWxQrcode;
import com.xgzx.admin.service.RebateWxQrcodeService;
import com.xgzx.admin.util.ImageCode;
import com.xgzx.base.BaseResult;
import com.xgzx.util.JsonObjUtils;

/**
 * @author DaiDong
 * @since 2018-01-25
 * @email 755144556@qq.com
 */
@RestController
@RequestMapping("/rebateWxQrcode")
public class RebateWxQrcodeController {
	
	@Autowired
	RebateWxQrcodeService rebateWxQrcodeService;
	@Autowired
	HttpServletResponse response;
	@Autowired
	HttpServletRequest request;
	

	/**
     * 新增
     */
	@RequestMapping(value="/insert", method={RequestMethod.POST})
   	public BaseResult insert(@RequestBody Map<String, Object> param) throws Exception {
		return rebateWxQrcodeService.insert(param); 
    }
	
	@RequestMapping(value = "/getRebateCheckCode")
	public String getRebateCheckCode() throws Exception {
		OutputStream os = response.getOutputStream();
		Map<String, Object> imageMap = ImageCode.getImageCode(60, 20, os);
		String checkCode = imageMap.get("strEnsure").toString().toLowerCase();
		request.getSession().setAttribute("rebateCheckCode", checkCode);
		ImageIO.write((BufferedImage) imageMap.get("image"), "JPEG", os);
		return "ok";
	}

	/**
     * 删除
     */
	@RequestMapping(value="/delete/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult delete(@PathVariable("id") String id) {
		boolean success = rebateWxQrcodeService.deleteById(Long.valueOf(id)); 
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
		
		boolean success = rebateWxQrcodeService.deleteBatchIds(idList); 
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
		RebateWxQrcode rebateWxQrcode = null;
		try {
			rebateWxQrcode = JsonObjUtils.map2obj(param, RebateWxQrcode.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		boolean success = rebateWxQrcodeService.updateById(rebateWxQrcode); 
        if (success) {
        	return new BaseResult("1", "成功", rebateWxQrcode);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
    
    /**
     * 查询
     */
	@RequestMapping(value="/select", method={RequestMethod.POST})
   	public BaseResult selectById(@RequestBody Map<String, Object> param) {
		String userId = (String) param.get("userId");
		RebateWxQrcode rebateWxQrcode = new RebateWxQrcode();
		rebateWxQrcode.setUserId(userId);
		EntityWrapper wrapper = new EntityWrapper<>(rebateWxQrcode);
   		wrapper.where("valid_tag = 1", "");
   		rebateWxQrcode = rebateWxQrcodeService.selectOne(wrapper);
        return new BaseResult("1", "成功", rebateWxQrcode);
    }
    
    /**
     * 分页查询
     */
	@RequestMapping(value="/selectPage", method={RequestMethod.POST})
   	public BaseResult selectPage(@RequestBody Map<String, Object> param) throws Exception {
   		RebateWxQrcode rebateWxQrcode = JsonObjUtils.map2obj(param, "rebateWxQrcode", RebateWxQrcode.class);
   		Page<RebateWxQrcode> page = new Page();
   		if (null != param.get("page")) {
   			page = JsonObjUtils.map2obj(param, "page", Page.class);
   		}  
   		Page<RebateWxQrcode> pageRebateWxQrcode = rebateWxQrcodeService.selectPage(page, 
				new EntityWrapper<>(rebateWxQrcode)); 
        return new BaseResult("1", "成功", pageRebateWxQrcode);
    }
    
    /**
     * 自定义查询
     */
    @RequestMapping(value="/customQuery/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult customQuery(@PathVariable("id") int id) {
		BaseResult baseResult = rebateWxQrcodeService.customQuery(id); 
        return baseResult;
    }
	
}
