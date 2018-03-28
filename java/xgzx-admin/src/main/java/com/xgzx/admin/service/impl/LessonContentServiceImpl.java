package com.xgzx.admin.service.impl;

import com.xgzx.admin.entity.LessonContent;
import com.xgzx.admin.mapper.LessonContentMapper;
import com.xgzx.admin.service.LessonContentService;
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
public class LessonContentServiceImpl extends ServiceImpl<LessonContentMapper, LessonContent> implements LessonContentService {
	
	@Autowired
	LessonContentMapper lessonContentMapper;
	
	@Override
	public BaseResult customQuery(int id) {
		LessonContent lessonContent = lessonContentMapper.customQuery(id);
		return new BaseResult("1", "成功", lessonContent);
	}
}
