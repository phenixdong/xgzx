package com.xgzx.admin.service.impl;

import com.xgzx.admin.entity.LessonCategory;
import com.xgzx.admin.mapper.LessonCategoryMapper;
import com.xgzx.admin.service.LessonCategoryService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.xgzx.base.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DaiDong
 * @since 2018-01-09
 * @email 755144556@qq.com
 */
@Service
@Transactional
public class LessonCategoryServiceImpl extends ServiceImpl<LessonCategoryMapper, LessonCategory> implements LessonCategoryService {
	
	@Autowired
	LessonCategoryMapper lessonCategoryMapper;
	
	@Override
	public BaseResult customQuery(int id) {
		LessonCategory lessonCategory = lessonCategoryMapper.customQuery(id);
		return new BaseResult("1", "成功", lessonCategory);
	}
}
