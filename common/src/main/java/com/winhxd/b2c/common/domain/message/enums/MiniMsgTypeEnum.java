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
    STORE_CONFIRM_ORDER((short) 1, "门店确认订单", "-uMG1WQxIOflPKaCGyRjsWWPP5RwxlKeFzeOGQtP-fg"),

    /**
     * 订单完成
     */
    ORDER_FINISH((short) 2, "订单完成", "9mbk7J8MxY9nyRSf7r3MtmjsNugjbil8aiqUaN5ntRA"),
    /**
     * 订单取消
     */
    ORDER_CANCELED((short) 3, "订单取消", "ywS5OMp09yV12g2qXCm_fYFYD7p9OndeYnzrgy6fm4I"),

    /**
     * 支付完成
     */
    PAY_SUCCESS((short) 4, "支付完成", "aoFbgtDYcSTubZDwJgaFmpdSx9XJnWG_k7OTUY2xvlQ"),
    /**
     * 退款成功
     */
    REFUND_SUCCESS((short) 5, "退款成功", "TdCW9xGUtkhr1kKeAVe2Q5bbs5bcAPeZ6rSaGCq-OOU");

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
