package com.xgzx.admin.mapper;


import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.xgzx.admin.entity.BulkBuyRegister;
import com.baomidou.mybatisplus.mapper.BaseMapper;


/**
 * @author DaiDong
 * @since 2018-03-17
 * @email 755144556@qq.com
 */
public interface BulkBuyRegisterMapper extends BaseMapper<BulkBuyRegister> {

	int insertReturnId(BulkBuyRegister bulkBuyRegister);

	List<BulkBuyRegister> selectList(Pagination page, Map<String, Object> map);
	
	List<BulkBuyRegister> selectByTime(Map<String, Object> param);
	
	
}