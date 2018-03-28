package com.xgzx.admin.mapper;

import com.xgzx.admin.entity.CommonParam;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * @author DaiDong
 * @since 2018-01-14
 * @email 755144556@qq.com
 */
public interface CommonParamMapper extends BaseMapper<CommonParam> {

	CommonParam customQuery(int id);
	
}