package com.winhxd.b2c.pay.weixin.base.condition;

import java.math.BigDecimal;

/**
 * PayTransfersToWxBalanceCondition
 *
 * @Author yindanqing
 * @Date 2018/8/14 10:42
 * @Description: 提现至微信余额
 */
public class PayTransfersToWxBalanceCondition {

    /**
     * 商户账号appid
     */
    private String mchAppid;

    /**
     * 商户号
     */
    private String mchid;

    /**
     * 设备号
     */
    private String deviceInfo;

    /**
     * 提现流水号
     */
    private String partnerTradeNo;

    /**
     * 收款人唯一标识(微信openid)
     */
    private String accountId;

    /**
     * 收款人姓名
     */
    private String accountName;

    /**
     * 提现金额,单位元
     */
    private BigDecimal totalAmount;

    /**
     * 付款描述信息
     */
    private String desc;

    /**
     * Ip地址
     */
    private String spbillCreateIp;

    public String getMchAppid() {
        return mchAppid;
    }

    public void setMchAppid(String mchAppid) {
        this.mchAppid = mchAppid;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getPartnerTradeNo() {
        return partnerTradeNo;
    }

    public void setPartnerTradeNo(String partnerTradeNo) {
        this.partnerTradeNo = partnerTradeNo;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
    }
}
