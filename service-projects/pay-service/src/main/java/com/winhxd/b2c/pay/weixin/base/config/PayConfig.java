package com.winhxd.b2c.pay.weixin.base.config;

import com.winhxd.b2c.pay.weixin.base.wxpayapi.IWXPayDomain;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayConfig;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * PayConfig
 *
 * @Author yindanqing
 * @Date 2018/8/15 14:15
 * @Description: 微信支付配置
 */
@Component
public class PayConfig extends WXPayConfig {

    /**
     * 商户号的appid或商户号绑定的appid
     */
    @Value("${WX.mchAppID}")
    private String mchAppID;

    /**
     * 公众账号ID
     */
    @Value("${WX.appID}")
    private String appID;

    /**
     * 商户号
     */
    @Value("${WX.mchID}")
    private String mchID = "1467361502";

    /**
     * API 密钥
     */
    @Value("${WX.key}")
    private String key;

    /***
     * 证书路径
     */
    @Value("${WX.certPath}")
    private String certPath;

    @Override
    public String getAppID() {
        return appID;
    }

    @Override
    public String getMchAppID() {
        return mchAppID;
    }

    @Override
    public String getMchID() {
        return mchID;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public InputStream getCertStream() throws FileNotFoundException {
        return new FileInputStream(certPath);
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return super.getHttpConnectTimeoutMs();
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return super.getHttpReadTimeoutMs();
    }

    @Override
    public IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain(){

            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {
            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
    }

    @Override
    public boolean shouldAutoReport() {
        return super.shouldAutoReport();
    }

    @Override
    public int getReportWorkerNum() {
        return super.getReportWorkerNum();
    }

    @Override
    public int getReportQueueMaxSize() {
        return super.getReportQueueMaxSize();
    }

    @Override
    public int getReportBatchSize() {
        return super.getReportBatchSize();
    }
}
