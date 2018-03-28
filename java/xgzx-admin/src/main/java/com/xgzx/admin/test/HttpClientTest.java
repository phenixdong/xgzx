package com.xgzx.admin.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.xgzx.admin.entity.User;
import com.xgzx.base.BaseResult;
import com.xgzx.util.UuidKeyUtil;

public class HttpClientTest {
	
	static String userId = "";
	
	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 1000; i++) {
			userRegister();
	        bulkBuySelect();
	        bulkBuyRegister();
		}
    }

    /**
     * get请求
     */
    public static void bulkBuySelect() throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        URIBuilder uri = new URIBuilder("http://localhost:9001/admin/bulkBuy/select/196e8314ab67458eb889015e36a6c970");
        //uri.addParameter("id", "10001");
        HttpGet hg = new HttpGet(uri.build());
        hg.addHeader(HTTP.CONTENT_TYPE, "application/json");
        hg.setHeader("client", "abcd");
        
        CloseableHttpResponse response = client.execute(hg);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            String resStr = EntityUtils.toString(entity, "utf-8");
            System.out.println("resp " + resStr);
        } else {
            System.out.println("fail " + statusCode);
        }

        response.close();
        client.close();
    }
    
    public static void userRegister() throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://localhost:9001/admin/user/register");
        post.addHeader(HTTP.CONTENT_TYPE, "application/json");
        post.setHeader("client", "abcd");
        Map<String, Object> map = new HashMap<>();
        String openId = UuidKeyUtil.get12UUID();
        map.put("openId", openId);
        map.put("nickname", "测试" + UuidKeyUtil.get28UUID());
        String jsonstr = JSON.toJSONString(map);
        StringEntity se = new StringEntity(jsonstr);
        se.setContentType("text/json");
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        post.setEntity(se);    
        
        CloseableHttpResponse response = client.execute(post);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            String resStr = EntityUtils.toString(response.getEntity());
            Map<String, Object> resultMap = JSON.parseObject(resStr, Map.class);
            Map<String, Object> dataMap = (Map<String, Object>) resultMap.get("data");
            userId = (String) dataMap.get("userId");
            System.out.println("resp " + resStr);
        } else {
            System.out.println("fail: " + statusCode);
        }

        response.close();
        client.close();
    }

    public static void bulkBuyRegister() throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://localhost:9001/admin/bulkBuy/register");
        post.addHeader(HTTP.CONTENT_TYPE, "application/json");
        post.setHeader("client", "abcd");
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("bulkBuyId", "196e8314ab67458eb889015e36a6c970");
        map.put("password", "ddc1");
        String jsonstr = JSON.toJSONString(map);
        StringEntity se = new StringEntity(jsonstr);
        se.setContentType("text/json");
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        post.setEntity(se);    
        
        CloseableHttpResponse response = client.execute(post);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            String resStr = EntityUtils.toString(response.getEntity());
            System.out.println("resp " + resStr);
        } else {
            System.out.println("fail: " + statusCode);
        }

        response.close();
        client.close();
    }
}
