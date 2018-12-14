package com.winhxd.b2c.common.domain.customer.enums;

/**
 * 用户绑定门店状态
 */
public enum CustomerBindStoreStatusEnum {

    BIND((short) 1, "绑定"),
    UN_BIND((short) 2, "解绑"),
    CHANGE_BIND((short) 3, "换绑");

    private short status;
    private String desc;

    CustomerBindStoreStatusEnum(short status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public short getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
