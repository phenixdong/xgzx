package com.xgzx.admin.mapper;

import com.xgzx.admin.entity.Teacher;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * @author DaiDong
 * @since 2018-01-05
 * @email 755144556@qq.com
 */
public interface TeacherMapper extends BaseMapper<Teacher> {

	Teacher customQuery(int id);
	
}