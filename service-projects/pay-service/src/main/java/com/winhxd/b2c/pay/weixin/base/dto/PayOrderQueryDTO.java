package com.winhxd.b2c.pay.weixin.base.dto;

/**
 * 主动查询支付结果参数
 * @author mahongliang
 * @date  2018年8月20日 上午11:15:29
 * @Description 
 * @version
 */
public class PayOrderQueryDTO extends RequestBase {
	/**
	 * 支付流水号
	 */
	private String outTradeNo;

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	@Override
	public String toString() {
		return "PayOrderQueryDTO{" +
				"outTradeNo='" + outTradeNo + '\'' +
				'}';
	}
}
