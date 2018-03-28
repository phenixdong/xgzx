package com.xgzx.admin.service;

import com.xgzx.admin.entity.MemberType;
import com.baomidou.mybatisplus.service.IService;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2018-01-10
 * @email 755144556@qq.com
 */
public interface MemberTypeService extends IService<MemberType> {
	
	BaseResult customQuery(int id);
}
