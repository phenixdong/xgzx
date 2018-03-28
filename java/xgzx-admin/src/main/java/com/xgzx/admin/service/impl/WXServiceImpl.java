package com.xgzx.admin.service.impl;
 
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.xgzx.admin.base.Constants;
import com.xgzx.admin.entity.CommonParam;
import com.xgzx.admin.entity.Groups;
import com.xgzx.admin.entity.GroupsUser;
import com.xgzx.admin.entity.Lesson;
import com.xgzx.admin.entity.MemberType;
import com.xgzx.admin.entity.Orders;
import com.xgzx.admin.entity.PaidLesson;
import com.xgzx.admin.entity.Rebate;
import com.xgzx.admin.entity.RebateTransaction;
import com.xgzx.admin.entity.Transaction;
import com.xgzx.admin.entity.User;
import com.xgzx.admin.entity.UserMember;
import com.xgzx.admin.mapper.CommonParamMapper;
import com.xgzx.admin.mapper.GroupsMapper;
import com.xgzx.admin.mapper.LessonMapper;
import com.xgzx.admin.mapper.PaidLessonMapper;
import com.xgzx.admin.mapper.RebateMapper;
import com.xgzx.admin.mapper.RebateTransactionMapper;
import com.xgzx.admin.mapper.UserMapper;
import com.xgzx.admin.service.GroupsService;
import com.xgzx.admin.service.GroupsUserService;
import com.xgzx.admin.service.MemberTypeService;
import com.xgzx.admin.service.OrdersService;
import com.xgzx.admin.service.TransactionService;
import com.xgzx.admin.service.UserMemberService;
import com.xgzx.admin.service.UserService;
import com.xgzx.admin.service.WXService;
import com.xgzx.admin.util.HttpUtil;
import com.xgzx.admin.wx.impl.WXPayConfigImpl;
import com.xgzx.admin.wx.impl.WXPubConfigImpl;
import com.xgzx.base.BaseErrResult;
import com.xgzx.base.BaseResult;
import com.xgzx.exception.CommonError;
import com.xgzx.util.DateTimeUtil;
import com.xgzx.util.JsonObjUtils;
import com.xgzx.util.RequestUtil;
import com.xgzx.util.UuidKeyUtil;
import com.xgzx.wxpay.WXPay;
import com.xgzx.wxpay.WXPayConstants;
import com.xgzx.wxpay.WXPayUtil;
import com.xgzx.wxpub.WXInfo;
import com.xgzx.wxpub.WXPubUtil;

/**
 * 公众号网页授权
 * @author DaiDong
 * @since 2017-12-19
 * @email 755144556@qq.com
 */
@Service
@Transactional
public class WXServiceImpl implements WXService {
	  
	DecimalFormat DF = new DecimalFormat("#.00");
	
	@Autowired 
	HttpServletRequest request;
	@Autowired 
	MemberTypeService memberTypeService;
	@Autowired 
	OrdersService ordersService;
	@Autowired
	UserService userService;
	@Autowired
	UserMemberService userMemberService;
	@Autowired
	GroupsMapper groupsMapper;
	@Autowired
	GroupsUserService groupsUserService;
	@Autowired
	RebateMapper rebateMapper;
	@Autowired
	TransactionService transactionService;
	@Autowired
	CommonParamMapper commonParamMapper;
	@Autowired
	UserMapper userMapper;
	@Autowired
	RebateTransactionMapper rebateTransactionMapper;
	@Autowired
	LessonMapper lessonMapper;
	@Autowired
	PaidLessonMapper paidLessonMapper;
	
	@Value("${wx.certPath}")
    private String certPath;
	@Value("${wx.mchName}")
    private String mchName;
	@Value("${wx.hostIp}")
	private String hostIp;
	
	WXPay wxpay;  
    WXPayConfigImpl wxPayConfig; 
	WXPubConfigImpl wxPubConfig;
	
