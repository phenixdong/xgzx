package com.xgzx.admin.mapper;


import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.xgzx.admin.entity.BulkBuy;
import com.baomidou.mybatisplus.mapper.BaseMapper;


/**
 * @author DaiDong
 * @since 2018-03-04
 * @email 755144556@qq.com
 */
public interface BulkBuyMapper extends BaseMapper<BulkBuy> {

	int insertReturnId(BulkBuy bulkBuy);

	List<BulkBuy> selectList(Pagination page, Map<String, Object> map);
	
}