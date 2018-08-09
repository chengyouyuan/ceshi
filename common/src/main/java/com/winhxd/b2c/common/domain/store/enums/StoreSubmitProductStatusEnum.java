package com.winhxd.b2c.common.domain.store.enums;
/**
 * 
 * @ClassName: StoreSubmitProductStatusEnum 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月8日 下午7:23:59
 */
public enum StoreSubmitProductStatusEnum {
	CREATE((byte)0,"待审核"),
	PASS((byte)1,"审核通过"),
	NOTPASS((byte)2,"未通过"),
	ADDPROD((byte)3,"添加到商品库");
	private byte statusCode;
    private String  statusDes;
	public byte getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(byte statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusDes() {
		return statusDes;
	}
	public void setStatusDes(String statusDes) {
		this.statusDes = statusDes;
	}
	private StoreSubmitProductStatusEnum(byte statusCode, String statusDes) {
		this.statusCode = statusCode;
		this.statusDes = statusDes;
	}
    
    
}
