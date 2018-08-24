package com.winhxd.b2c.common.domain.customer.enums;

/**
 * Created by wangbaokuo on 2018/8/24 17:43
 */
public enum CustomerUserEnum {

    CUSTOMER_STATUS_NORMAL(0, "有效"),
    CUSTOMER_STATUS_INVALID(1, "无效");

    private Integer code;
    private String desc;
    CustomerUserEnum(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }

}
