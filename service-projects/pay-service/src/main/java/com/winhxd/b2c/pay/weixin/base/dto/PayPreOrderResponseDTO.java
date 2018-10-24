package com.winhxd.b2c.pay.weixin.base.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一下单入参
 * @author mahongliang
 * @date  2018年8月15日 下午3:29:57
 * @Description 
 * @version
 */
@Data
public class PayPreOrderResponseDTO extends ResponseBase implements Serializable {
	private static final long serialVersionUID = -6751906874587493059L;
	
	/**
	 * 业务结果
	 */
	private String resultCode;
	
	/**
	 * 错误代码
	 */
	private String errCode;
	
	/**
	 * 错误代码描述
	 */
	private String errCodeDes;
	
	/**
	 * 交易类型
	 */
	private String tradeType;
	
	/**
	 * 预支付交易会话标识
	 */
	private String prepayId;
	
	/**
	 * trade_type为NATIVE时有返回，用于生成二维码，展示给用户进行扫码支付
	 */
	private String code_url;

}
