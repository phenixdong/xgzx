package com.xgzx.admin.mapper;

import com.xgzx.admin.entity.RebateTransaction;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * @author DaiDong
 * @since 2018-01-09
 * @email 755144556@qq.com
 */
public interface RebateTransactionMapper extends BaseMapper<RebateTransaction> {

	RebateTransaction customQuery(int id);
	
	List<RebateTransaction> selectRebateTransactionList(Pagination page, 
			Map<String, Object> transactionMap);
	
}