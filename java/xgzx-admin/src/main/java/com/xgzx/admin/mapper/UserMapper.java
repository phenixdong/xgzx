package com.xgzx.admin.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.xgzx.admin.entity.User;

/**
 * @author DaiDong
 * @since 2017-12-24
 * @email 755144556@qq.com
 */
public interface UserMapper extends BaseMapper<User> {

	User customQuery(int id);
	
	List<User> selectUserList(Pagination page, Map<String, Object> userMap);
	
	User getUserInfo(Map<String, Object> map);

	List<Map<String, Object>> testGetMasterList();
		
}