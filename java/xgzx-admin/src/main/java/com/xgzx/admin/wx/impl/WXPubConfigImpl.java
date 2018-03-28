package com.xgzx.admin.wx.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.xgzx.admin.base.Constants;
import com.xgzx.wxpay.IWXPayDomain;
import com.xgzx.wxpay.WXPayConfig; 

/**
 * 公众号网页授权配置
 * @author daidong
 */ 
public class WXPubConfigImpl extends WXPayConfig{

    private byte[] certData;
    private static WXPubConfigImpl INSTANCE;

    private WXPubConfigImpl(String certPath) throws Exception{
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    public static WXPubConfigImpl getInstance(String certPath) throws Exception{
        if (INSTANCE == null) {
            synchronized (WXPubConfigImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WXPubConfigImpl(certPath);
                }
            }
        }
        return INSTANCE;
    }

    public String getAppID() {
    	return Constants.APP_ID; 
    }

    public String getMchID() {
    	return Constants.MCH_ID;
    }

    //公众号appSecret
    public String getKey() {
    	return Constants.APP_SECRET;
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis;
        certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }


    public int getHttpConnectTimeoutMs() {
        return 2000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    public IWXPayDomain getWXPayDomain() {
        return WXPayDomainSimpleImpl.instance();
    }

    public String getPrimaryDomain() {
        return "api.mch.weixin.qq.com";
    }

    public String getAlternateDomain() {
        return "api2.mch.weixin.qq.com";
    }

    @Override
    public int getReportWorkerNum() {
        return 1;
    }

    @Override
    public int getReportBatchSize() {
        return 2;
    }
}
