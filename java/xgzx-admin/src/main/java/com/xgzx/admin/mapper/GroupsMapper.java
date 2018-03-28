package com.xgzx.admin.mapper;

import com.xgzx.admin.entity.Groups;
import com.xgzx.admin.entity.Lesson;
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
public interface GroupsMapper extends BaseMapper<Groups> {

	Groups customQuery(int id);
	
	List<Groups> selectGroupsList(Pagination page, Map<String, Object> groupsMap);
	
	List<Groups> selectBulkBuyGroupsList(Pagination page, Map<String, Object> groupsMap);
	
	List<Groups> getBelongedGroups(Map<String, Object> groupsMap);

	int insertReturnId(Groups Groups);
	
}