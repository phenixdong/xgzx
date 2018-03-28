package com.xgzx.admin.controller;


import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xgzx.admin.service.WXService;
import com.xgzx.base.BaseResult;

/**
 * @author DaiDong
 * @since 2017-12-19
 * @email 755144556@qq.com
 * 
 * 配置说明：
 * 		IP白名单（获取openId）：公众平台-设置-安全中心-IP白名单
 * 		网页授权域名（获取用户信息）：公众平台-设置-公众号设置-功能设置-业务域名、JS接口安全域名
 * 		支付授权目录（支付允许的域名）：商户平台-产品中心-开发配置
 * 		支付通知Url：统一下单接口，参数notify_url
 * 切换公众号时，需替换部分：
 * 		公众号app_id，app_secret，在WXPubConfigImpl.java中
 * 		商户id，api_key，在WXPayConfigImpl.java中
 * 		商户密钥文件apiclient_cert和rootca.pem
 * 		登录重定向中appid，在前端的host.js中const redirectHomeUrl
 * 		公众号菜单，页面地址，其中appid的部分
 */
@RestController
@RequestMapping("/wx")
public class WXController {
	 
	@Autowired 
	HttpServletRequest request;
	@Autowired 
	HttpServletResponse response;
	@Autowired
	WXService wxService;  
	
	/**
	 * 获取openId和access_token
	 * 注意：
	 * 	access_token是网页授权的token，与支付中的access_token不同 
	 * 	secret是公众号的appSecret，不是商户的api密钥
	 */
	@RequestMapping(value = "/getOpenId", method = {RequestMethod.POST})
   	public BaseResult getOpenId(@RequestBody Map<String, Object> param) {
		return wxService.getOpenId(param);
    }
	
	/**
	 * 统一下单
	 */
	@RequestMapping(value = "/doUnifiedOrder", method = {RequestMethod.POST})
   	public BaseResult doUnifiedOrder(@RequestBody Map<String, Object> param) {
		return wxService.doUnifiedOrder(param);
    }
	
	/**
	 * 支付结果通知
	 */
	@RequestMapping(value = "/payNotify", method = {RequestMethod.POST})
   	public String payNotify() {
		return wxService.payNotify();
    }
	
	/**
	 * wx.config参数
	 */
	@RequestMapping(value = "/getWXConfigParams", method = {RequestMethod.POST})
   	public BaseResult getWXConfigParams(@RequestBody String url) throws Exception {
		return wxService.getWXConfigParams(url);		     	 
    }
	
	/**
	 * 点击别人分享的链接进入界面，跳转到微信网页授权页面
	 */
	@RequestMapping(value = "/share", method = {RequestMethod.GET})
   	public void shareRedirect() throws Exception {
		response.sendRedirect(wxService.shareRedirect());	
    }
	
}
