package com.winhxd.b2c.pay.weixin.condition;

import com.winhxd.b2c.common.constant.WithDrawChannelCodeType;

import java.math.BigDecimal;

/**
 * PayTransfersToWxBankCondition
 *
 * @Author yindanqing
 * @Date 2018/8/14 10:43
 * @Description: 提现至微信余额
 */
public class PayTransfersToWxBankCondition implements java.io.Serializable {

    private static final long serialVersionUID = -7292089920160701229L;

    /**
     * 提现流水号
     */
    private String partnerTradeNo;

    /**
     * 提现账户(银行卡)
     */
    private String account;

    /**
     * 收款人姓名
     */
    private String accountName;

    /**
     * 流向名称,银行代码
     */
    private WithDrawChannelCodeType channelCode;

    /**
     * 提现金额,单位元
     */
    private BigDecimal totalAmount;

    /**
     * 付款描述信息
     */
    private String desc;

    public String getPartnerTradeNo() {
        return partnerTradeNo;
    }

    public void setPartnerTradeNo(String partnerTradeNo) {
        this.partnerTradeNo = partnerTradeNo;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public WithDrawChannelCodeType getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(WithDrawChannelCodeType channelCode) {
        this.channelCode = channelCode;
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
}