	/*
	 * 获取openId和access_token，
	 * 注意，access_token是和用户关联的access_token，
	 * 不是公众号基本配置中的access_token
	 */
	@Override
	public BaseResult getOpenId(Map<String, Object> param) {
		System.out.println("getOpenId sessionId " + request.getSession().getId());
		//入参code
		String code = (String) param.get("code");
		System.out.println("getOpenId code " + code);
		
        //注意参数secret是公众号的appSecret，不是商户的api密钥
        try {
			wxPubConfig = WXPubConfigImpl.getInstance(certPath);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
        
        // 请求
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token"
        		+ "?appid=" + wxPubConfig.getAppID()
        		+ "&secret=" + wxPubConfig.getKey()
        		+ "&code=" + code
        		+ "&grant_type=authorization_code";
        HttpUtil httpUtil = new HttpUtil();
        String result = httpUtil.request(url);
        
        // 返回数据
        Map<String, Object> map = null;
		try {
			map = JsonObjUtils.json2map(result);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
        String access_token = (String) map.get("access_token"); 
        String openId = (String) map.get("openid"); 
        if (null != access_token && null != openId) {
        	// openId要全局存储，调用次数限制
        	request.getSession().setAttribute("openId", openId);
        } else {
        	return new BaseResult("0", result, null);
        }
        
        // 获取userInfo
        String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo"
        		+ "?access_token=" + access_token
        		+ "&openid=" + openId
        		+ "&lang=zh_CN";
        String userInfoResult = httpUtil.request(userInfoUrl);
        System.out.println("userInfo " + userInfoResult);
        
        // 返回数据
        Map<String, Object> userInfoMap = null;
		try {
			// 示例：{"openid":"oTLDUwXtiPdlmAxR9e3vtUJDCX4E","nickname":"phenix","sex":0,"language":"zh_CN","city":"","province":"","country":"","headimgurl":"","privilege":[]}
			userInfoMap = JsonObjUtils.json2map(userInfoResult);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
		
		// 用户本地注册
		userService.register(openId, userInfoMap);
        
        return new BaseResult("1", "成功", openId);
	}

	/*  
	 * 统一下单
	 */
	@Override
	public BaseResult doUnifiedOrder(Map<String, Object> param) {
		// 初始化
		try {
			wxPayConfig = WXPayConfigImpl.getInstance(certPath);
			wxpay = new WXPay(wxPayConfig);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
         
		// 入参
		String openId = (String) param.get("openId");
        String userId = (String) param.get("userId"); 
        Integer productType = (Integer) param.get("productType");
        String totalFeeStr = (String) param.get("totalFee");
        BigDecimal totalFee = new BigDecimal(totalFeeStr);
        List<String> productIds = (List<String>) param.get("productIds");
		String clientIp = RequestUtil.getIpAddr(request);
        // 订单id
        String orderId = WXPayUtil.generateUUID();
        
        // 记录推荐人
        String recommenderUserId = null;
        User user = userMapper.selectById(userId);
        if (null == user) {
        	return new BaseResult("0", "没有该用户", null);
        }
        if (null != param.get("recommenderUserId") && !"".equals(param.get("recommenderUserId"))) {
        	recommenderUserId = (String) param.get("recommenderUserId");
        // 没有推荐人，使用默认推荐人
        } else {
        	if (null != user.getRecommenderUserId() && !"".equals(user.getRecommenderUserId())) {
	        	recommenderUserId = user.getRecommenderUserId();
        	}
        }
        
        // 总价格检查，防止传错参数，导致多付或者少付
        if (!checkFee(productType, productIds, totalFee)) {
        	return new BaseResult("0", "支付价格错误", null);
        }
        
        // 不需主动调用签名，appid、mch_id、nonce_str不用设置，不用排序，
        // requestWithOutCert中的fillRequestData会读取默认值、排序，并签名
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("device_info", "WEB");
        data.put("body", "有效管理之道课程");
        data.put("out_trade_no", orderId);        
        data.put("fee_type", "CNY");
        BigDecimal totalFeeDecimal = totalFee.multiply(new BigDecimal(100));
        data.put("total_fee", (totalFeeDecimal.intValue() + ""));
        data.put("spbill_create_ip", clientIp);
        data.put("notify_url", Constants.PAY_NOTIFY_URL);
        System.out.println("notify_url " + Constants.PAY_NOTIFY_URL);
        data.put("trade_type", "JSAPI");
        data.put("openid", openId);
        if (null != recommenderUserId && !"".equals(recommenderUserId)) {
        	data.put("attach", recommenderUserId);// 附加信息，传推荐人userId，用于支付通知时返点
        }
        data.put("sign_type", WXPayConstants.MD5);
        
        // 请求微信服务器，获取prepayId
        String prepayId = "";
        try {
        	System.out.println("data " + data);
            Map<String, String> map = wxpay.unifiedOrder(data);
            System.out.println(map);
            prepayId = map.get("prepay_id");
            request.getSession().setAttribute("prepayId", prepayId);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResult("0", e.getMessage(), null);
        } 
        
        // 支付参数签名，用于下一步支付
        HashMap<String, String> payData = new HashMap<String, String>();
        payData.put("appId", wxPayConfig.getAppID());
        payData.put("timeStamp", WXPayUtil.getCurrentTimestamp() + "");
        payData.put("nonceStr", WXPayUtil.generateNonceStr());
        payData.put("package", "prepay_id=" + prepayId);
        payData.put("signType", WXPayConstants.MD5);
        try {
			payData.put("paySign", WXPayUtil.generateSignature(payData, wxPayConfig.getKey()));
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", e.getMessage(), null);
		}
        
        // 新增订单
        Orders orders = new Orders();
        orders.setOrderId(orderId);
        orders.setUserId(userId);
        orders.setProductId(productIds.get(0));
        orders.setPayState(0); 
        Date nowTime = new Date();
        orders.setCreateTime(nowTime);
        orders.setUpdateTime(nowTime);
        orders.setTotalFee(totalFee);
        orders.setProductType(productType);// 商品类型，1会员，2商品
        // 推荐人userId
        if (null != recommenderUserId && !"".equals(recommenderUserId)) {
        	orders.setRecommenderUserId(recommenderUserId);
        }
        orders.setValidTag(1);
        ordersService.insert(orders);
        
        // 订单时间，用于10分钟之内，查询视频信息，都允许通过，防止支付消息接收延迟
        request.getSession().setAttribute("doUnifiedOrderTime", System.currentTimeMillis());
        
		return new BaseResult("1", "成功", payData);
	}

	boolean checkFee(Integer productType, List<String> productIds, BigDecimal totalFee) {
		// 商品类型，1会员，2商品
		if (1 == productType) {
			// 查询会员价格是否与支付价格一致
			String productId = productIds.get(0);
			MemberType memberType = memberTypeService.selectById(productId);
			String totalFeeStr = DF.format(totalFee);
			if (memberType.getPrice().equals(new BigDecimal(totalFeeStr))) {
				return true;
			} else {
				return false;
			}
		// 购买类型为“课程”
		} else {
			// 查询会员价格是否与支付价格一致
			String productId = productIds.get(0);
			Lesson lesson = lessonMapper.selectById(productId);
			String totalFeeStr = DF.format(totalFee);
			if (lesson.getPrice().equals(new BigDecimal(totalFeeStr))) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	@Override
	public String payNotify() {
		System.out.println("payNotify");
		// 读取数据流
		ServletInputStream instream = null;
		StringBuffer sb = null;
		try {
			instream = request.getInputStream();
			sb = new StringBuffer();
			int len = -1;
			byte[] buffer = new byte[1024];
			while ((len = instream.read(buffer)) != -1) {
				sb.append(new String(buffer,0,len));
			}
			instream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return returnXml(WXPayConstants.FAIL, "文件流读取错误");
		}
		
		// 解析xml
		Map<String,String> notifyMap = null;
		try {
			System.out.println("payNotify string " + sb.toString());
			notifyMap = wxpay.processResponseXml(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		if (!WXPayConstants.SUCCESS.equals(notifyMap.get("return_code"))) {
			// 收到失败消息，返回微信已经收到
			return returnXml(WXPayConstants.SUCCESS, "OK");
		}  
		
		// 检查签名
		try {
			if (!WXPayUtil.isSignatureValid(notifyMap, wxPayConfig.getKey(), 
					WXPayConstants.SignType.MD5)) {
				return returnXml(WXPayConstants.FAIL, "签名失败");
			} 
		} catch (Exception e) {
			e.printStackTrace();
			return returnXml(WXPayConstants.FAIL, "签名失败");
		}
		
		// 检查支付金额
		String orderId = notifyMap.get("out_trade_no");// 订单id，由本地生成，下单时传给微信支付
		Orders orders = ordersService.selectById(orderId);
		// 以分为单位
		BigDecimal ordersTotalFeeDecimal = orders.getTotalFee().multiply(new BigDecimal(100));
        Integer ordersTotalFee = ordersTotalFeeDecimal.intValue();
        Integer wxTotalFee = Integer.valueOf(notifyMap.get("total_fee"));
		if (!ordersTotalFee.equals(wxTotalFee)) {
        	return returnXml(WXPayConstants.FAIL, "金额错误");
        }
		
     	// 更新订单状态
		orders.setIsSubscribe("Y".equals(notifyMap.get("is_subscribe")) ? 1 : 0);
		orders.setTradeType(notifyMap.get("trade_type"));
		orders.setBankType(notifyMap.get("bank_type"));
		String timeEnd = notifyMap.get("time_end");
		try {
			Date date = DateTimeUtil.yyyymmddHHmmss.parse(timeEnd);
			orders.setEndTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		orders.setPayState(1);
        ordersService.updateById(orders);
        
        // 增加一条交易记录
 		Transaction transaction = new Transaction();
 		transaction.setUserId(orders.getUserId());
 		transaction.setType(orders.getProductType());// 交易类型，1购买会员，2购买课程，3充值，4提现
 		transaction.setOperation(2);// 1收入，2支出
 		transaction.setNumber(orders.getTotalFee());// 单位：元
 		transaction.setOrderId(orderId);
 		transaction.setPayTransactionType(1);// 支付交易类型，1微信，2银行卡
 		transaction.setPayTransactionId(notifyMap.get("transaction_id")); // 微信支付交易id 
 		transaction.setValidTag(1);
 		Date nowTime = new Date();
 		transaction.setCreateTime(nowTime);
 		transaction.setUpdateTime(nowTime);
 		transactionService.insert(transaction);
     		
 		// 购买会员，更新会员状态
 		if (1 == orders.getProductType()) {
	        // 查询会员有效期
	        MemberType memberType = memberTypeService.selectById(orders.getProductId());
	        Date startTime = new Date();
	    	Calendar rightNow = Calendar.getInstance();
	    	rightNow.setTime(startTime);
	    	rightNow.add(Calendar.MONTH, memberType.getPeriod());
	    	Date endTime = rightNow.getTime();
	    	
	    	// 更新用户会员身份、有效期
	        UserMember userMember = new UserMember();	
			userMember.setUserId(orders.getUserId());
			userMember.setMemberTypeId(Integer.valueOf(orders.getProductId()));
			userMember.setStartTime(startTime);
			userMember.setEndTime(endTime);
			userMember.setBuyType(1);// 1单次购买，2团购
			userMember.setValidTag(1);
			userMemberService.insertOrUpdateByUserId(userMember);
		// 购买课程，增加一条课程购买记录
 		} else {
 			PaidLesson paidLesson = new PaidLesson();
 			paidLesson.setUserId(orders.getUserId());
 			paidLesson.setLessonId(Integer.valueOf(orders.getProductId()));
 			paidLesson.setValidTag(1);
 			paidLessonMapper.insert(paidLesson);
 		}
 		
 		// 计算返点金额
		CommonParam commonParam = new CommonParam();
		commonParam.setParamName("rebateRate");
		commonParam = commonParamMapper.selectOne(commonParam);
		BigDecimal rebatePercent = new BigDecimal(commonParam.getParamValue());
		BigDecimal rebateNumber = orders.getTotalFee().multiply(rebatePercent)
				.divide(new BigDecimal(100));
 				
		// 如果有推荐人，给推荐人返点，不能返点给自己
		String recommenderUserId = notifyMap.get("attach");// 推荐人userId从统一下单attach传入
		String rebateUserId = null;
		if (null != recommenderUserId && !"".equals(recommenderUserId)
			&& !orders.getUserId().equals(recommenderUserId)) {
			rebateUserId = recommenderUserId;
		} else {
			rebateUserId = "1234567890123456";//公用账户，固定值
		}
		// 更新返点账户
		Rebate rebate  = new Rebate();
		rebate.setUserId(rebateUserId);
		rebate = rebateMapper.selectOne(rebate);
		if (null == rebate) {
			CommonError.CommonErr(new BaseErrResult("-1", "返点查询失败"));
		}
		rebate.setNumber(rebate.getNumber().add(rebateNumber));
		Integer ret = rebateMapper.updateById(rebate);
		if (ret <= 0) {
			CommonError.CommonErr(new BaseErrResult("-1", "返点更新失败"));
		}
		
		//增加返点记录
		RebateTransaction rebateTransaction = new RebateTransaction();
		rebateTransaction.setUserId(rebateUserId);
		rebateTransaction.setNumber(rebateNumber);
		rebateTransaction.setOperation(1);// 交易类型，1收入，2支出，3红包未领取退款
		rebateTransaction.setOrderId(orderId);
		rebateTransaction.setValidTag(1);
		ret = rebateTransactionMapper.insert(rebateTransaction);
		if (ret <= 0) {
			CommonError.CommonErr(new BaseErrResult("-1", "返点记录新增失败"));
		}
		
		// 如果有推荐人，加入推荐人拥有的群组
//		if (null != recommenderUserId && !"".equals(recommenderUserId)) {
//			// 新建群组
//			Groups groups = new Groups();
//			groups.setMasterUserId(recommenderUserId);
//			groups = groupsMapper.selectOne(groups);
//			if (null == groups) {
//				User recommenderUser = userMapper.selectById(recommenderUserId);
//				groups = new Groups();
//				groups.setMasterUserId(recommenderUserId);
//				groups.setValidTag(1);
//				groups.setCreateTime(new Date());
//				groups.setGroupName(recommenderUser.getUserName() + "的群组");
//				ret = groupsMapper.insertReturnId(groups);
//				if (ret <= 0) {
//					CommonError.CommonErr(new BaseErrResult("-1", "群组创建失败"));
//				}
//			}
//			int groupId = groups.getGroupId();
//			System.out.println("群组ID: " + groupId);
//			
//			// 新增用户-群组关系
//			Map<String, Object> groupMemberMap = new HashMap<>();
//			groupMemberMap.put("group_id", groupId);
//			groupMemberMap.put("user_id", orders.getUserId());
//			List<GroupsUser> groupsUserList = groupsUserService.selectByMap(groupMemberMap);
//			if (null == groupsUserList || 0 == groupsUserList.size()) {
//				// 新增
//				GroupsUser groupsUser = new GroupsUser();
//				groupsUser.setGroupId(groupId);
//				groupsUser.setUserId(orders.getUserId());
//				groupsUser.setValidTag(1);
//				groupsUser.setMessageReadTime(new Date());
//				groupsUserService.insert(groupsUser);
//			}
//			System.out.println("userId " + orders.getUserId() + " 加入群组");
//		}
        
		// 返回微信服务器
        return returnXml(WXPayConstants.SUCCESS, "OK");
	}

    String returnXml(String code, String msg) {
    	// 返回给微信的错误值
		Map<String, String> returnMap = new HashMap<>();
		returnMap.put("return_code", code);
		returnMap.put("return_msg", msg);
		String xmlData = "";
		try {
			xmlData = WXPayUtil.mapToXml(returnMap);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("payNotify error " + e.getMessage());
		}
		return xmlData;
    }

    /*  
     * 签名注意：
     * 1. 要使用签名工具验证，容易找问题，签名工具在公众平台文档，JS-SDK说明文档，附录5
	 * 2. 签名工具会删除url的#之后的字符，微信服务器使用url时也会删除，
	 * 	     所以代码生成签名时url也要删除#之后的字符，才能签名成功
	 * 3. 签名字符串不需要key，字母小写（微信支付的签名是大写）
	 * 4. timestamp是字符串，前端wx.config也是用字符串
     */
    @Override
	public BaseResult getWXConfigParams(String url) {
    	System.out.println("getWXConfigParams url " + url);
    	HashMap<String, String> respData = new HashMap<>();
    	String noncestr = WXPubUtil.generateNonceStr();
    	String timestamp = WXPubUtil.getCurrentTimestamp() + "";
    	// 签名参数 noncestr、jsapi_ticket、timestamp、url
    	HashMap<String, String> signData = new HashMap<>();
    	signData.put("noncestr", noncestr);
    	signData.put("timestamp", timestamp);
    	signData.put("url", url);
    	
    	// 获取jsapi_ticket
    	String jsapiTicket = WXInfo.getJsapiTicket();
    	if (null == jsapiTicket || "".equals(jsapiTicket)) {
    		return new BaseResult("0", "jsapi_ticket获取失败", null);
    	}
    	System.out.println("getWXConfigParams jsapiTicket " + jsapiTicket);
    	
    	signData.put("jsapi_ticket", jsapiTicket);
    	System.out.println("signData " + JSON.toJSONString(signData));
    	    	
    	// 签名，sha1
    	String signature;
		try {
			signature = WXPubUtil.generateSignature(signData);
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseResult("0", "签名失败", e.getMessage());
		}
		System.out.println("signature " + signature);
		
    	respData.put("appId", wxPubConfig.getAppID());
    	respData.put("timestamp", timestamp);
    	respData.put("noncestr", noncestr);
    	respData.put("signature", signature);
		return new BaseResult(respData);
	}
    
    /**
	 * 获取js-sdk所需access_token，文档在微信公众平台技术文档-开始开发-获取access_token,
	 * access_token每2小时失效，要存入全局内存，不能频繁请求，
	 * 定时任务每1小时请求一次，更新access_token
	 */
	public String getJsAccessToken() throws Exception {
		System.out.println("getJsAccessToken");
		wxPubConfig = WXPubConfigImpl.getInstance(certPath);
		String url = "https://api.weixin.qq.com/cgi-bin/token?"
				+ "grant_type=client_credential"
				+ "&appid=" + wxPubConfig.getAppID()
				+ "&secret=" + wxPubConfig.getKey();
		HttpUtil httpUtil = new HttpUtil();
	   	String result = httpUtil.request(url);
	   	String accessToken = "";
    	if (null != result) {
    		Map<String, Object> map;
			map = JsonObjUtils.json2map(result);
    		accessToken = (String) map.get("access_token"); 
    		Integer errorcode = (Integer) map.get("errcode");
    		String errmsg = (String) map.get("errmsg");
    		if (null != accessToken) {
    			// accessToken存入全局内存
    			WXInfo.setAccessToken(accessToken);
    			return accessToken;
    		} else {
    			throw new Exception("JsAccessToken 获取失败：" + errmsg);
    		}
    	}
    	return null;
	}
	
	/**
	 * 获取js-sdk中wx.config所需jsapi_ticket，
	 * 文档在微信公众平台技术文档-微信网页开发-微信JS-SDK说明文档,
	 * jsapi_ticket每2小时失效，要存入全局内存，不能频繁请求，
	 * 定时任务每1小时请求一次，更新jsapi_ticket
	 */
	public String getJsApiTicket(String accessToken) throws Exception {
		System.out.println("getJsApiTicket");
		// 请求
		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?"
				+ "access_token=" + accessToken
				+ "&type=jsapi";
		HttpUtil httpUtil = new HttpUtil();
	   	String result = httpUtil.request(url);
		 
	    // 解析返回
    	if (null != result) {
			Map<String, Object> map = JsonObjUtils.json2map(result);
			String ticket = (String) map.get("ticket"); 
    		Integer errorcode = (Integer) map.get("errcode");
    		String errmsg = (String) map.get("errmsg");
    		if (null != ticket) {
    			// jsapi_ticket存入全局内存
    			WXInfo.setJsapiTicket(ticket);
    			return ticket;
    		} else {
    			throw new Exception("jsapi_ticket 获取失败：" + errmsg); 
    		}
    	}
    	return null;
	}

	/**
	 * 点击分享链接，跳转到微信网页授权页面
	 * （1）
	 * 分享redirect_url中，不能加#，因为微信重定向后，会在#前面增加？，
	 * 分享地址：https://show.mypro.com/#/firPage?userId=1
	 * 重定向后：https://show.mypro.com/?#/firPage?userId=1
	 * （2）
	 * 但是如果redirect_url不加#，则vue无法访问子页面（主页不加#也可以访问），
	 * （3）
	 * 解决方案：
	 * redirect_url中不加#，但加关键词redirectBack，
	 * 比如：https://show.mypro.com/redirectBack/firPage?userId=1
	 * 然后在nginx中，重定向，加上#，去掉关键词redirectBack，
	 * 比如：https://show.mypro.com/#/firPage?userId=1
	 */
	@Override
	public String shareRedirect() throws Exception {
		String paramStr = ""; 
		String pageName = ""; // vue中页面名称
		String uri = request.getRequestURI();
		System.out.println("shareRedirect uri " + uri);
		
		// 获取全部参数，拼接成redirect_url
		Map<String, String[]> paramMap = request.getParameterMap();
		for (Iterator iter = paramMap.entrySet().iterator(); iter.hasNext(); ){
        	Map.Entry element=(Map.Entry)iter.next();
        	String strKey = (String) element.getKey();
        	String[] value=(String[])element.getValue();
        	
        	// page不作为redirect_url的参数
        	if ("page".equals(strKey)) {
        		pageName = value[0];
        		// 首页没有pageName，直接使用域名
        		if ("null".equals(value[0])) {
        			pageName = "";
        		}
        		continue;
        	}
        	for (int i = 0; i < value.length; i++) {
        		paramStr = paramStr + "&" + strKey + "=" + value[i];
        	}  
		}
		paramStr = paramStr.substring(1, paramStr.length()); // 删除头部的&
		System.out.println("paramStr " + paramStr);
		// 格式：http://domain/?code=xxx&state=state#/lessonDetail，
		// 不是：http://domain/#/lessonDetail?code=xxx&state=state，
		// 因为非分享时，界面跳转是第一种格式，#放到最后也能正常跳转，并且支付时，微信服务器添加？也不影响支付，
		// 而第二种方式，虽然界面跳转地址规范，但是支付时，提示“当前页面的URL未注册”，
		// 就是因为这个地址格式格式和wx.config初始化时的格式不一样 
		String redirectUrl = Constants.BASE_URL + "?" + paramStr + "#/" + pageName;
		System.out.println("redirectUrl " + redirectUrl);
		
		// 重定向到微信授权
		wxPubConfig = WXPubConfigImpl.getInstance(certPath);
		String encodeUrl = URLEncoder.encode(redirectUrl, "UTF-8");
		String authUrl = "https://open.weixin.qq.com/connect/oauth2/authorize"
				+ "?appid=" + wxPubConfig.getAppID() + "&redirect_uri=" + encodeUrl
				+ "&response_type=code&scope=snsapi_userinfo&state=state"
				+ "#wechat_redirect";
		System.out.println("authUrl " + authUrl);
		return authUrl;
	}

	/*  
	 * 商家发红包给用户
	 * 注意，红包额度在“现金红包”-“产品设置”中修改
	 */
	@Override
	public BaseResult payWithdraw(Map<String, Object> param) throws Exception {
		// 初始化
		wxPayConfig = WXPayConfigImpl.getInstance(certPath);
		wxpay = new WXPay(wxPayConfig);
		String mchBillno = UuidKeyUtil.get28UUID();
		
		// 不需主动调用签名，wxappid, mch_id、nonce_str不用设置，不用排序，
        // requestWithCert中的fillRequestData读取这几个默认值、排序，并签名
        HashMap<String, String> payData = new HashMap<String, String>();
        payData.put("mch_billno", mchBillno);
        payData.put("wxappid", wxPayConfig.getAppID());
        payData.put("send_name", mchName);
        payData.put("re_openid", (String) param.get("openId"));
        payData.put("total_amount", (String) param.get("number"));
        payData.put("total_num", "1");
        payData.put("wishing", "恭喜发财");
        payData.put("client_ip", hostIp);
        payData.put("act_name", "有效管理之道课程");
        payData.put("remark", "有效管理之道课程");
        payData.put("scene_id", "PRODUCT_1");
        
        // 发送请求
        Map<String, String> map = wxpay.sendRedPack(payData);
        System.out.println("sendRedPack resp " + map.toString());
        if (WXPayConstants.SUCCESS.equals(map.get("result_code"))
        	&& WXPayConstants.SUCCESS.equals(map.get("return_code"))) {
        	return new BaseResult(map);
        } else {
        	return new BaseResult("0", map.get("err_code_des"), null);
        }
	}
	
	/*  
	 * 查询红包领取状态
	 */
	@Override
	public BaseResult checkRedPackStatus(String mchBillNo) throws Exception {
		// 初始化
		wxPayConfig = WXPayConfigImpl.getInstance(certPath);
		wxpay = new WXPay(wxPayConfig);
		
		// 不需主动调用签名，appid, mch_id、nonce_str不用设置，不用排序，
        // requestWithCert中的fillRequestData会读取默认值、排序，并签名
        HashMap<String, String> payData = new HashMap<String, String>();
        payData.put("mch_billno", mchBillNo);
        payData.put("bill_type", "MCHT");
        
        // 发送请求
        Map<String, String> map = wxpay.checkRedPackStatus(payData);
        System.out.println("checkRedPackStatus resp " + map.toString());
        if (WXPayConstants.SUCCESS.equals(map.get("result_code"))
        	&& WXPayConstants.SUCCESS.equals(map.get("return_code"))) {
        	return new BaseResult(map);
        } else {
        	return new BaseResult("0", map.get("err_code_des"), null);
        }
	}
	
}
