package com.winhxd.b2c.common.domain.store.enums;

/**
 * @ClassName: StoreSubmitProductStatusEnum
 * @Description: StoreSubmitProductStatusEnum
 * @author: wuyuanbao
 * @date: 2018年8月8日 下午7:23:59
 */
public enum StoreSubmitProductStatusEnum {
    CREATE((short) 0, "待审核"),
    PASS((short) 1, "审核通过"),
    NOTPASS((short) 2, "未通过"),
    ADDPROD((short) 3, "添加到商品库");
    private short statusCode;
    private String statusDes;

    public short getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(short statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDes() {
        return statusDes;
    }

    public void setStatusDes(String statusDes) {
        this.statusDes = statusDes;
    }

    private StoreSubmitProductStatusEnum(short statusCode, String statusDes) {
        this.statusCode = statusCode;
        this.statusDes = statusDes;
    }


}
