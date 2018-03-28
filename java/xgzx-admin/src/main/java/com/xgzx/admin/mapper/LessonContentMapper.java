package com.xgzx.admin.mapper;

import com.xgzx.admin.entity.LessonContent;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * @author DaiDong
 * @since 2018-01-09
 * @email 755144556@qq.com
 */
public interface LessonContentMapper extends BaseMapper<LessonContent> {

	LessonContent customQuery(int id);
	
}