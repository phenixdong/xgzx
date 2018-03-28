package com.xgzx.admin.service;

import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.xgzx.admin.entity.BulkBuyRegister;
import com.baomidou.mybatisplus.service.IService;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2018-03-17
 * @email 755144556@qq.com
 */
public interface BulkBuyRegisterService extends IService<BulkBuyRegister> {
	
	int insertReturnId(BulkBuyRegister bulkBuyRegister);
	
	BaseResult selectList(Page<BulkBuyRegister> page, Map<String, Object> map);

}
