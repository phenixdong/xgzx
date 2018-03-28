package com.xgzx.admin.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.CreateUploadImageRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadImageResponse;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoResponse;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xgzx.admin.base.Constants;
import com.xgzx.admin.entity.User;
import com.xgzx.admin.entity.Video;
import com.xgzx.admin.mapper.VideoMapper;
import com.xgzx.admin.service.UserService;
import com.xgzx.admin.service.VideoService;
import com.xgzx.admin.util.AliyunUtil;
import com.xgzx.admin.util.StringUtil;
import com.xgzx.base.BaseErrResult;
import com.xgzx.base.BaseResult;
import com.xgzx.exception.CommonError;
import com.xgzx.util.JsonObjUtils;
import com.xgzx.util.httpclient.HttpUtil;

/**
 * @author DaiDong
 * @since 2018-01-05
 * @email 755144556@qq.com
 */
@Service
@Transactional
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {
	
	DefaultAcsClient aliyunClient;
	@Autowired
	UserService userService;
	@Autowired
	VideoMapper videoMapper;
	
	@Override
	public BaseResult getVideoInfo(String videoId) {
		Map<String, String> privateParams = new HashMap<>();
		privateParams.put("Action", "GetVideoInfo");
        privateParams.put("VideoId", videoId);
        Map<String, String> publicParams = AliyunUtil.generatePublicParamters();
        String URL = AliyunUtil.generateOpenAPIURL(publicParams, privateParams);
        String resp = "";
        HttpUtil httpUtil = new HttpUtil();
        resp = httpUtil.request(URL);
        System.out.println("getVideoInfo resp " + resp);
        return parseResp(resp);
	}
	
	@Override
	public BaseResult getPlayInfo(Map<String, Object> param) {
		String videoId = (String) param.get("videoId");
    	String userId = (String) param.get("userId");
    	Integer lessonId = (Integer) param.get("lessonId");
    	Integer chargeType = (Integer) param.get("chargeType");
    	// 是否收费，1免费，2会员免费，3单独收费
    	if (1 != chargeType) {
        	if (!userService.isPaid(userId, lessonId)) {
        		// 仍然返回成功，不算错误
        		return new BaseResult("1", "非会员", null);
        	}
    	}
    	
		Map<String, String> privateParams = new HashMap<>();
		privateParams.put("Action", "GetPlayInfo");
        privateParams.put("VideoId", videoId);
        Map<String, String> publicParams = AliyunUtil.generatePublicParamters();
        String URL = AliyunUtil.generateOpenAPIURL(publicParams, privateParams);
        String resp = "";
        HttpUtil httpUtil = new HttpUtil();
        resp = httpUtil.request(URL);
        System.out.println("getPlayInfo resp " + resp);
        return parseResp(resp);
	}
	
	@Override
	public BaseResult getUploadAuth(Map<String, Object> param){
		aliyunClient = new DefaultAcsClient(
	    		DefaultProfile.getProfile("cn-shanghai", AliyunUtil.accessKeyId,
	    				AliyunUtil.accessKeySecret));
	    BaseResult result = createUploadVideo(aliyunClient, param);
	    if ("1".equals(result.returnCode)) {
	    	CreateUploadVideoResponse resp = (CreateUploadVideoResponse) result.getData();
		    // 存本地数据库
	    	Video video = new Video();
	    	video.setCategoryId(Constants.CATEGORY_ID);
		    video.setTitle((String) param.get("title"));
		    video.setDescription((String) param.get("description"));
		    video.setVideoId(resp.getVideoId());
		    video.setUploadAuth(resp.getUploadAuth());
		    video.setUploadAddress(resp.getUploadAddress());
		    video.setStatus("Uploading");
		    video.setValidTag(1);
		    int ret = videoMapper.insert(video);
		    if (ret <= 0) {
		    	return new BaseResult("0", "新增记录失败", null);
		    }
		    
			return new BaseResult("1", "成功", video);
	    } else {
	    	return new BaseResult("0", result.returnMsg, null);
	    }
	}
	
	/**
     * 获取视频上传凭证和地址
     */
    private static BaseResult createUploadVideo(DefaultAcsClient client, 
    		Map<String, Object> param) {
        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        CreateUploadVideoResponse response = null;
        try {
        	/*必选，视频源文件名称（必须带后缀, 支持 ".3gp", ".asf", ".avi", ".dat", 
	           * ".dv", ".flv", ".f4v", ".gif", ".m2t", ".m3u8", ".m4v", ".mj2",
	           * ".mjpeg", ".mkv", ".mov", ".mp4", ".mpe", ".mpg", ".mpeg", 
	           * ".mts", ".ogg", ".qt", ".rm", ".rmvb", ".swf", ".ts", ".vob", 
	           * ".wmv", ".webm"".aac", ".ac3", ".acm", ".amr", ".ape", ".caf", 
	           * ".flac", ".m4a", ".mp3", ".ra", ".wav", ".wma"）*/
        	// 文件名要有后缀，并且后缀是上面中的一种，否则报错：
        	//“The specified parameter FileName is not valid.”
        	request.setFileName((String) param.get("fileName"));
        	//必选，视频标题
        	request.setTitle((String) param.get("title"));
        	//可选，分类ID
        	request.setCateId(Constants.CATEGORY_ID);
        	//可选，视频标签，多个用逗号分隔
        	//request.setTags("标签1,标签2");
        	//可选，视频描述
        	request.setDescription((String) param.get("description"));
          	response = client.getAcsResponse(request);
        } catch (ServerException e) {
        	System.out.println("CreateUploadVideoRequest Server Exception:");
        	e.printStackTrace();
        	return new BaseResult("0", e.getMessage(), null);
        } catch (ClientException e) {
        	System.out.println("CreateUploadVideoRequest Client Exception:");
        	e.printStackTrace();
        	return new BaseResult("0", e.getMessage(), null);
        }
        System.out.println("VideoId:"+response.getVideoId());
        System.out.println("RequestId:"+response.getRequestId());
        System.out.println("UploadAuth:"+response.getUploadAuth());
        System.out.println("UploadAddress:"+response.getUploadAddress());
        return new BaseResult("1", "成功", response);
	}
     
    
    /**
     * 刷新视频上传凭证
     */
    private static void refreshUploadVideo(DefaultAcsClient client, String videoId) {
        RefreshUploadVideoRequest request = new RefreshUploadVideoRequest();
        RefreshUploadVideoResponse response = null;
        try {
              request.setVideoId(videoId);
              response = client.getAcsResponse(request);
        } catch (ServerException e) {
              System.out.println("RefreshUploadVideoRequest Server Exception:");
              e.printStackTrace();
              return;
        } catch (ClientException e) {
              System.out.println("RefreshUploadVideoRequest Client Exception:");
              e.printStackTrace();
              return;
        }
        System.out.println("RequestId:" + response.getRequestId());
        System.out.println("UploadAuth:" + response.getUploadAuth());
    }
    
    /**
     * 获取图片上传凭证和地址
     */
    private static BaseResult createUploadImage(DefaultAcsClient client) {
        CreateUploadImageRequest request = new CreateUploadImageRequest();
        CreateUploadImageResponse response = null;
        try {
        	// 文件名要有后缀，并且后缀是允许格式中的一种，否则报错：
        	//“The specified parameter FileName is not valid.”
        	request.setImageType("cover");
          	response = client.getAcsResponse(request);
        } catch (ServerException e) {
        	System.out.println("CreateUploadVideoRequest Server Exception:");
        	e.printStackTrace();
        	return new BaseResult("0", e.getMessage(), null);
        } catch (ClientException e) {
        	System.out.println("CreateUploadVideoRequest Client Exception:");
        	e.printStackTrace();
        	return new BaseResult("0", e.getMessage(), null);
        }
        System.out.println("ImageURL:"+response.getImageURL());
        System.out.println("RequestId:"+response.getRequestId());
        System.out.println("UploadAuth:"+response.getUploadAuth());
        System.out.println("UploadAddress:"+response.getUploadAddress());
        return new BaseResult("1", "成功", response);
	}
    
	@Override
	public BaseResult delete(Map<String, Object> param) throws Exception {
		List<String> videoIds =  JsonObjUtils.map2List(param, "ids", String.class);
    	String ids = StringUtil.listToStr(videoIds);
    	Map<String, String> privateParams = new HashMap<>();
        privateParams.put("Action", "DeleteVideo");
        privateParams.put("VideoIds", ids);
        Map<String, String> publicParams = AliyunUtil.generatePublicParamters();
        String URL = AliyunUtil.generateOpenAPIURL(publicParams, privateParams);
        String resp = "";
        HttpUtil httpUtil = new HttpUtil();
        resp = httpUtil.request(URL);
        System.out.println("getVideoInfo resp " + resp);
        BaseResult result = parseResp(resp);
        if ("0".equals(result.getReturnCode())) {
        	// 不能返回，如果aliyun上视频没上传成功，会删除失败，直接返回，导致本地不能删除了
        	System.out.println("删除失败 " + result.getReturnMsg());
        }
        // 删除本地数据
        for (String id : videoIds) {
   			Video video = videoMapper.selectById(id);
   			video.setValidTag(0);
			int updateRet = videoMapper.updateById(video);
			if (updateRet <= 0) {
				CommonError.CommonErr(new BaseErrResult("-1", "本地删除失败"));
			}
		}
        return new BaseResult(); 
	}

	@Override
	public BaseResult update(Map<String, Object> param) throws Exception {
		Map<String, String> privateParams = new HashMap<>();
        privateParams.put("Action", "UpdateVideoInfo");
        privateParams.put("VideoId", (String) param.get("VideoId"));
        privateParams.put("Title", (String) param.get("Title"));
        privateParams.put("CoverURL", (String) param.get("CoverURL"));
        Map<String, String> publicParams = AliyunUtil.generatePublicParamters();
        String URL = AliyunUtil.generateOpenAPIURL(publicParams, privateParams);
        String resp = "";
        HttpUtil httpUtil = new HttpUtil();
        resp = httpUtil.request(URL);
        System.out.println("updateVideo resp " + resp);
        BaseResult result = parseResp(resp);
        if ("0".equals(result.getReturnCode())) {
        	// 失败，返回
        	return result;
        }
        
        // 更新本地数据库
        Video video = new Video();
        video.setVideoId((String) param.get("VideoId"));
        video.setTitle((String) param.get("Title"));
        video.setCoverUrl((String) param.get("CoverURL"));
        int ret = videoMapper.updateById(video);
        if (ret <= 0) {
        	return new BaseResult("0", "本地更新失败", null);
        }
        return new BaseResult();
	}

	@Override
	public BaseResult getImageUploadAuth(Map<String, Object> param) throws Exception {
		aliyunClient = new DefaultAcsClient(
	    		DefaultProfile.getProfile("cn-shanghai", AliyunUtil.accessKeyId,
	    				AliyunUtil.accessKeySecret));
	    BaseResult result = createUploadImage(aliyunClient);
	    if ("1".equals(result.returnCode)) {
	    	CreateUploadImageResponse resp = (CreateUploadImageResponse) result.getData();
		    Map<String, String> map = new HashMap<>();
		    map.put("imageURL", resp.getImageURL());
		    map.put("requestId", resp.getRequestId());
		    map.put("uploadAuth", resp.getUploadAuth());
		    map.put("uploadAddress", resp.getUploadAddress());
			return new BaseResult("1", "成功", map);
	    } else {
	    	return new BaseResult("0", result.returnMsg, null);
	    }
	}

	@Override
	public BaseResult getCategory() {
		Map<String, String> privateParams = new HashMap<>();
        privateParams.put("Action", "GetCategories");
        Map<String, String> publicParams = AliyunUtil.generatePublicParamters();
        String URL = AliyunUtil.generateOpenAPIURL(publicParams, privateParams);
        String resp = "";
        HttpUtil httpUtil = new HttpUtil();
        resp = httpUtil.request(URL);
        System.out.println("getVideoCategories resp " + resp);
        return parseResp(resp);
	}
	
	/**
	 * 阿里接口，查询视频列表
	 */
	private BaseResult getVideoList(@RequestBody Map<String, Object> param) {
    	Map<String, String> privateParams = new HashMap<>();
        privateParams.put("Action", "GetVideoList");
        privateParams.put("PageSize", (Integer) param.get("pageSize") + "");
        if (null == param.get("pageNo")) {
        	privateParams.put("PageNo", Constants.VIDEO_PAGE_SIZE + "");
        } else {
        	privateParams.put("PageNo", (Integer) param.get("pageNo") + "");
        } 
        if (null != param.get("sortBy")) {
        	privateParams.put("SortBy", (String) param.get("sortBy"));
        }
        Map<String, String> publicParams = AliyunUtil.generatePublicParamters();
        String URL = AliyunUtil.generateOpenAPIURL(publicParams, privateParams);
        String resp = "";
        HttpUtil httpUtil = new HttpUtil();
        resp = httpUtil.request(URL);
        
        System.out.println("getVideoList resp " + resp);
        return parseResp(resp);
	}

	@Override
	public BaseResult selectVideoList(Page<Video> page, 
			Map<String, Object> videoMap) throws Exception {
		videoMap.put("categoryId", Constants.CATEGORY_ID);
		List<Video> list = videoMapper.selectVideoList(page, videoMap); 
		// 查询状态为”上传中“的视频最新状态，更新到本地
		if (null != list && list.size() > 0) {
			for (Video item : list) {
				if ("Uploading".equals(item.getStatus())
					|| "UploadFail".equals(item.getStatus())
					|| "Transcoding".equals(item.getStatus())
					|| "TranscodeFail".equals(item.getStatus())) {
					// 查询视频详情
					BaseResult result = getVideoInfo(item.getVideoId());
					if ("1".equals(result.getReturnCode())) {
						String resp = (String) result.getData();
						Map<String, Object> map = JsonObjUtils.json2map(resp);
						Map<String, Object> respVideoMap = (Map<String, Object>)
								map.get("Video");
						item.setStatus((String) respVideoMap.get("Status"));
						item.setTitle((String) respVideoMap.get("Title"));
						item.setDescription((String) respVideoMap.get("Description"));
						item.setCoverUrl((String) respVideoMap.get("CoverURL"));
						BigDecimal duration = (BigDecimal) respVideoMap.get("Duration");
						item.setDuration(duration.intValue());
						item.setSize((Integer) respVideoMap.get("Size"));
						item.setCategoryId((Integer) respVideoMap.get("CateId"));
						item.setCategoryName((String) respVideoMap.get("CateName"));
						int ret = videoMapper.updateById(item);
						if (ret <= 0) {
							return new BaseResult("0", "查询结果更新失败", null);
						}
					} else {
						System.out.println("视频查询失败 " + item.getVideoId());
						continue;
					}
				} else {
					continue;
				}
			}
		}
		page.setRecords(list);
		return new BaseResult(page);
	}

	BaseResult parseResp(String resp) {
    	Map<String, Object> map = null;
        try {
			map = JsonObjUtils.json2map(resp);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
        String code = (String) map.get("Code");
        String message = (String) map.get("Message");
        if (null != code && !"".equals(code)) {
        	return new BaseResult("0", message, null);
        }
    	return new BaseResult("1", "成功", resp);
    }
	
}
