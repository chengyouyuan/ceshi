package com.winhxd.b2c.common.domain.order.enums;

/**
 * @author pangjianhua
 * @date 2018/8/2 19:34
 */
public enum PickUpTypeEnum {

    /**
     * 自提
     */
    SELF_PICK_UP((short) 1, "自提"),

    /**
     * 配送
     */
    DELIVERY((short) 2, "配送");

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
}
