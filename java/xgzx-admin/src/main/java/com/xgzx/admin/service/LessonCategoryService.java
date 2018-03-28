package com.xgzx.admin.service;

import com.xgzx.admin.entity.LessonCategory;
import com.baomidou.mybatisplus.service.IService;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2018-01-09
 * @email 755144556@qq.com
 */
public interface LessonCategoryService extends IService<LessonCategory> {
	
	BaseResult customQuery(int id);
}
