package com.xgzx.admin.interceptor;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 登录状态检查，未登录不允许访问
 */
public class LoginInterceptor implements HandlerInterceptor  {
	
	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, Object handler) throws Exception { 
		String url = request.getRequestURI();

		if (url.indexOf("login") > 0 
        	|| url.indexOf("logout") > 0
        	|| url.indexOf("checkCode") > 0
        	|| url.indexOf("static") > 0 // 下载文件
        	|| url.indexOf("upload") > 0 // 上传
        	|| url.indexOf("share") > 0 // 微信分享重定向
        	|| url.indexOf("payNotify") > 0 // 微信支付通知
        	|| url.indexOf("getVideoUploadAuth") > 0 // 视频上传地址
        	|| url.indexOf("getImageUploadAuth") > 0 // 封面上传地址
        	|| url.indexOf("error") > 0 
        	|| url.indexOf("getRebateCheckCode") > 0 
        	|| url.indexOf("test") > 0 
        	) {
        	// 不需登录也可访问
            return true;
        } else {
        	String client = request.getHeader("client");// 移动端访问
        	if (null == request.getSession().getAttribute("loginId")
        			&& null == client) {
        		response.setCharacterEncoding("utf-8");
            	response.setHeader("Accept", "application/json, text/plain, */*");
            	response.setHeader("Content-Type", "application/json;charset=UTF-8");
            	response.getWriter().print("{\"returnCode\":\"-1\",\"returnMsg\":\"请登录！\"}");
        		System.out.println("login interceptor url " + url);
            	return false;
        	} else {
        		return true;
        	}
        } 
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
