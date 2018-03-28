package com.xgzx.admin.service;
import java.util.Map;
import com.baomidou.mybatisplus.plugins.Page;
import com.xgzx.admin.entity.BulkBuy;
import com.baomidou.mybatisplus.service.IService;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2018-03-04
 * @email 755144556@qq.com
 */
public interface BulkBuyService extends IService<BulkBuy> {
	
	int insertReturnId(BulkBuy bulkBuy);
	
	BaseResult register(Map<String, Object> param);
	
	BaseResult selectList(Page<BulkBuy> page, Map<String, Object> map);

}
