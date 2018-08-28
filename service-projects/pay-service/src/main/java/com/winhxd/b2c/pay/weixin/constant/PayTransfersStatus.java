package com.winhxd.b2c.pay.weixin.constant;

/**
 * PayTransfersStatus
 *
 * @Author yindanqing
 * @Date 2018/8/18 15:23
 * @Description: 转账状态
 */
public enum PayTransfersStatus {

    /**
     * 转账成功
     */
    SUCCESS("SUCCESS", "转账成功"),

    /**
     * 转账失败
     */
    FAILED("FAILED", "转账失败"),

    /**
     * 处理中
     */
    PROCESSING("PROCESSING", "处理中"),

    BANK_FAIL("BANK_FAIL", "银行退票"),

    /**
     * 失败(用于判断resultCode状态)
     */
    FAIL("FAIL","转账失败");

    private String code;
    private String text;

    private PayTransfersStatus(String code, String text){
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
