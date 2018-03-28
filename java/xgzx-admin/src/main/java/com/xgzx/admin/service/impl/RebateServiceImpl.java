package com.xgzx.admin.service.impl;

import com.xgzx.admin.entity.Rebate;
import com.xgzx.admin.entity.RebateWxQrcode;
import com.xgzx.admin.mapper.RebateMapper;
import com.xgzx.admin.mapper.RebateWxQrcodeMapper;
import com.xgzx.admin.service.RebateService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.xgzx.base.BaseResult;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DaiDong
 * @since 2018-01-09
 * @email 755144556@qq.com
 */
@Service
@Transactional
public class RebateServiceImpl extends ServiceImpl<RebateMapper, Rebate> implements RebateService {
	
	@Autowired
	RebateMapper rebateMapper;
	@Autowired
	RebateWxQrcodeMapper rebateWxQrcodeMapper;
	
	@Override
	public BaseResult customQuery(int id) {
		Rebate rebate = rebateMapper.customQuery(id);
		return new BaseResult("1", "成功", rebate);
	}
	
	@Override
	public BaseResult getRebateInfo(Map<String, Object> param) {
		if (null == param.get("userId")) {
			return new BaseResult("0", "userId为空", null);
		}
		String userId = (String) param.get("userId");
		Rebate rebate = new Rebate();
		rebate.setUserId(userId);
		rebate.setValidTag(1);
		rebate = rebateMapper.selectOne(rebate);
		
		// 提现收款二维码
		RebateWxQrcode rebateWxQrcode = new RebateWxQrcode();
		rebateWxQrcode.setUserId(userId);
		rebateWxQrcode.setValidTag(1);
		rebateWxQrcode = rebateWxQrcodeMapper.selectOne(rebateWxQrcode);
		rebate.setRebateWxQrcode(rebateWxQrcode);		
		return new BaseResult(rebate);
	}
}
