package com.xgzx.admin.service;

import com.xgzx.admin.entity.RebateWithdraw;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2018-01-09
 * @email 755144556@qq.com
 */
public interface RebateWithdrawService extends IService<RebateWithdraw> {
	
	BaseResult customQuery(int id);
	
	BaseResult insertRebateWithdraw(Map<String, Object> param) throws Exception;
	
	BaseResult deleteRebateWithdraw(Map<String, Object> param) throws Exception;
	
	BaseResult selectRebateWithdrawList(Page<RebateWithdraw> page, 
			Map<String, Object> rebateWithdrawMap) throws Exception ;
	
	BaseResult pay(@RequestBody Map<String, Object> param)
			throws Exception;
}
