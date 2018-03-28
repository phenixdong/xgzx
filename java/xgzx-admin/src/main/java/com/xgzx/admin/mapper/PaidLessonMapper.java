package com.xgzx.admin.mapper;

import com.xgzx.admin.entity.PaidLesson;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * @author DaiDong
 * @since 2018-01-29
 * @email 755144556@qq.com
 */
public interface PaidLessonMapper extends BaseMapper<PaidLesson> {

	PaidLesson customQuery(int id);
	
}