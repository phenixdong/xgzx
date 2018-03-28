package com.xgzx.admin.service;

import com.xgzx.admin.entity.LessonWatch;
import com.baomidou.mybatisplus.service.IService;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2018-01-12
 * @email 755144556@qq.com
 */
public interface LessonWatchService extends IService<LessonWatch> {
	
	BaseResult customQuery(int id);
}
