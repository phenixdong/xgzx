package com.xgzx.admin.service.impl;

import com.xgzx.admin.entity.MemberType;
import com.xgzx.admin.mapper.MemberTypeMapper;
import com.xgzx.admin.service.MemberTypeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.xgzx.base.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DaiDong
 * @since 2018-01-10
 * @email 755144556@qq.com
 */
@Service
@Transactional
public class MemberTypeServiceImpl extends ServiceImpl<MemberTypeMapper, MemberType> implements MemberTypeService {
	
	@Autowired
	MemberTypeMapper memberTypeMapper;
	
	@Override
	public BaseResult customQuery(int id) {
		MemberType memberType = memberTypeMapper.customQuery(id);
		return new BaseResult("1", "成功", memberType);
	}
}
