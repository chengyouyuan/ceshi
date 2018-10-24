package com.winhxd.b2c.pay.weixin.base.dto;

import lombok.Data;

/**
 * 请求公共参数
 * @author mahongliang
 * @date  2018年8月18日 下午3:23:17
 * @Description 
 * @version
 */
@Data
public class RequestBase {
	
	/**
	 * 小程序ID
	 */
	private String appid;
	
	/**
	 * 商户号
	 */
	private String mchId;
	
	/**
	 * 随机字符串
	 */
	private String nonceStr;
	
	/**
	 * 签名
	 */
	private String sign;
	
	/**
	 * 签名类型，默认为MD5，支持HMAC-SHA256和MD5
	 */
	private String signType;
	
}
