package com.xgzx.admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.xgzx.admin.service.WXService;

/**
 * 启动时运行
 */
@Component
public class MyApplicationRunner implements ApplicationRunner {

	@Autowired
	WXService wxService;
	
	/* 
	 * 注意，需要加try-catch，否则如果调用接口抛出异常，会导致容器启动失败
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		// 读取微信access_token和jsapi_ticket
		try {
			String accessToken = wxService.getJsAccessToken();
			if (null != accessToken) {
				wxService.getJsApiTicket(accessToken);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
