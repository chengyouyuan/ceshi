package com.winhxd.b2c.common.domain.order.enums;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author pangjianhua
 * @date 2018/8/2 19:34
 */
public enum PickUpTypeEnum {

    /**
     * 立即自提
     */
    SELF_PICK_UP_NOW((short) 1, "立即自提"),

    /**
     * 普通自提
     */
    SELF_PICK_UP_LATER((short) 2, "普通自提");

    private short typeCode;
    private String typeDesc;

    PickUpTypeEnum(short typeCode, String typeDesc) {
        this.typeCode = typeCode;
        this.typeDesc = typeDesc;
    }

    public short getTypeCode() {
        return typeCode;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public static PickUpTypeEnum getPickUpTypeEnumByCode(short code) {
        for (PickUpTypeEnum pickUpTypeEnum : PickUpTypeEnum.values()) {
            if (pickUpTypeEnum.getTypeCode() == code) {
                return pickUpTypeEnum;
            }
        }
        return null;
    }

    public static String getPickUpTypeDescByCode(Short code) {
        if (null != code) {
            for (PickUpTypeEnum pickUpTypeEnum : PickUpTypeEnum.values()) {
                if (pickUpTypeEnum.getTypeCode() == code) {
                    return pickUpTypeEnum.getTypeDesc();
                }
            }
        }
        return null;
    }
    

    public static Map<Short, String> getDescMap() {
        Map<Short, String> map = new TreeMap<>();
        for (PickUpTypeEnum pickUpTypeEnum : values()) {
            map.put(pickUpTypeEnum.getTypeCode(), pickUpTypeEnum.getTypeDesc());
        }
        return map;
    }
}
