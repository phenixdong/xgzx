package com.xgzx.admin.mapper;

import com.xgzx.admin.entity.User;
import com.xgzx.admin.entity.Video;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

/**
 * @author DaiDong
 * @since 2018-01-18
 * @email 755144556@qq.com
 */
public interface VideoMapper extends BaseMapper<Video> {

	Video customQuery(int id);
	
	List<Video> selectVideoList(Pagination page, Map<String, Object> videoMap);
	
}