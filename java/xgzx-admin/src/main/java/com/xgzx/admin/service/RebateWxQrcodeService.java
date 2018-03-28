package com.xgzx.admin.service;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.xgzx.admin.entity.RebateWxQrcode;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2018-01-25
 * @email 755144556@qq.com
 */
public interface RebateWxQrcodeService extends IService<RebateWxQrcode> {
	
	BaseResult customQuery(int id);
	
	BaseResult insert(Map<String, Object> param) throws Exception ;
}
