package com.xgzx.admin.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xgzx.admin.entity.CommonParam;
import com.xgzx.admin.service.CommonParamService;
import com.xgzx.base.BaseResult;
import com.xgzx.util.DateUtil;

/**
 * @author DaiDong
 * @since 2018-01-05
 * @email 755144556@qq.com
 */
@RestController
@RequestMapping("/file")
public class FileController {

	@Value("${uploadPath}")
	String uploadPath;
	@Autowired
	CommonParamService commonParamService;
	
	/**
     * 文件上传存服务器
     */
    @RequestMapping(value="/upload/{subPath}", method={RequestMethod.POST})
	public BaseResult uploadAssetFile(MultipartFile file, 
			HttpServletRequest req, @PathVariable("subPath") String subPath)
			throws Exception {
    	//服务器存放文件路径
//    	String projectPath = req.getServletContext().getRealPath("/");
//    	projectPath = projectPath.replace("\\", "/");
//    	projectPath = projectPath.replace("static/", "");//savedUrl已经有static文件夹路径，这里重复了
    	
    	//原文件路径
    	String fileName = file.getOriginalFilename();
    	String suffix = fileName.substring(fileName.lastIndexOf("."));
    	
    	//新文件存放路径及文件名
    	Date now = new Date();
		//String newFilePath = uploadPath + "static/uploadFile/";
    	String newFilePath = uploadPath;
    	if (null != subPath && !"".equals(uploadPath)) {
    		newFilePath = newFilePath + subPath + "/";
    	}
    	String newFileName = getRandomString(10) + suffix;
		String newFilePathAndName = newFilePath + newFileName;  
		//新建存放文件夹
		File f = new File(newFilePath);
		if (!f.exists()) {
			f.mkdirs();
		}
		//存文件
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(newFilePathAndName)));  
		out.write(file.getBytes());  
		out.flush();  
		out.close();  
		//返回文件url
		return new BaseResult("1", "成功", newFileName);
	}
    
   
    /**
     * 广告图片
     */
    @RequestMapping(value="/uploadAdvertiseFile", method={RequestMethod.POST})
	public BaseResult uploadAdvertiseFile(MultipartFile file, HttpServletRequest req)
			throws Exception {
    	BaseResult baseResult = uploadAssetFile(file, req, "common");
    	if ("1".equals(baseResult.getReturnCode())) {
    		String url = (String) baseResult.getData();
    		// 查询广告图片 
    		CommonParam commonParam = new CommonParam();
    		commonParam.setParamName("advertiseImageUrl");
    		EntityWrapper wrapper = new EntityWrapper<>(commonParam);
    		commonParam = commonParamService.selectOne(wrapper);
    		
    		// 更新
    		commonParam.setParamValue(url);
    		commonParamService.updateById(commonParam);
    		return new BaseResult(url);
    	} else {
    		return new BaseResult("0", "失败", null);
    	}
    }
    
    /**
     * 联系我们图片
     */
    @RequestMapping(value="/uploadContactUsFile", method={RequestMethod.POST})
	public BaseResult uploadContactUsFile(MultipartFile file, HttpServletRequest req)
			throws Exception {
    	BaseResult baseResult = uploadAssetFile(file, req, "common");
    	if ("1".equals(baseResult.getReturnCode())) {
    		String url = (String) baseResult.getData();
    		// 查询图片 
    		CommonParam commonParam = new CommonParam();
    		commonParam.setParamName("contactUsImageUrl");
    		EntityWrapper wrapper = new EntityWrapper<>(commonParam);
    		commonParam = commonParamService.selectOne(wrapper);
    		
    		// 更新
    		commonParam.setParamValue(url);
    		commonParamService.updateById(commonParam);
    		return new BaseResult(url);
    	} else {
    		return new BaseResult("0", "失败", null);
    	}
    }
    
    /**
     * 分享的默认图片
     */
    @RequestMapping(value="/uploadShareIconFile", method={RequestMethod.POST})
	public BaseResult uploadShareIconFile(MultipartFile file, HttpServletRequest req)
			throws Exception {
    	//原文件路径
    	String fileName = file.getOriginalFilename();
    	//新文件存放路径及文件名
    	Date now = new Date();
    	String newFilePath = uploadPath + "common/";
    	String newFileName = "shareIcon.jpg";
		String newFilePathAndName = newFilePath + newFileName;  
		//新建存放文件夹
		File f = new File(newFilePath);
		if (!f.exists()) {
			f.mkdirs();
		}
		//存文件
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(newFilePathAndName)));  
		out.write(file.getBytes());  
		out.flush();  
		out.close();  
		//返回文件url
		return new BaseResult("1", "成功", newFileName);
    }
    
    /**
     * 随机字符串
     */
    private static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";   
        Random random = new Random();   
        StringBuffer sb = new StringBuffer();   
        for (int i = 0; i < length; i++) {   
            int number = random.nextInt(base.length());   
            sb.append(base.charAt(number));   
        }   
        return sb.toString();   
     }   

}
