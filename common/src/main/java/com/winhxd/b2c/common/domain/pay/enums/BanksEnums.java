package com.winhxd.b2c.common.domain.pay.enums;

import java.util.ArrayList;
import java.util.List;

import com.winhxd.b2c.common.domain.pay.vo.BanksVO;

public enum BanksEnums {

	/**
	 * 工商银行
	 */
	ICBC("1002","工商银行"),
	/**
	 * 农业银行
	 */
	ABC("1005","农业银行"),
	/**
	 * 中国银行
	 */
	BANK_OF_CHINA("1026","中国银行"),
	/**
	 * 招商银行
	 */
	CONSTRUCTION_BANK("1003","建设银行"),
	/**
	 * 招商银行
	 */
	CHINA_MERCHANTS_BANK("1001","招商银行"),
	/**
	 * 邮储银行
	 */
	POSTAL_BANK("1066","邮储银行"),
	/**
	 * 交通银行
	 */
	BANK_OF_COMMUNICATIONS("1020","交通银行"),
	/**
	 * 浦发银行
	 */
	SHANGHAI_PUDONG_DEVELOPMENT_BANK("1004","浦发银行"),
	/**
	 * 民生银行
	 */
	MINSHENG_BANK("1006","民生银行"),
	/**
	 * 兴业银行
	 */
	INDUSTRIAL_BANK("1009","兴业银行"),
	/**
	 * 平安银行
	 */
	PING_AN_BANK("1010","平安银行"),
	/**
	 * 中信银行
	 */
	CITIC_BANK("1021","中信银行"),
	/**
	 * 华夏银行
	 */
	HSBC_BANK("1025","华夏银行"),
	/**
	 * 广发银行
	 */
	GUANGFA_BANK("1027","广发银行"),
	/**
	 * 招商银行
	 */
	EVERBRIGHT_BANK("1022","光大银行"),
	/**
	 * 北京银行
	 */
	BANK_OF_BEIJING("1032","北京银行"),
	/**
	 * 宁波银行
	 */
	BANK_OF_NINGBO("1056","宁波银行");
	
	
	private String code;
	private String desc;
	BanksEnums(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public static List<BanksVO> getValus(){
		BanksEnums[] banksEnums=values();
		List<BanksVO> list=new ArrayList<>();
		for (int i = 0; i < banksEnums.length; i++) {
			BanksVO vo=new BanksVO();
			vo.setBankCode(banksEnums[i].getCode());
			vo.setBankName(banksEnums[i].getDesc());
			list.add(vo);
		}
		return list;
		
	}
	
}
