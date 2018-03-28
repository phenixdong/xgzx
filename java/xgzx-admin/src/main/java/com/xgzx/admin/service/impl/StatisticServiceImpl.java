package com.xgzx.admin.service.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xgzx.admin.mapper.StatisticMapper;
import com.xgzx.admin.service.StatisticService;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2017-12-24
 * @email 755144556@qq.com
 */
@Service
@Transactional
public class StatisticServiceImpl implements StatisticService {
	
	@Autowired 
	HttpServletRequest request;
	@Autowired
	StatisticMapper statisticMapper;
	 

	@Override
	public BaseResult select(Map<String, Object> userMap) {
		Map<String, Object> resultMap = statisticMapper.select(userMap); 
		return new BaseResult(resultMap);
	}
 
 
}
