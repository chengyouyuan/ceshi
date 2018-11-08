package com.winhxd.b2c.common.domain.customer.enums;

/**
 * @author: sunwenwu
 * @Date: 2018/11/8 15：10
 * @Description:
 */
public enum CustomerAddressEnum {

    DDFAULT_ADDRESS(1,"默认地址"),
    NO_DDFAULT_ADDRESS(0,"非默认地址");

    private Integer code;
    private String desc;
    CustomerAddressEnum(Integer code, String desc){
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
