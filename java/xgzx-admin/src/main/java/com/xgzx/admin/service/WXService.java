package com.xgzx.admin.service;
 
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.xgzx.base.BaseResult; 

/**
 * @author DaiDong
 * @since 2017-12-19
 * @email 755144556@qq.com
 */
public interface WXService {
	
	BaseResult getOpenId(Map<String, Object> param);
	
	BaseResult doUnifiedOrder(Map<String, Object> param);
	
	String payNotify();
	
	BaseResult getWXConfigParams(String url);
	
	String getJsAccessToken() throws Exception;
	
	String getJsApiTicket(String accessToken) throws Exception;
	
	String shareRedirect() throws Exception;
	
	BaseResult payWithdraw(Map<String, Object> param) throws Exception;
	
	BaseResult checkRedPackStatus(String mchBillNo) throws Exception;
	
	
}
