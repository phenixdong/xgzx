package com.xgzx.admin.mapper;

import com.xgzx.admin.entity.MemberType;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * @author DaiDong
 * @since 2018-01-10
 * @email 755144556@qq.com
 */
public interface MemberTypeMapper extends BaseMapper<MemberType> {

	MemberType customQuery(int id);
	
}