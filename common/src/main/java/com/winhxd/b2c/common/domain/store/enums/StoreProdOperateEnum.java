package com.winhxd.b2c.common.domain.store.enums;
/**
 * 门店商品操作枚举
 * @ClassName: StoreProdOperateEnum 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月7日 上午9:28:55
 */
public enum StoreProdOperateEnum {
	
	UNPUTAWAY((byte)0,"商品下架"),
	PUTAWAY((byte)1,"商品上架"),
	DELETE((byte)2,"商品删除"),
	EDIT((byte)3,"商品编辑");
	
	private byte operateCode;
    private String  operateDes;
    
    
	private StoreProdOperateEnum(byte operateCode, String operateDes) {
		this.operateCode = operateCode;
		this.operateDes = operateDes;
	}
	public byte getOperateCode() {
		return operateCode;
	}
	public void setOperateCode(byte operateCode) {
		this.operateCode = operateCode;
	}
	public String getOperateDes() {
		return operateDes;
	}
	public void setOperateDes(String operateDes) {
		this.operateDes = operateDes;
	}
    
    
    
    

}
