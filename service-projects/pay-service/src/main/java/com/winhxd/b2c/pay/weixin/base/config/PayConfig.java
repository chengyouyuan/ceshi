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

    /**
     * RSA加密公钥
     */
    @Value("${WX.PUBLIC_KEY}")
    private String publicKey;

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
    public String getRSAPublicKey() {
        return publicKey;
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

	public String getPayNotifyUrl() {
		return payNotifyUrl;
	}

	public String getRefundNotifyUrl() {
		return refundNotifyUrl;
	}

}
