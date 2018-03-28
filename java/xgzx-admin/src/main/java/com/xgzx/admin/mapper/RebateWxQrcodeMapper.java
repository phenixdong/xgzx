package com.xgzx.admin.mapper;

import com.xgzx.admin.entity.RebateWxQrcode;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * @author DaiDong
 * @since 2018-01-25
 * @email 755144556@qq.com
 */
public interface RebateWxQrcodeMapper extends BaseMapper<RebateWxQrcode> {

	RebateWxQrcode customQuery(int id);
	
}