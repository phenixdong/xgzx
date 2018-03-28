package com.xgzx.admin.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.xgzx.admin.entity.Video;
import com.xgzx.admin.service.VideoService;
import com.xgzx.base.BaseResult;
import com.xgzx.util.JsonObjUtils; 

/**
 * @author DaiDong
 * @since 2017-12-24
 * @email 755144556@qq.com
 */
@RestController
@RequestMapping("/video")
public class VideoController {
	
	@Autowired
	VideoService videoService;
	
	/**
     * 新增
     */
	@RequestMapping(value="/insert", method={RequestMethod.POST})
   	public BaseResult insert(@RequestBody Map<String, Object> param) throws Exception {
		Video video = JsonObjUtils.map2obj(param, Video.class);
		boolean success = videoService.insert(video); 
        if (success) {
        	return new BaseResult("1", "成功", video);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }

	/**
     * 删除
     */
	@RequestMapping(value="/delete/{id}", method={RequestMethod.GET, RequestMethod.POST})
   	public BaseResult delete(@PathVariable("id") String id) {
		boolean success = videoService.deleteById(Long.valueOf(id)); 
        if (success) {
        	return new BaseResult("1", "成功", null);
      	} else {
      		return new BaseResult("0", "失败", null);
      	}
    }
    
    /**
     * 批量删除
     */
	@RequestMapping(value="/deleteBatch", method={RequestMethod.POST})
   	public BaseResult deleteBatch(@RequestBody Map<String, Object> param) throws Exception {
		return videoService.delete(param);
    }

	/**
     * 修改
     */
	@RequestMapping(value="/update", method={RequestMethod.POST})
   	public BaseResult update(@RequestBody Map<String, Object> param) throws Exception {
		return videoService.update(param);
    }
    
    /**
     * 分页查询
     */
	@RequestMapping(value="/selectPage", method={RequestMethod.POST})
   	public BaseResult selectPage(@RequestBody Map<String, Object> param) throws Exception {
   		Page<Video> page = JsonObjUtils.map2obj(param, "page", Page.class);
   		Map<String, Object> videoMap = JsonObjUtils.map2obj(param, "video", Map.class);
   		if (page == null) {
			page = new Page<Video>(1, 10);
		}  
   		List<String> timeRangeList = (List<String>) param.get("createTimeRange");
		if (null != timeRangeList && timeRangeList.size() > 0) {
			videoMap.put("startTime", timeRangeList.get(0));
			videoMap.put("endTime", timeRangeList.get(1));
		}
		return videoService.selectVideoList(page, videoMap);
    }
	
	/**
	 * 视频上传凭证
	 */
	@RequestMapping(value="/getVideoUploadAuth", method={RequestMethod.POST})
   	public BaseResult getVideoUploadAuth(@RequestBody Map<String, Object> param) throws Exception {
	    return videoService.getUploadAuth(param);
	}
	
	/**
	 * 图片上传凭证（不需要传参数）
	 */
	@RequestMapping(value="/getImageUploadAuth", method={RequestMethod.POST})
   	public BaseResult getImageUploadAuth(@RequestBody Map<String, Object> param) throws Exception {
	    return videoService.getImageUploadAuth(param);
	}
    
    /**
     * 视频分类
     */
    @RequestMapping(value="/getCategory", method={RequestMethod.GET})
   	public BaseResult getCategory() {
    	return videoService.getCategory();
    }
    
    /**
     * 视频详情
     */
    @RequestMapping(value="/selectById/{videoId}", method={RequestMethod.GET})
   	public BaseResult selectById(@PathVariable String videoId) {
    	return videoService.getVideoInfo(videoId);
    }
    
    /**
     * 获取播放地址
     */
    @RequestMapping(value="/getPlayInfo", method={RequestMethod.POST})
   	public BaseResult getPlayInfo(@RequestBody Map<String, Object> param) throws Exception {
    	return videoService.getPlayInfo(param);
    }
     
}
