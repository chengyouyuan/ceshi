package com.winhxd.b2c.common.constant;

/**
 * TransfersChannelCodeType
 *
 * @Author yindanqing
 * @Date 2018/8/14 9:55
 * @Description: 转账通道编码枚举
 */
public enum TransfersChannelCodeType {

    WXBALANCE(1, "微信余额"),
    ICBC(1002, "工商银行"),
    ABC(1005, "农业银行"),
    BOC(1026, "中国银行"),
    CCB(1003, "建设银行"),
    CMB(1001, "招商银行"),
    PSBC(1066, "邮储银行"),
    BCM(1020, "交通银行"),
    SPDB(1004, "浦发银行"),
    CMSB(1006, "民生银行"),
    CIB(1009, "兴业银行"),
    PINGANBANK(1010, "平安银行"),
    CITICBANK(1021, "中信银行"),
    HSBCBANK(1025, "华夏银行"),
    CGB(1027, "广发银行"),
    CEB(1022, "光大银行"),
    BOB(1032, "北京银行"),
    BANKOFNINGBO(1056, "宁波银行");

    private TransfersChannelCodeType(int code, String text){
        this.code = code;
        this.text = text;
    }

    private int code;

    private String text;

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }


}
