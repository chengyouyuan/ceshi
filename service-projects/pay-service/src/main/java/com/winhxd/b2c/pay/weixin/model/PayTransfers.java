package com.winhxd.b2c.pay.weixin.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author yindanqing
 * @Date 2018-8-14 09:51:26
 * @Description: 提现流水
 */
public class PayTransfers {
    /**
     * 主键
     */
    private Long id;

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
     * 随机字符串
     */
    private String nonceStr;

    /**
     * 签名
     */
    private String sign;

    /**
     * 提现流水号
     */
    private String partnerTradeNo;

    /**
     * 第三方单号
     */
    private String transactionId;

    /**
     * 收款人唯一标识(微信openid)
     */
    private String accountId;

    /**
     * 提现账户(银行卡)
     */
    private String account;

    /**
     * NO_CHECK：不校验真实姓名,FORCE_CHECK：强校验真实姓名
     */
    private String checkName;

    /**
     * 收款人姓名
     */
    private String accountName;

    /**
     * 提现通道,1微信余额,2:微信银行
     */
    private Short channel;

    /**
     * 流向名称,1:微信余额, 或各个银行代码
     */
    private String channelCode;

    /**
     * 提现金额,单位分
     */
    private Integer totalFee;

    /**
     * 提现金额,单位元
     */
    private BigDecimal totalAmount;

    /**
     * 手续费,单位分
     */
    private Integer cmmsFee;

    /**
     *手续费,单位元
     */
    private BigDecimal cmmsAmount;

    /**
     * 实际到账金额,单位分
     */
    private Integer realFee;

    /**
     * 实际到账金额,单位元
     */
    private BigDecimal realAmount;

    /**
     * 付款描述信息
     */
    private String desc;

    /**
     * Ip地址
     */
    private String spbillCreateIp;

    /**
     * 完成时间
     */
    private Date timeEnd;

    /**
     * 返回状态码,0:失败,1:成功,2:处理中(针对于提现到银行卡)
     */
    private Short status;

    /**
     * 错误代码
     */
    private String errorCode;

    /**
     * 返回信息
     */
    private String errorMsg;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 创建人ID
     */
    private String createdBy;

    /**
     * 创建人姓名
     */
    private String createdByName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMchAppid() {
        return mchAppid;
    }

    public void setMchAppid(String mchAppid) {
        this.mchAppid = mchAppid == null ? null : mchAppid.trim();
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid == null ? null : mchid.trim();
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo == null ? null : deviceInfo.trim();
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr == null ? null : nonceStr.trim();
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign == null ? null : sign.trim();
    }

    public String getPartnerTradeNo() {
        return partnerTradeNo;
    }

    public void setPartnerTradeNo(String partnerTradeNo) {
        this.partnerTradeNo = partnerTradeNo == null ? null : partnerTradeNo.trim();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId == null ? null : transactionId.trim();
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName == null ? null : checkName.trim();
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    public Short getChannel() {
        return channel;
    }

    public void setChannel(Short channel) {
        this.channel = channel;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode == null ? null : channelCode.trim();
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getCmmsFee() {
        return cmmsFee;
    }

    public void setCmmsFee(Integer cmmsFee) {
        this.cmmsFee = cmmsFee;
    }

    public BigDecimal getCmmsAmount() {
        return cmmsAmount;
    }

    public void setCmmsAmount(BigDecimal cmmsAmount) {
        this.cmmsAmount = cmmsAmount;
    }

    public Integer getRealFee() {
        return realFee;
    }

    public void setRealFee(Integer realFee) {
        this.realFee = realFee;
    }

    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode == null ? null : errorCode.trim();
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg == null ? null : errorMsg.trim();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName == null ? null : createdByName.trim();
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