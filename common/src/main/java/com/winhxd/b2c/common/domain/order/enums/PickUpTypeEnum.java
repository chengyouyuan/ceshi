package com.winhxd.b2c.common.domain.order.enums;

/**
 * @author pangjianhua
 * @date 2018/8/2 19:34
 */
public enum PickUpTypeEnum {

    /**
     * 自提
     */
    SELF_PICK_UP(1, "自提"),

    /**
     * 配送
     */
    DELIVERY(2, "配送");

    private int typeCode;
    private String typeDesc;

    PickUpTypeEnum(int typeCode, String typeDesc) {
        this.typeCode = typeCode;
        this.typeDesc = typeDesc;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public String getTypeDesc() {
        return typeDesc;
    }
}
