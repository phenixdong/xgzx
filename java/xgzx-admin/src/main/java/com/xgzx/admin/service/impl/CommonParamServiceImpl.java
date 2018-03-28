package com.xgzx.admin.service.impl;

import com.xgzx.admin.entity.CommonParam;
import com.xgzx.admin.mapper.CommonParamMapper;
import com.xgzx.admin.service.CommonParamService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.xgzx.base.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DaiDong
 * @since 2018-01-14
 * @email 755144556@qq.com
 */
@Service
@Transactional
public class CommonParamServiceImpl extends ServiceImpl<CommonParamMapper, CommonParam> implements CommonParamService {
	
	@Autowired
	CommonParamMapper commonParamMapper;
	
	@Override
	public BaseResult customQuery(int id) {
		CommonParam commonParam = commonParamMapper.customQuery(id);
		return new BaseResult("1", "成功", commonParam);
	}
}
