package com.xgzx.admin.service;

import com.xgzx.admin.entity.Teacher;

import java.text.ParseException;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2018-01-05
 * @email 755144556@qq.com
 */
public interface TeacherService extends IService<Teacher> {
	
	BaseResult customQuery(int id);
	
	BaseResult statisticRebate(Map<String, Object> param) throws Exception;
}
