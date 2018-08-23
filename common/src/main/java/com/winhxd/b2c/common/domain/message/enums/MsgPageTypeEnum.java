package com.winhxd.b2c.common.domain.message.enums;

/**
 * @author jujinbiao
 * @className MiniMsgTypeEnum 云信消息，页面类型/消息跳转类型枚举
 * @description
 */
public enum MsgPageTypeEnum {
    /**
     * 跳转到订单详情页面
     */
    ORDER_DETAIL((short) 15, "跳转到订单详情页面"),

    /**
     * 消息通知，无跳转
     */
    NOTICE((short) 16, "消息通知，无跳转");

    private short pageType;
    private String typeDesc;

    MsgPageTypeEnum(short pageType, String typeDesc) {
        this.pageType = pageType;
        this.typeDesc = typeDesc;
    }

    public short getPageType() {
        return pageType;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public static MsgPageTypeEnum getMsgPageTypeEnumByPageType(short pageType) {
        for (int i = 0; i < MsgPageTypeEnum.values().length; i++) {
            if (MsgPageTypeEnum.values()[i].getPageType() == pageType) {
                return MsgPageTypeEnum.values()[i];
            }
        }
        return null;
    }
    public static String getDescByType(Short pageType) {
        if (null != pageType) {
            for (MsgPageTypeEnum msgTypeEnum : MsgPageTypeEnum.values()) {
                if (pageType == msgTypeEnum.getPageType()) {
                    return msgTypeEnum.getTypeDesc();
                }
            }
        }
        return null;
    }
}
