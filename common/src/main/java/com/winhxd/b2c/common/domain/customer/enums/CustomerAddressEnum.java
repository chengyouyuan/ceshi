package com.winhxd.b2c.common.domain.customer.enums;

/**
 * @author: sunwenwu
 * @Date: 2018/11/8 15：10
 * @Description:
 */
public enum CustomerAddressEnum {

    INSERT("insert","保存收货地址"),
    UPDATE("update","更新收货地址");

    private String opt;
    private String desc;
    CustomerAddressEnum(String opt, String desc){
        this.opt = opt;
        this.desc = desc;
    }

    public String getOpt() {
        return opt;
    }


    public String getDesc() {
        return desc;
    }
}
