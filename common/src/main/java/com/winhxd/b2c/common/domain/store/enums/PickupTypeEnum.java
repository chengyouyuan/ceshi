package com.winhxd.b2c.common.domain.store.enums;

import java.util.Map;
import java.util.TreeMap;

/**
 * 取货方式，门店功能专用
 *
 * @author liutong
 * @date 2018/8/20 14:34
 */
public enum PickupTypeEnum {

    /**
     * 到店自提
     */
    PICK_UP_SELF((short) 1, "到店自提"),
    /**
     * 普通自提
     */
    PICK_UP_DELIVERY((short) 2, "送货上门");

    private short typeCode;
    private String typeDesc;

    PickupTypeEnum(short typeCode, String typeDesc) {
        this.typeCode = typeCode;
        this.typeDesc = typeDesc;
    }

    public short getTypeCode() {
        return typeCode;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public static PickupTypeEnum getPickupTypeEnumByCode(short code) {
        for (PickupTypeEnum pickupTypeEnum : PickupTypeEnum.values()) {
            if (pickupTypeEnum.getTypeCode() == code) {
                return pickupTypeEnum;
            }
        }
        return null;
    }

    public static String getPickupTypeDescByCode(Short code) {
        if (null != code) {
            for (PickupTypeEnum pickupTypeEnum : PickupTypeEnum.values()) {
                if (pickupTypeEnum.getTypeCode() == code) {
                    return pickupTypeEnum.getTypeDesc();
                }
            }
        }
        return null;
    }


    public static Map<Short, String> getDescMap() {
        Map<Short, String> map = new TreeMap<>();
        for (PickupTypeEnum pickupTypeEnum : values()) {
            map.put(pickupTypeEnum.getTypeCode(), pickupTypeEnum.getTypeDesc());
        }
        return map;
    }
}
