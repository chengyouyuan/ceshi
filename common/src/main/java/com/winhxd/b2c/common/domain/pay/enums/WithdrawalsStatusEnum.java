package com.winhxd.b2c.common.domain.pay.enums;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/18 11 22
 * @Description
 */
public enum WithdrawalsStatusEnum {

    APPLY((short) 0, "申请中"),

    SUCCESS((short) 1, "提现成功"),

    FAIL((short) 2, "提现失败"),

    REAPPLY((short) 3, "无效"),
    HANDLE((short) 4, "处理中") ,
    BANK_FAIL((short) 5, "银行退票") ;

    private short statusCode;
    private String statusDesc;

    WithdrawalsStatusEnum(short statusCode, String statusDesc) {
        this.statusCode = statusCode;
        this.statusDesc = statusDesc;
    }

    public short getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(short statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
}
