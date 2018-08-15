package com.winhxd.b2c.pay.weixin.condition;

/**
 * @author lizhonghua
 * @date  2018年8月14日11:09:35
 * @Description 微信退款
 * @version 
 */
public class PayRefundCondition {

	/**
	 * 公众账号ID
	 */
	private String appid;

	/**
	 * 商户号
	 */
	private String mchId;

	/**
	 * 商户订单号
	 */
	private String outTradeNo;

	/**
	 * 订单金额
	 */
	private Integer totalFee;

	/**
	 * 退款金额
	 */
	private Integer refundFee;

    /**
     * 退款原因
     */
	private String refundDesc;

    /**
     * 退款回调URL
     */
	private String notifyUrl;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public Integer getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}

	public Integer getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(Integer refundFee) {
		this.refundFee = refundFee;
	}

	public String getRefundDesc() {
        return refundDesc;
    }

    public void setRefundDesc(String refundDesc) {
        this.refundDesc = refundDesc;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}
