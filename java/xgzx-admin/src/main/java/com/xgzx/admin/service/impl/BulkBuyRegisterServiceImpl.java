package com.xgzx.admin.service.impl;

import com.xgzx.admin.entity.BulkBuyRegister;
import com.xgzx.admin.mapper.BulkBuyRegisterMapper;
import com.xgzx.admin.service.BulkBuyRegisterService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.xgzx.base.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.plugins.Page;
import java.util.List;
import java.util.Map;

/**
 * @author DaiDong
 * @since 2018-03-17
 * @email 755144556@qq.com
 */
@Service
@Transactional
public class BulkBuyRegisterServiceImpl extends ServiceImpl<BulkBuyRegisterMapper, BulkBuyRegister> implements BulkBuyRegisterService {
	
	@Autowired
	BulkBuyRegisterMapper bulkBuyRegisterMapper;
	
	@Override
	public int insertReturnId(BulkBuyRegister bulkBuyRegister) {
		return bulkBuyRegisterMapper.insertReturnId(bulkBuyRegister);
	}
	
	@Override
	public BaseResult selectList(Page<BulkBuyRegister> page, Map<String, Object> map) {
		List<BulkBuyRegister> list = bulkBuyRegisterMapper.selectList(page, map); 
		page.setRecords(list);
		return new BaseResult(page);
	}
	 
}
