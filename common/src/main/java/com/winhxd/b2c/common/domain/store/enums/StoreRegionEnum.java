package com.winhxd.b2c.common.domain.store.enums;

/**
 * @author: wangbaokuo
 * @date: 2018/8/10 16:21
 */
public enum StoreRegionEnum {
    /**
     * 优惠券模板是否有效  0有效 1无效
     */
    EFFICTIVE((short) 1, "有效"),
    VALIDATE((short) 0, "无效");

    private short code;
    private String desc;

    StoreRegionEnum(short code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public short getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }

}
