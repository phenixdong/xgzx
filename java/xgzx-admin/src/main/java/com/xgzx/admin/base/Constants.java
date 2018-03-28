package com.xgzx.admin.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {

	// 视频分类id
	// 我的账号
//	public static final Integer CATEGORY_ID = 818;
	//客户账号
	public static final Integer CATEGORY_ID = 714;
	
//	public static final String BASE_URL = "http://18";
	public static final String BASE_URL = "https://phe";
	
	// 我的账号
//	public static final String APP_ID = "";
//	public static final String APP_SECRET = "";
//	public static final String MCH_ID = "";
//	public static final String MCH_SECRET = "";
	// 客户账号
	public static final String APP_ID = "";
	public static final String APP_SECRET = "";
	public static final String MCH_ID = "";
	public static final String MCH_SECRET = "";
	
	public static final int VIDEO_PAGE_SIZE = 5;
	
	public static final String REDIRECT_BACK_URL = BASE_URL + "redirectBack/";
	
	public static final String BASE_HASH_URL = BASE_URL + "#/";
	
	public static final String PAY_NOTIFY_URL = BASE_URL + "xgzxapi/admin/wx/payNotify";
	
	
}
