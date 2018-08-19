package com.winhxd.b2c.pay.weixin.base.dto;


/**
 * 下载对账单入参
 * @author yuluyuan
 *
 * 2018年8月19日
 */
public class PayStatementDTO extends RequestBase{
	
    /**
     * 对账单日期
     */
    private String billDate;
    
    /**
     * 账单类型
     */
    private String billType;
    
    /**
     * 压缩账单
     * 非必传参数，固定值：GZIP，返回格式为.gzip的压缩包账单。不传则默认为数据流形式。
     */
    private String tarType;

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getTarType() {
		return tarType;
	}

	public void setTarType(String tarType) {
		this.tarType = tarType;
	}

	public enum BillType {

		/**
		 * 返回当日所有订单信息，默认值
		 */
		ALL("ALL", "返回当日所有订单信息，默认值"),

		/**
		 * 返回当日成功支付的订单
		 */
		SUCCESS("SUCCESS", "返回当日成功支付的订单"),

		/**
		 * 返回当日退款订单
		 */
		REFUND("REFUND", "返回当日退款订单"),
		
		/**
		 * 返回当日充值退款订单
		 */
		RECHARGE_REFUND("RECHARGE_REFUND", "返回当日充值退款订单");

		private String text;
		private String desc;

		private BillType(String text, String desc) {
			this.text = text;
			this.desc = desc;
		}

		public String getText() {
			return text;
		}

		public String getDesc() {
			return desc;
		}
	}
	
}
