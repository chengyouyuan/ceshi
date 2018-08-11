package com.winhxd.b2c.pay.weixin.base.dto;

import java.io.Serializable;

public class ResponseBase implements Serializable {
	private static final long serialVersionUID = 2755651634569976638L;
	
	public static final String SUCCESS = "SUCCESS";
	public static final String FAIL = "FAIL";
	
    //错误码
    private String returnCode;
    //错误描述
    private String returnMsg;
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

}
