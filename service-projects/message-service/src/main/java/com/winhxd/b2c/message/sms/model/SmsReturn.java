package com.winhxd.b2c.message.sms.model;

import com.winhxd.b2c.message.sms.enums.InternationalStatusEnum;
import com.winhxd.b2c.message.sms.enums.SmsReturnStatusEnum;
import com.winhxd.b2c.message.sms.enums.SmsSupplierEnum;

/**
 * @ClassName: SmsReturn
 * @Description: 返回短信的实体
 * @Author fanzhanzhan
 * @Date 2018-08-20 10:43
 **/
public class SmsReturn {

	/**
	 * 返回状态,详情参见SmsStatusEnum
	 */
	private SmsReturnStatusEnum status;
	/**
	 * 返回状态,详情参见SmsStatusEnum
	 */
	private InternationalStatusEnum internationalstatus;
	/**
	 * 短信发送完成时间
	 */
	private String finishTime;

	/**
	 * 状态报告id(发送失败时为null)
	 */
	private String msgid;

	/**
	 * 短信供应商
	 */
	private SmsSupplierEnum smsSupplier;

	public SmsReturnStatusEnum getStatus() {
		return status;
	}

	public void setStatus(SmsReturnStatusEnum status) {
		this.status = status;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public SmsSupplierEnum getSmsSupplier() {
		return smsSupplier;
	}

	public void setSmsSupplier(SmsSupplierEnum smsSupplier) {
		this.smsSupplier = smsSupplier;
	}

	public InternationalStatusEnum getInternationalstatus() {
		return internationalstatus;
	}

	public void setInternationalstatus(InternationalStatusEnum internationalstatus) {
		this.internationalstatus = internationalstatus;
	}

}
