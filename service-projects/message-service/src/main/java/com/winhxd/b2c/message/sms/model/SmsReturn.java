package com.winhxd.b2c.message.sms.model;

import com.winhxd.b2c.message.sms.enums.InternationalStatusEnum;
import com.winhxd.b2c.message.sms.enums.SmsReturnStatusEnum;
import com.winhxd.b2c.message.sms.enums.SmsSupplierEnum;

/**
 * 短信发送返回结果dto
 * */
public class SmsReturn {

    private SmsReturnStatusEnum status;//返回状态,详情参见SmsStatusEnum
    private InternationalStatusEnum internationalstatus;//返回状态,详情参见SmsStatusEnum
    
    private String finishTime;// 短信发送完成时间

    private String msgid;// 状态报告id(发送失败时为null)
    
    private SmsSupplierEnum smsSupplier;// 短信供应商

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
