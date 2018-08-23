package com.winhxd.b2c.pay.weixin.base.dto;

import java.io.Serializable;

/**
 * 统一下单入参
 * @author mahongliang
 * @date  2018年8月15日 下午3:29:57
 * @Description 
 * @version
 */
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

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrCodeDes() {
		return errCodeDes;
	}

	public void setErrCodeDes(String errCodeDes) {
		this.errCodeDes = errCodeDes;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	public String getCode_url() {
		return code_url;
	}

	public void setCode_url(String code_url) {
		this.code_url = code_url;
	}

	@Override
	public String toString() {
		return "PayPreOrderResponseDTO{" +
				"resultCode='" + resultCode + '\'' +
				", errCode='" + errCode + '\'' +
				", errCodeDes='" + errCodeDes + '\'' +
				", tradeType='" + tradeType + '\'' +
				", prepayId='" + prepayId + '\'' +
				", code_url='" + code_url + '\'' +
				'}';
	}
}
