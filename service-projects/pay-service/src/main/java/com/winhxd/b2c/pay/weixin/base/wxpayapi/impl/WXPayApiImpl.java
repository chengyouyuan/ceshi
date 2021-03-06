package com.winhxd.b2c.pay.weixin.base.wxpayapi.impl;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.pay.vo.PayPreOrderVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.pay.weixin.base.config.PayConfig;
import com.winhxd.b2c.pay.weixin.base.dto.*;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayApi;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayConstants;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayConstants.SignType;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayRequest;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayUtil;
import com.winhxd.b2c.pay.weixin.util.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
	/**
	 * 默认签名算法
	 */
	private SignType signType = SignType.MD5;
	/**
	 * 上报，agent
	 */
    private boolean autoReport = false;
	/**
	 * 沙箱环境
	 */
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
		//签名
        payPreOrderDTO.setSign(this.generateSign(payPreOrderDTO));
		//bean转map
        Map<String, String> reqData = null;
		try {
			reqData = XmlUtil.bean2MapUnderline2Hump(payPreOrderDTO);
			logger.info("预支付时1，请求参数reqData：{}", reqData);
		} catch (Exception e) {
			logger.error("预支付时，请求参数解析失败", e);
			throw new BusinessException(BusinessCode.CODE_340006, "预支付时，请求参数解析失败");
		}
        //统一下单，respXml为响应参数
        String respXml = this.unifiedOrder(reqData, config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
        logger.info("预支付时，响应参数：{}", respXml);
        //响应参数验证，转为map
        Map<String, String> respData = this.processResponseXml(respXml);
        PayPreOrderResponseDTO payPreOrderResponseDTO = null;
		try {
			payPreOrderResponseDTO = XmlUtil.map2Bean(respData, PayPreOrderResponseDTO.class);
		} catch (Exception e) {
			logger.error("预支付时，响应参数解析失败", e);
			throw new BusinessException(BusinessCode.CODE_340006, "预支付时，响应参数解析失败");
		}
        
        return payPreOrderResponseDTO;
    }

    @Override
    public PayRefundResponseDTO refundOder(PayRefundDTO payRefundDTO) {
		logger.info("开始转换入参：");
        if(config != null) {
            payRefundDTO.setNotifyUrl(config.getRefundNotifyUrl());
        }
        //组装公共属性
        payRefundDTO = (PayRefundDTO)this.fillRequestDTO(payRefundDTO);
		logger.info("开始转换入参dto："+JsonUtil.toJSONString(payRefundDTO));
		payRefundDTO.setSign(this.generateSign(payRefundDTO));
        //bean转map
		Map<String, String> reqData = null;
		try {
			reqData = XmlUtil.bean2MapUnderline2Hump(payRefundDTO);
			logger.info("开始转换入参map："+JsonUtil.toJSONString(reqData));
		} catch (Exception e) {
			logger.error("申请退款时，请求参数解析失败", e);
			throw new BusinessException(BusinessCode.CODE_340006, "申请退款时，响应参数解析失败");
		}
        //申请退款，respXml为响应参数
        String respXml = this.refund(reqData, config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
		logger.info("退款返回数据是："+ respXml);
        //响应参数验证，转为map
		logger.info("开始转换返参：");
        Map<String, String> respData = this.processResponseXml(respXml);
        logger.info("返参转换成map结束"+ JsonUtil.toJSONString(respData));
        PayRefundResponseDTO responseDTO = new PayRefundResponseDTO();
        try {
            responseDTO = XmlUtil.map2Bean(respData, PayRefundResponseDTO.class);
            logger.info("返参转换成dto结束："+JsonUtil.toJSONString(responseDTO));
        } catch (Exception e) {
            logger.error("申请退款时，响应参数解析失败", e);
            throw new BusinessException(BusinessCode.CODE_340006, "申请退款时，响应参数解析失败");
        }
        return responseDTO;
    }
    
    /**
     * 判断支付结果通知中的sign是否有效
     *
     * @param reqData 向wxpay post的请求数据
     * @return 签名是否有效
     * @throws Exception
     */
    public boolean isPayResultNotifySignatureValid(Map<String, String> reqData) throws Exception {
        String signTypeInData = reqData.get(WXPayConstants.FIELD_SIGN_TYPE);
        SignType signType;
        if (signTypeInData == null) {
            signType = SignType.MD5;
        }
        else {
            signTypeInData = signTypeInData.trim();
            if (signTypeInData.length() == 0) {
                signType = SignType.MD5;
            }
            else if (WXPayConstants.MD5.equals(signTypeInData)) {
                signType = SignType.MD5;
            }
            else if (WXPayConstants.HMACSHA256.equals(signTypeInData)) {
                signType = SignType.HMACSHA256;
            }
            else {
                throw new Exception(String.format("Unsupported sign_type: %s", signTypeInData));
            }
        }
        return WXPayUtil.isSignatureValid(reqData, this.config.getKey(), signType);
    }
    
    /**
     * 作用：查询订单<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     * @throws Exception
     */
    @Override
    public PayPreOrderCallbackDTO orderQuery(PayOrderQueryDTO payOrderQueryDTO) {
    	payOrderQueryDTO = (PayOrderQueryDTO) this.fillRequestDTO(payOrderQueryDTO);
    	payOrderQueryDTO.setSign(this.generateSign(payOrderQueryDTO));
    	PayPreOrderCallbackDTO payPreOrderCallbackDTO = null;
    	try {
    		//请求参数
        	Map<String, String> resqData = XmlUtil.bean2MapUnderline2Hump(payOrderQueryDTO);
        	//返回参数
        	Map<String, String> respData = this.orderQuery(resqData, config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
        	
        	payPreOrderCallbackDTO = XmlUtil.map2Bean(respData, PayPreOrderCallbackDTO.class);
    	} catch(Exception e) {
    		logger.error("主动查询订单支付状态，参数解析失败", e);
            throw new BusinessException(BusinessCode.CODE_340006, "主动查询订单支付状态，参数解析失败");
    	}
    	return payPreOrderCallbackDTO;
    }

    /**
     * 作用：查询订单<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付
     * @param reqData 向wxpay post的请求数据 int
     * @param connectTimeoutMs 连接超时时间，单位是毫秒
     * @param readTimeoutMs 读超时时间，单位是毫秒
     * @return API返回数据
     * @throws Exception
     */
    private Map<String, String> orderQuery(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_ORDERQUERY_URL_SUFFIX;
        }
        else {
            url = WXPayConstants.ORDERQUERY_URL_SUFFIX;
        }
        String respXml = this.requestWithoutCert(url, reqData, connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml);
    }

	@Override
	public Map<String, String> refundQuery(PayRefundDTO payRefundDTO) {
		//组装公共属性
		payRefundDTO = (PayRefundDTO)this.fillRequestDTO(payRefundDTO);
		payRefundDTO.setSign(this.generateSign(payRefundDTO));
		//bean转map
		Map<String, String> reqData = null;
		try {
			reqData = XmlUtil.bean2MapUnderline2Hump(payRefundDTO);
		} catch (Exception e) {
			logger.error("查询退款时，请求参数解析失败", e);
			throw new BusinessException(BusinessCode.CODE_340006, "查询退款时，请求参数解析失败");
		}
		String respXml = this.refundQuery(reqData, config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs());
		//响应参数验证，转为map
		Map<String, String> respData = this.processResponseXml(respXml);
		return respData;
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
	 * 添加 appid、mch_id、nonce_str、sign_type
	 * 该函数适用于 签名方式为HMACSHA256 接口
	 * 
	 * @param requestBase
	 * @return
	 * @throws Exception
	 */
	private RequestBase fillRequestDTOByHMACSHA256(RequestBase requestBase){
		requestBase.setAppid(config.getAppID());
		requestBase.setMchId(config.getMchID());
		requestBase.setNonceStr(WXPayUtil.generateNonceStr());
		requestBase.setSignType(WXPayConstants.HMACSHA256);
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
	
	@Override
	public String generateSign(Object obj){
        return this.generateSign(obj, signType);
    }
	
	@Override
	public String payPreOrderSign(PayPreOrderVO payPreOrderVO){
		String sign = null;
        try {
            //bean转map
		    Map<String, String> reqData = XmlUtil.bean2Map(payPreOrderVO);
		    WXPayUtil.getLogger().warn("-----签名数据AAA------"+reqData);
        	//签名添加调用微信API入参
        	sign = WXPayUtil.generateSignature(reqData, config.getKey(), signType);
		} catch (Exception e) {
			logger.error("签名失败", e);
			throw new BusinessException(BusinessCode.CODE_340001, "生产签名失败");
		}
        return sign;
	}
	
	@Override
	public String generateSign(Object obj, SignType signType){
		if(signType == null) {
			signType = this.signType;
		}
		String sign = null;
        try {
            //bean转map
		    Map<String, String> reqData = XmlUtil.bean2MapUnderline2Hump(obj);
		    WXPayUtil.getLogger().warn("-----签名数据AAA------"+reqData);
        	//签名添加调用微信API入参
        	sign = WXPayUtil.generateSignature(reqData, config.getKey(), signType);
		} catch (Exception e) {
			logger.error("签名失败", e);
			throw new BusinessException(BusinessCode.CODE_340001, "生产签名失败");
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
        logger.info("请求微信支付参数：{}", reqBody);
        String resp = null;
		try {
			resp = this.wxPayRequest.requestWithoutCert(urlSuffix, msgUUID, reqBody, connectTimeoutMs, readTimeoutMs, autoReport);
		} catch (Exception e) {
			logger.error("请求微信支付失败", e);
			throw new BusinessException(BusinessCode.CODE_340010, "微信无证书请求失败");
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
			reqBody = XmlUtil.mapToXml(reqData);
		} catch (Exception e) {
			logger.error("将Map转换为XML格式的字符串出错", e);
			throw new BusinessException(BusinessCode.CODE_340002, "微信请求参数转换异常");
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
			respData = XmlUtil.xmlToMap(xmlStr);
		} catch (Exception e) {
			logger.error("将Map转换为XML格式的字符串出错", e);
			throw new BusinessException(BusinessCode.CODE_340003, "微信响应参数解析异常");
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
        if (!respData.containsKey(RETURN_CODE)) {
        	logger.error("No `return_code` in XML: %s", xmlStr);
			throw new BusinessException(BusinessCode.CODE_340004, "微信响应值错误");
        }
        return_code = respData.get(RETURN_CODE);
		if (return_code.equals(WXPayConstants.FAIL)) {
			return respData;
		} else if (return_code.equals(WXPayConstants.SUCCESS)) {
			//验签一致性校验
			boolean success = this.isResponseSignatureValid(respData);
			if(!success) {
				logger.error("微信支付返回验签失败");
				throw new BusinessException(BusinessCode.CODE_340005, "微信响应验签失败");
			}
			return respData;
		} else{
			logger.error("return_code value %s is invalid in XML: %s", return_code, xmlStr);
			throw new BusinessException(BusinessCode.CODE_340004, "微信响应值错误");
		}
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
			throw new BusinessException(BusinessCode.CODE_340005, "微信响应验签失败");
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
	 * 作用：退款查询<br>
	 * 场景：刷卡支付、公共号支付、扫码支付、APP支付
	 * @param reqData 向wxpay post的请求数据
	 * @param connectTimeoutMs 连接超时时间，单位是毫秒
	 * @param readTimeoutMs 读超时时间，单位是毫秒
	 * @return API返回数据
	 * @throws Exception
	 */
	private String refundQuery(Map<String, String> reqData, int connectTimeoutMs, int readTimeoutMs){
		String url;
		if (this.useSandbox) {
			url = WXPayConstants.SANDBOX_REFUNDQUERY_URL_SUFFIX;
		}
		else {
			url = WXPayConstants.REFUNDQUERY_URL_SUFFIX;
		}
		return this.requestWithoutCert(url, reqData, connectTimeoutMs, readTimeoutMs);
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
			throw new BusinessException(BusinessCode.CODE_340011, "微信有证书请求失败");
		}
		return resp;
	}

    /**
     * 作用：对账单下载<br>
     * 场景：刷卡支付、公共号支付、扫码支付、APP支付<br>
     * 其他：无论是否成功都返回Map。若成功，返回的Map中含有return_code、return_msg、data，
     *      其中return_code为`SUCCESS`，data为对账单数据。
     * @param reqData 向wxpay post的请求数据
     * @param connectTimeoutMs 连接超时时间，单位是毫秒
     * @param readTimeoutMs 读超时时间，单位是毫秒
     * @return 经过封装的API返回数据
     * @throws Exception
     */
    @Override
    public PayBillDownloadResponseDTO downloadBill(PayStatementDTO payStatementDTO) {
    	
		//填充配置参数
		this.fillRequestDTO(payStatementDTO);
        payStatementDTO.setSign(this.generateSign(payStatementDTO));
		//bean转map
        Map<String, String> reqData = null;
        try {
            reqData = XmlUtil.bean2MapUnderline2Hump(payStatementDTO);
        } catch (Exception e) {
            logger.error("下载对账单时，请求参数解析失败", e);
            throw new BusinessException(BusinessCode.CODE_340006, "下载对账单时，请求参数解析失败");
        }

        String url;
        if (this.useSandbox) {
            url = WXPayConstants.SANDBOX_DOWNLOADBILL_URL_SUFFIX;
        }
        else {
            url = WXPayConstants.DOWNLOADBILL_URL_SUFFIX;
        }
        String respStr = this.requestWithoutCert(url, reqData, config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs()).trim();
        
        PayBillDownloadResponseDTO responseDTO = new PayBillDownloadResponseDTO();
        
        Map<String, String> ret;
        try {
        	// 出现错误，返回XML数据
	        if (respStr.indexOf("<") == 0) {
	            ret = XmlUtil.xmlToMap(respStr);
	        }
	        else {
	            // 正常返回csv数据
	            ret = new HashMap<String, String>();
	            ret.put("return_code", WXPayConstants.SUCCESS);
	            ret.put("return_msg", "ok");
	            ret.put("data", respStr);
	        }
	        responseDTO = XmlUtil.map2Bean(ret, PayBillDownloadResponseDTO.class);
	    } catch (Exception e) {
	        logger.error("下载对账单时，响应参数解析失败", e);
	        throw new BusinessException(BusinessCode.CODE_340007, "下载对账单时，响应参数解析失败");
	    }
        return responseDTO;
    }
    
    /**
     * 作用：资金账单下载<br>
     * 场景：资金账单中的数据反映的是商户微信账户资金变动情况 <br>
     * 其他：无论是否成功都返回Map。若成功，返回的Map中含有return_code、return_msg、data，
     *      其中return_code为`SUCCESS`，data为资金账单数据。
     * @param reqData 向wxpay post的请求数据
     * @param connectTimeoutMs 连接超时时间，单位是毫秒
     * @param readTimeoutMs 读超时时间，单位是毫秒
     * @return 经过封装的API返回数据
     * @throws Exception
     */
    @Override
    public PayBillDownloadResponseDTO downloadFundFlow(PayFinancialBillDTO payFinancialBillDTO) {

		//填充配置参数
		this.fillRequestDTOByHMACSHA256(payFinancialBillDTO);
        payFinancialBillDTO.setSign(this.generateSign(payFinancialBillDTO, SignType.HMACSHA256));
		//bean转map
        Map<String, String> reqData = null;
        try {
            reqData = XmlUtil.bean2MapUnderline2Hump(payFinancialBillDTO);
        } catch (Exception e) {
            logger.error("下载资金对账单时，请求参数解析失败", e);
            throw new BusinessException(BusinessCode.CODE_340006, "下载资金对账单时，请求参数解析失败");
        }

        String url;
    	if (this.useSandbox) {
    		url = WXPayConstants.SANDBOX_DOWNLOADFUNDFLOW_URL_SUFFIX;
    	}
    	else {
    		url = WXPayConstants.DOWNLOADFUNDFLOW_URL_SUFFIX;
    	}
    	String respStr = this.requestWithCert(url, reqData, config.getHttpConnectTimeoutMs(), this.config.getHttpReadTimeoutMs()).trim();

        PayBillDownloadResponseDTO responseDTO = new PayBillDownloadResponseDTO();
        
        Map<String, String> ret;

        try {
	        // 出现错误，返回XML数据
	        if (respStr.indexOf("<") == 0) {
	            ret = XmlUtil.xmlToMap(respStr);
	        }
	        else {
	            // 正常返回csv数据
	            ret = new HashMap<String, String>();
	            ret.put("return_code", WXPayConstants.SUCCESS);
	            ret.put("return_msg", "ok");
	            ret.put("data", respStr);
	        }
	        responseDTO = XmlUtil.map2Bean(ret, PayBillDownloadResponseDTO.class);
        } catch (Exception e) {
            logger.error("下载资金账单时，响应参数解析失败", e);
            throw new BusinessException(BusinessCode.CODE_340007, "下载资金账单时，响应参数解析失败");
        }
        return responseDTO;
        
    }

	@Override
	public String transferToChange(Map<String, String> reqData) {
		String url;
		if (this.useSandbox) {
			url = WXPayConstants.SANDBOX_TRANSFER_TO_CHANGE_URL_SUFFIX;
		} else {
			url = WXPayConstants.TRANSFER_TO_CHANGE_URL_SUFFIX;
		}
		return this.requestWithCert(url, reqData, config.getHttpConnectTimeoutMs(), config.getHttpReadTimeoutMs());
	}

	@Override
	public String queryTransferToChange(Map<String, String> reqData) {
		String url;
		if (this.useSandbox) {
			url = WXPayConstants.SANDBOX_QUERY_TRANSFER_TO_CHANGE_URL_SUFFIX;
		} else {
			url = WXPayConstants.QUERY_TRANSFER_TO_CHANGE_URL_SUFFIX;
		}
		return this.requestWithCert(url, reqData, config.getHttpConnectTimeoutMs(), config.getHttpReadTimeoutMs());
	}

	@Override
	public String transferToBank(Map<String, String> reqData) {
		String url;
		if (this.useSandbox) {
			url = WXPayConstants.SANDBOX_TRANSFER_TO_BANK_URL_SUFFIX;
		} else {
			url = WXPayConstants.TRANSFER_TO_BANK_URL_SUFFIX;
		}
		return this.requestWithCert(url, reqData, config.getHttpConnectTimeoutMs(), config.getHttpReadTimeoutMs());
	}

	@Override
	public String queryTransferToBank(Map<String, String> reqData) {
		String url;
		if (this.useSandbox) {
			url = WXPayConstants.SANDBOX_QUERY_TRANSFER_TO_BANK_URL_SUFFIX;
		} else {
			url = WXPayConstants.QUERY_TRANSFER_TO_BANK_URL_SUFFIX;
		}
		return this.requestWithCert(url, reqData, config.getHttpConnectTimeoutMs(), config.getHttpReadTimeoutMs());
	}

	@Override
	public String publicKey(Map<String, String> reqData) {
		String url;
		if (this.useSandbox) {
			url = WXPayConstants.SANDBOX_PUBLICKEY_URL_SUFFIX;
		} else {
			url = WXPayConstants.PUBLICKEY_URL_SUFFIX ;
		}
		return this.requestWithCert(url, reqData, 6*1000, 8*1000);
	}

}
