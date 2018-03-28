package com.xgzx.admin.mapper;

import com.xgzx.admin.entity.RebateWithdraw;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * @author DaiDong
 * @since 2018-01-09
 * @email 755144556@qq.com
 */
public interface RebateWithdrawMapper extends BaseMapper<RebateWithdraw> {

	RebateWithdraw customQuery(int id);
	
	List<RebateWithdraw> selectRebateWithdrawList(Pagination page, 
			Map<String, Object> rebateWithdrawMap);
	
}