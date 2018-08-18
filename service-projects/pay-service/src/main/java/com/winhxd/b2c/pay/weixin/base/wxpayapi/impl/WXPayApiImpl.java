package com.winhxd.b2c.pay.weixin.base.wxpayapi.impl;

import java.util.Map;

import com.winhxd.b2c.pay.weixin.base.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.pay.weixin.base.config.PayConfig;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayApi;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayConstants;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayConstants.SignType;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayRequest;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayUtil;
import com.winhxd.b2c.pay.weixin.util.BeanAndXmlUtil;

/**
 * 对接微信支付API
 * @author mahongliang
 * @date  2018年8月18日 下午2:10:48
 * @Description 
 * @version
 */
@Service
public class WXPayApiImpl implements WXPayApi {
	private static final Logger logger = LoggerFactory.getLogger(WXPayApiImpl.class);
	//默认签名算法
	private SignType signType = SignType.MD5;
	//上报，agent
    private boolean autoReport = false;
    //沙箱环境
    private boolean useSandbox = false;
    
    @Autowired
    private WXPayRequest wxPayRequest;
	@Autowired
    private PayConfig config;
	
	@Override
	public PayPreOrderResponseDTO unifiedOrder(PayPreOrderDTO payPreOrderDTO) {
        if(config != null) {
            payPreOrderDTO.setNotifyUrl(config.getPayNotifyUrl());
        }
		//填充配置参数
		payPreOrderDTO = (PayPreOrderDTO)this.fillRequestDTO(payPreOrderDTO);
        payPreOrderDTO.setSign(this.generateSign(payPreOrderDTO));
		//bean转map
        Map<String, String> reqData = BeanAndXmlUtil.beanToMap(payPreOrderDTO);
        //统一下单，respXml为响应参数
        String respXml = this.unifiedOrder(reqData, config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
        //响应参数验证，转为map
        Map<String, String> respData = this.processResponseXml(respXml);
        PayPreOrderResponseDTO PayPreOrderResponseDTO = null;
		try {
			PayPreOrderResponseDTO = BeanAndXmlUtil.mapToBean(respData, PayPreOrderResponseDTO.class);
		} catch (Exception e) {
			logger.error("预支付时，响应参数解析失败", e);
			throw new BusinessException(3400906, "预支付时，响应参数解析失败");
		}
        
        return PayPreOrderResponseDTO;
    }

    @Override
    public PayRefundResponseDTO refundOder(PayRefundDTO payRefundDTO) {

        if(config != null) {
            payRefundDTO.setNotifyUrl(config.getRefundNotifyUrl());
        }
        //组装公共属性
        payRefundDTO = (PayRefundDTO)this.fillRequestDTO(payRefundDTO);
        payRefundDTO.setSign(this.generateSign(payRefundDTO));
        //bean转map
        Map<String, String> reqData = BeanAndXmlUtil.beanToMap(payRefundDTO);
        //申请退款，respXml为响应参数
        String respXml = this.refund(reqData, config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
        //响应参数验证，转为map
        Map<String, String> respData = this.processResponseXml(respXml);
        PayRefundResponseDTO responseDTO = null;
        try {
            responseDTO = BeanAndXmlUtil.mapToBean(respData, PayRefundResponseDTO.class);
        } catch (Exception e) {
            logger.error("申请退款时，响应参数解析失败", e);
            throw new BusinessException(3400906, "申请退款时，响应参数解析失败");
        }
        return responseDTO;
    }
	
	/**
     * 添加 appid、mch_id、nonce_str、sign_type
     * 该函数适用于商户适用于统一下单、退款等接口，不适用于红包、代金券接口
     *
     * @param requestBase
     * @return
     * @throws Exception
     */
	private RequestBase fillRequestDTO(RequestBase requestBase){
        requestBase.setAppid(config.getAppID());
        requestBase.setMchId(config.getMchID());
        requestBase.setNonceStr(WXPayUtil.generateNonceStr());
        if (SignType.MD5.equals(this.signType)) {
            requestBase.setSignType(WXPayConstants.MD5);
        }
        else if (SignType.HMACSHA256.equals(this.signType)) {
            requestBase.setSignType(WXPayConstants.HMACSHA256);
        }
        return requestBase;
    }
	
	/**
	 * 
	 * @author mahongliang
	 * @date  2018年8月18日 下午2:59:30
	 * @Description 
	 * @param reqData
	 * @param connectTimeoutMs
	 * @param readTimeoutMs
	 * @return
	 */
	private String unifiedOrder(Map<String, String> reqData,  int connectTimeoutMs, int readTimeoutMs) {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_UNIFIEDORDER_URL_SUFFIX;
        }
        else {
            url = WXPayConstants.UNIFIEDORDER_URL_SUFFIX;
        }
        return this.requestWithoutCert(url, reqData, connectTimeoutMs, readTimeoutMs);
    }
	
	/**
	 * 微信统一签名方法
	 * @author mahongliang
	 * @date  2018年8月18日 下午6:25:02
	 * @Description 
	 * @param obj
	 * @return
	 */
	@Override
	public String generateSign(Object obj){
		String sign = null;
		//bean转map
        Map<String, String> reqData = BeanAndXmlUtil.beanToMap(obj);
        try {
        	//签名添加调用微信API入参
        	sign = WXPayUtil.generateSignature(reqData, config.getKey(), this.signType);
		} catch (Exception e) {
			logger.error("签名失败", e);
			throw new BusinessException(3400901, "签名失败");
		}
        return sign;
    }
	
	/**
     * 不需要证书的请求
     * @param urlSuffix String
     * @param reqData 向wxpay post的请求数据
     * @param connectTimeoutMs 超时时间，单位是毫秒
     * @param readTimeoutMs 超时时间，单位是毫秒
     * @return API返回数据
     * @throws Exception
     */
    private String requestWithoutCert(String urlSuffix, Map<String, String> reqData,
                                     int connectTimeoutMs, int readTimeoutMs) {
        String msgUUID = reqData.get("nonce_str");
        String reqBody = this.mapToXml(reqData);

        String resp = null;
		try {
			resp = this.wxPayRequest.requestWithoutCert(urlSuffix, msgUUID, reqBody, connectTimeoutMs, readTimeoutMs, autoReport);
		} catch (Exception e) {
			logger.error("请求微信支付失败", e);
			throw new BusinessException(3400910, "请求微信支付失败");
		}
        return resp;
    }
    
    /**
     * 将Map转换为XML格式的字符串
     * @author mahongliang
     * @date  2018年8月18日 上午11:39:39
     * @Description 
     * @param reqData
     * @return
     */
    private String mapToXml(Map<String, String> reqData) {
    	String reqBody = null;
    	try {
			reqBody = WXPayUtil.mapToXml(reqData);
		} catch (Exception e) {
			logger.error("将Map转换为XML格式的字符串出错", e);
			throw new BusinessException(3400902, "微信支付参数异常");
		}
    	return reqBody;
    }
    /**
     *  XML格式字符串转换为Map
     * @author mahongliang
     * @date  2018年8月18日 上午11:51:32
     * @Description 
     * @param xmlStr
     * @return
     */
    private Map<String, String> xmlToMap(String xmlStr) {
    	Map<String, String> respData = null;
		try {
			respData = WXPayUtil.xmlToMap(xmlStr);
		} catch (Exception e) {
			logger.error("将Map转换为XML格式的字符串出错", e);
			throw new BusinessException(3400903, "微信返回参数解析异常");
		}
    	
    	return respData;
    }
    
    /**
     * 处理 HTTPS API返回数据，转换成Map对象。return_code为SUCCESS时，验证签名。
     * @param xmlStr API返回的XML格式数据
     * @return Map类型数据
     * @throws Exception
     */
    public Map<String, String> processResponseXml(String xmlStr) {
        String RETURN_CODE = "return_code";
        String return_code;
        Map<String, String> respData = this.xmlToMap(xmlStr);
        //验签一致性校验
        boolean success = this.isResponseSignatureValid(respData);
        if(!success) {
        	logger.error("微信支付返回验签失败");
			throw new BusinessException(3400905, "微信API返回验签失败");
        }
        if (!respData.containsKey(RETURN_CODE)) {
        	logger.error("No `return_code` in XML: %s", xmlStr);
			throw new BusinessException(3400904, "微信API返回值错误");
            
        }
        return_code = respData.get(RETURN_CODE);
        if (!return_code.equals(WXPayConstants.FAIL) || !return_code.equals(WXPayConstants.SUCCESS)) {
        	logger.error("return_code value %s is invalid in XML: %s", return_code, xmlStr);
			throw new BusinessException(3400904, "微信API返回值错误");
        }

        return respData;
    }
    
    /**
     * 判断xml数据的sign是否有效，必须包含sign字段，否则返回false。
     *
     * @param reqData 向wxpay post的请求数据
     * @return 签名是否有效
     * @throws Exception
     */
    private boolean isResponseSignatureValid(Map<String, String> reqData) {
        // 返回数据的签名方式和请求中给定的签名方式是一致的
        try {
			return WXPayUtil.isSignatureValid(reqData, this.config.getKey(), this.signType);
		} catch (Exception e) {
			logger.error("微信支付返回验签失败", e);
			throw new BusinessException(3400905, "微信支付返回验签失败");
		}
    }

	/**
	 * 申请退款
	 * @param reqData
	 * @param connectTimeoutMs
	 * @param readTimeoutMs
	 * @return
	 */
	private String refund(Map<String, String> reqData,  int connectTimeoutMs, int readTimeoutMs) {
		String url;
		if (this.useSandbox) {
			url = WXPayConstants.SANDBOX_REFUND_URL_SUFFIX;
		}
		else {
			url = WXPayConstants.REFUND_URL_SUFFIX;
		}
		return this.requestWithCert(url, reqData, connectTimeoutMs, readTimeoutMs);
	}

	/**
	 * 需要证书的请求
	 * @param urlSuffix String
	 * @param reqData 向wxpay post的请求数据  Map
	 * @param connectTimeoutMs 超时时间，单位是毫秒
	 * @param readTimeoutMs 超时时间，单位是毫秒
	 * @return API返回数据
	 * @throws Exception
	 */
	private String requestWithCert(String urlSuffix, Map<String, String> reqData,
								  int connectTimeoutMs, int readTimeoutMs){
		String msgUUID= reqData.get("nonce_str");
		String reqBody = this.mapToXml(reqData);

		String resp = null;
		try {
			resp = this.wxPayRequest.requestWithCert(urlSuffix, msgUUID, reqBody, connectTimeoutMs, readTimeoutMs, this.autoReport);
		} catch (Exception e) {
			logger.error("请求微信退款", e);
			throw new BusinessException(3400910, "请求微信退款失败");
		}
		return resp;
	}
}
