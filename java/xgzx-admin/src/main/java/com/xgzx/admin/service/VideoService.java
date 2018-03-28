package com.xgzx.admin.service;

import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.xgzx.admin.entity.User;
import com.xgzx.admin.entity.Video;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2018-01-05
 * @email 755144556@qq.com
 */
public interface VideoService extends IService<Video> {
	
	BaseResult getVideoInfo(String videoId);
	
	BaseResult getPlayInfo(Map<String, Object> param);
	
	BaseResult getCategory();
	
	BaseResult getUploadAuth(Map<String, Object> param) throws Exception;
	
	BaseResult getImageUploadAuth(Map<String, Object> param) throws Exception;
	
	BaseResult delete(Map<String, Object> param) throws Exception;
	
	BaseResult update(Map<String, Object> param) throws Exception;
	
	BaseResult selectVideoList(Page<Video> page, 
			Map<String, Object> videoMap) throws Exception;
	
	
	
}
