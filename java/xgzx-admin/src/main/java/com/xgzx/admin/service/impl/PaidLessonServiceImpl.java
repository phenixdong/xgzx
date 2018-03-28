package com.xgzx.admin.service.impl;

import com.xgzx.admin.entity.PaidLesson;
import com.xgzx.admin.mapper.PaidLessonMapper;
import com.xgzx.admin.service.PaidLessonService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.xgzx.base.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DaiDong
 * @since 2018-01-29
 * @email 755144556@qq.com
 */
@Service
@Transactional
public class PaidLessonServiceImpl extends ServiceImpl<PaidLessonMapper, PaidLesson> implements PaidLessonService {
	
	@Autowired
	PaidLessonMapper paidLessonMapper;
	
	@Override
	public BaseResult customQuery(int id) {
		PaidLesson paidLesson = paidLessonMapper.customQuery(id);
		return new BaseResult("1", "成功", paidLesson);
	}
}
