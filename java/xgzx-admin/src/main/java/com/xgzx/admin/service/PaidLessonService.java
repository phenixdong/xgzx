package com.xgzx.admin.service;

import com.xgzx.admin.entity.PaidLesson;
import com.baomidou.mybatisplus.service.IService;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2018-01-29
 * @email 755144556@qq.com
 */
public interface PaidLessonService extends IService<PaidLesson> {
	
	BaseResult customQuery(int id);
}
