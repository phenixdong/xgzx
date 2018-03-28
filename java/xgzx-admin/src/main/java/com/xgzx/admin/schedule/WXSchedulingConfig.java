package com.xgzx.admin.schedule;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.xgzx.admin.service.WXService;
 

/**
 * Scheduled不会启动立即执行，立即执行要在启动项ApplicationRunner里面调用
 */
@Configuration
@EnableScheduling
public class WXSchedulingConfig {
	 
	@Autowired
	WXService wxService;
	
	/**
	 * 每1小时，更新access_token和jsapi_ticket
	 */
	@Scheduled(cron = "${wx.quartztimer}")
    public void scheduler() throws Exception {
		String accessToken = wxService.getJsAccessToken();
		if (null != accessToken) {
			wxService.getJsApiTicket(accessToken);
		}
    }
	
}
