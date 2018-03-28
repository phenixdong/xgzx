package com.xgzx.admin.config;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;

public class DbTimeHandler extends MetaObjectHandler {

	@Override
    public void insertFill(MetaObject metaObject) {
		Object createTime = getFieldValByName("createTime", metaObject); 
        //System.out.println("createTime=" + createTime);
        if (createTime == null) {
            setFieldValByName("createTime", new Date(), metaObject); 
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
    	setFieldValByName("updateTime", new Date(), metaObject); 
    }
}
