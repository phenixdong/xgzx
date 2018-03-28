package com.xgzx.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xgzx.admin.entity.RebateWxQrcode;
import com.xgzx.admin.entity.User;
import com.xgzx.admin.mapper.RebateWxQrcodeMapper;
import com.xgzx.admin.mapper.UserMapper;
import com.xgzx.admin.service.RebateWxQrcodeService;
import com.xgzx.base.BaseResult;
import com.xgzx.util.JsonObjUtils;

/**
 * @author DaiDong
 * @since 2018-01-25
 * @email 755144556@qq.com
 */
@Service
@Transactional
public class RebateWxQrcodeServiceImpl extends ServiceImpl<RebateWxQrcodeMapper, RebateWxQrcode> implements RebateWxQrcodeService {
	
	@Autowired
	RebateWxQrcodeMapper rebateWxQrcodeMapper;
	@Autowired
	HttpServletRequest request;
	@Autowired
	UserMapper userMapper;
	
	@Override
	public BaseResult customQuery(int id) {
		RebateWxQrcode rebateWxQrcode = rebateWxQrcodeMapper.customQuery(id);
		return new BaseResult("1", "成功", rebateWxQrcode);
	}
	
	@Override
	public BaseResult insert(Map<String, Object> param) throws Exception { 
		// 验证码
		String rebateCheckCode = (String) param.get("rebateCheckCode");
		if (!rebateCheckCode.equals(request.getSession().getAttribute("rebateCheckCode"))) {
			return new BaseResult("0", "验证码错误", null);
		}
		
		// token
		String token = (String) param.get("token");
		String sessionToken = (String) request.getSession().getAttribute("token");
		if (!token.equals(sessionToken)) {
			return new BaseResult("0", "参数错误", null);
		}
		
		String userId = (String) param.get("userId");
		User user = new User();
		user.setUserId(userId);
		user.setValidTag(1);
		user = userMapper.selectOne(user);
		if (null == user) {
			return new BaseResult("0", "参数2错误", null);
		}
		
		RebateWxQrcode rebateWxQrcode = JsonObjUtils.map2obj(param, RebateWxQrcode.class);
		// 删除userId对应的url,逻辑删除
		Map<String, Object> map = new HashMap<>();
		map.put("user_id", rebateWxQrcode.getUserId());
		List<RebateWxQrcode> list = rebateWxQrcodeMapper.selectByMap(map);
		if (null != list && list.size() > 0) {
			for (RebateWxQrcode item : list) {
				item.setValidTag(0);
				int ret = rebateWxQrcodeMapper.updateById(item);
				if (ret <= 0) {
					System.out.println("删除收款二维码失败");
				}
			}
		}
			
		// 新增
		RebateWxQrcode newRebateWxQrcode = new RebateWxQrcode();
		newRebateWxQrcode.setUserId(rebateWxQrcode.getUserId());
		newRebateWxQrcode.setQrcodeUrl(rebateWxQrcode.getQrcodeUrl());
		newRebateWxQrcode.setValidTag(1);
		int ret = rebateWxQrcodeMapper.insert(newRebateWxQrcode); 
        if (ret <= 0) {
        	return new BaseResult("0", "新增收款二维码失败", null);
      	} else {
      		return new BaseResult("1", "成功", newRebateWxQrcode);
      	}
	}
}
