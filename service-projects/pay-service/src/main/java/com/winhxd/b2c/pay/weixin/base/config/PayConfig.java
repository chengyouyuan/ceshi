package com.winhxd.b2c.pay.weixin.base.config;

import com.winhxd.b2c.pay.weixin.base.wxpayapi.IWXPayDomain;
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
public class PayConfig{

    /**
     * 商户号的appid或商户号绑定的appid
     */
    @Value("${WX.MCH_APP_ID}")
    private String mchAppID;

    /**
     * 公众账号ID
     */
    @Value("${WX.APP_ID}")
    private String appID;

    /**
     * 商户号
     */
    @Value("${WX.MCH_ID}")
    private String mchID;

    /**
     * API 密钥
     */
    @Value("${WX.KEY}")
    private String key;

    /***
     * 证书路径
     */
    @Value("${WX.CERT_PATH}")
    private String certPath;

    /***
     * 支付回调配置
     */
    @Value("${DOMAIN.CALLBACK_DOMAIN_URL}${WX.PAY_NOTIFY_URL}")
    private String payNotifyUrl;

    /***
     * 退款回调配置
     */
    @Value("${DOMAIN.CALLBACK_DOMAIN_URL}${WX.REFUND_NOTIFY_URL}")
    private String refundNotifyUrl;

    /***
     * 门店appID
     */
    @Value("${WX.STORE_APP.APP_ID}")
    private String storeAppID;

    /***
     * 门店appSecret
     */
    @Value("${WX.STORE_APP.APP_SECRET}")
    private String storeAppSecret;

    /**
     * RSA加密公钥
     */
    @Value("${WX.PUBLIC_KEY}")
    private String publicKey;

    public String getAppID() {
        return appID;
    }

    public String getMchAppID() {
        return mchAppID;
    }

    public String getMchID() {
        return mchID;
    }

    public String getKey() {
        return key;
    }

    public InputStream getCertStream() throws FileNotFoundException {
        return new FileInputStream(certPath);
    }

    public String getRSAPublicKey() {
        return publicKey;
    }

    public int getHttpConnectTimeoutMs() {
        return 6*1000;
    }

    public int getHttpReadTimeoutMs() {
        return 8*1000;
    }

    public IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain(){

            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {
            }

            @Override
            public DomainInfo getDomain(PayConfig config) {
                return new DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
    }

    public boolean shouldAutoReport() {
        return false;
    }

    public int getReportWorkerNum() {
        return 6;
    }

    public int getReportQueueMaxSize() {
        return 10000;
    }

    public int getReportBatchSize() {
        return 10;
    }

	public String getPayNotifyUrl() {
		return payNotifyUrl;
	}

	public String getRefundNotifyUrl() {
		return refundNotifyUrl;
	}

    public String getStoreAppID() {
        return storeAppID;
    }

    public String getStoreAppSecret() {
        return storeAppSecret;
    }
}
