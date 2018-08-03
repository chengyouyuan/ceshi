package com.winhxd.b2c.common.domain.store.enums;

/**
 * @title: retail2c-backend
 * @description:
 * @author: lvsen
 * @date: 2018/8/3 15:58
 * @version: 1.0
 */
public enum StoreProductStatusEnum {
    /**
     * 下架
     */
    UNPUTAWAY((short) 0, "下架"),
    /**
     * 上架
     */
    PUTAWAY((short) 1, "上架"),
    /**
     * 删除
     */
    DELETED((short) 2, "删除");

    private short statusCode;
    private String statusDes;

    StoreProductStatusEnum(short statusCode, String statusDes) {
        this.statusCode = statusCode;
        this.statusDes = statusDes;
    }

    public short getStatusCode() {
        return statusCode;
    }

    public String getStatusDes() {
        return statusDes;
    }
}
