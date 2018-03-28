package com.xgzx.admin.service;

import com.xgzx.admin.entity.Rebate;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2018-01-09
 * @email 755144556@qq.com
 */
public interface RebateService extends IService<Rebate> {
	
	BaseResult customQuery(int id);
	
	BaseResult getRebateInfo(Map<String, Object> param);
}
