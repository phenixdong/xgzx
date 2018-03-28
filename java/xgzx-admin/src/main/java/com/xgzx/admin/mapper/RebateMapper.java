package com.xgzx.admin.mapper;

import com.xgzx.admin.entity.Rebate;

import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * @author DaiDong
 * @since 2018-01-09
 * @email 755144556@qq.com
 */
public interface RebateMapper extends BaseMapper<Rebate> {

	Rebate customQuery(int id);
	
	
}