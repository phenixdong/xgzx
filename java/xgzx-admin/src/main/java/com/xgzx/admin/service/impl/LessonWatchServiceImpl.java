package com.xgzx.admin.service.impl;

import com.xgzx.admin.entity.LessonWatch;
import com.xgzx.admin.mapper.LessonWatchMapper;
import com.xgzx.admin.service.LessonWatchService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.xgzx.base.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DaiDong
 * @since 2018-01-12
 * @email 755144556@qq.com
 */
@Service
@Transactional
public class LessonWatchServiceImpl extends ServiceImpl<LessonWatchMapper, LessonWatch> implements LessonWatchService {
	
	@Autowired
	LessonWatchMapper lessonWatchMapper;
	
	@Override
	public BaseResult customQuery(int id) {
		LessonWatch lessonWatch = lessonWatchMapper.customQuery(id);
		return new BaseResult("1", "成功", lessonWatch);
	}
}
