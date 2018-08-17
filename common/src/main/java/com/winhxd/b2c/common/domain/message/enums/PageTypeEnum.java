package com.winhxd.b2c.common.domain.message.enums;

import java.util.Map;
import java.util.TreeMap;

/**
 * @Author: FanZhanzhan
 * @Date: 2018-08-14 17:09
 * @Description 消息跳转类型
 * @Version
 */
public enum PageTypeEnum {

	/**
	 * 新订单
	 */
	ORDER_NEW((short) 1, "新", "新订单"),
	/**
	 * 已完成
	 */
	ORDER_COMPLETE((short) 2, "完", "已完成"),
	/**
	 * 已退款
	 */
	ORDER_REFUND((short) 3, "退", "已退款"),
	/**
	 * 已取消
	 */
	ORDER_CANCEL((short) 4, "取", "已取消"),
	/**
	 * 申请退款
	 */
	ORDER_APPLY_REFUND((short) 5, "申", "申请退款"),
	/**
	 * 提现申请
	 */
	WITHDRAW_APPLY((short) 10, "提", "提现申请"),
	/**
	 * 提现成功
	 */
	WITHDRAW_SUCCESS((short) 11, "现", "提现成功"),
	/**
	 * 提现失败
	 */
	WITHDRAW_FAIL((short) 12, "败", "提现失败"),
	/**
	 * 惠小店通知
	 */
	HUI_NOTICE((short) 13, "惠", "惠小店通知");

	private short typeCode;
	private String typeSummary;
	private String typeDesc;

	PageTypeEnum(short typeCode, String typeSummary, String typeDesc) {
		this.typeCode = typeCode;
		this.typeSummary = typeSummary;
		this.typeDesc = typeDesc;
	}

	public short getTypeCode() {
		return typeCode;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public String getTypeSummary() {
		return typeSummary;
	}

	public static String getDescByCode(Short code) {
		if (null != code) {
			for (PageTypeEnum typeEnum : PageTypeEnum.values()) {
				if (typeEnum.getTypeCode() == code) {
					return typeEnum.getTypeDesc();
				}
			}
		}
		return null;
	}

	public static String getSummaryByCode(Short code) {
		if (null != code) {
			for (PageTypeEnum typeEnum : PageTypeEnum.values()) {
				if (typeEnum.getTypeCode() == code) {
					return typeEnum.getTypeSummary();
				}
			}
		}
		return null;
	}

	public static Map<Short, Map<String, String>> getDescMap() {
		Map<Short, Map<String, String>> map = new TreeMap<>();
		for (PageTypeEnum msgTypeEnum : values()) {
			Map<String, String> descMap = new TreeMap<>();
			descMap.put(msgTypeEnum.getTypeSummary(), msgTypeEnum.getTypeDesc());
			map.put(msgTypeEnum.getTypeCode(), descMap);
		}
		return map;
	}
}
