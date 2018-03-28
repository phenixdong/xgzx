package com.xgzx.admin.service;

import com.xgzx.admin.entity.CommonParam;
import com.baomidou.mybatisplus.service.IService;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2018-01-14
 * @email 755144556@qq.com
 */
public interface CommonParamService extends IService<CommonParam> {
	
	BaseResult customQuery(int id);
}
