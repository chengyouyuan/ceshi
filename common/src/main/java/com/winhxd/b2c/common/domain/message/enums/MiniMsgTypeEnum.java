package com.winhxd.b2c.common.domain.message.enums;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author jujinbiao
 * @className MiniMsgTypeEnum 小程序模板消息类型
 * @description
 */
public enum MiniMsgTypeEnum {
    /**
     * 门店确认订单
     */
    STORE_CONFIRM_ORDER((short) 1, "门店确认订单", "9P9K-qlU8TQPuSDjuYLlNjhd6djZFP8J0uoBQfkAfNs"),

    /**
     * 用户取消订单
     */
    USER_CANCEL_ORDER((short) 2, "用户取消订单", ""),
    /**
     * 门店确认退款
     */
    STORE_CONFIRM_REFUND((short) 3, "门店确认退款", ""),

    /**
     * 门店超时未确认
     */
    STORE_NOT_CONFIRM_TIMEOUT((short) 4, "门店超时未确认", ""),
    /**
     * 门店取消订单
     */
    STORE_CANCEL_ORDER((short) 5, "门店取消订单", ""),
    /**
     * 用户超时未取货且未付款
     */
    USER_TIMEOUT_NO_GOODS_NO_PAYMENT((short) 6, "用户超时未取货且未付款", ""),
    /**
     * 用户超时未取货但已付款
     */
    USER_TIMEOUT_NO_GOODS_PAID((short) 7, "用户超时未取货但已付款", ""),
    /**
     * 用户取货完成
     */
    USER_PICK_UP_GOODS((short) 8, "用户取货完成", ""),
    /**
     * 待自提订单还有一小时失效
     */
    ORDER_INVALID_ONE_HOUR((short) 9, "待自提订单还有一小时失效", "");

    private short msgType;
    private String msgDesc;
    private String templateId;

    MiniMsgTypeEnum(short msgType, String msgDesc, String templateId) {
        this.msgType = msgType;
        this.msgDesc = msgDesc;
        this.templateId = templateId;
    }

    public short getMsgType() {
        return msgType;
    }

    public String getMsgDesc() {
        return msgDesc;
    }

    public String getTemplateId(){
        return templateId;
    }

    public static MiniMsgTypeEnum getMiniMsgTypeEnumByMsgType(short msgType) {
        for (int i = 0; i < MiniMsgTypeEnum.values().length; i++) {
            if (MiniMsgTypeEnum.values()[i].getMsgType() == msgType) {
                return MiniMsgTypeEnum.values()[i];
            }
        }
        return null;
    }
    public static String getDescByType(Short msgType) {
        if (null != msgType) {
            for (MiniMsgTypeEnum miniMsgTypeEnum : MiniMsgTypeEnum.values()) {
                if (msgType == miniMsgTypeEnum.getMsgType()) {
                    return miniMsgTypeEnum.getMsgDesc();
                }
            }
        }
        return null;
    }

    public static Map<Short, String> getDescMap() {
        Map<Short, String> map = new TreeMap<>();
        for (MiniMsgTypeEnum miniMsgTypeEnum : values()) {
            map.put(miniMsgTypeEnum.getMsgType(), miniMsgTypeEnum.getMsgDesc());
        }
        return map;
    }
}
