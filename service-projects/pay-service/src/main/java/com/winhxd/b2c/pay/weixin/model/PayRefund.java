package com.winhxd.b2c.pay.weixin.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lizhonghua
 * @description 退款类
 */
public class PayRefund {

    /**
     * 主键
     */
    private Long id;

    /**
     * 公众号ID
     */
    private String appid;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 随机字符串
     */
    private String nonceStr;

    /**
     * 签名
     */
    private String sign;

    /**
     * 签名类型
     */
    private String signType;

    /**
     * 微信生成的订单号
     */
    private String transactionId;

    /**
     * 商户订单号（流水号）
     */
    private String outTradeNo;

    /**
     * 商户退款单号（使用流水号）
     */
    private String outRefundNo;

    /**
     * 订单金额：分/单位
     */
    private Integer totalFee;

    /**
     * 订单金额：元/单位
     */
    private BigDecimal totalAmount;

    /**
     * 退款金额：分/单位
     */
    private Integer refundFee;

    /**
     * 退款金额：元/单位
     */
    private BigDecimal refundAmount;

    /**
     * 退款货币种类
     */
    private String refundFeeType;

    /**
     * 退款原因
     */
    private String refundDesc;

    /**
     * 退款资金来源
     */
    private String refundAccount;

    /**
     * 回调URL
     */
    private String notifyUrl;

    /**
     * 应用系统订单号
     */
    private String orderNo;

    /**
     * 支付类型 1微信 2支付宝
     */
    private Short payType;

    /**
     * 回调微信退款单号
     */
    private String callbackRefundId;

    /**
     * 回调退款金额：分/单位
     */
    private Integer callbackRefundFee;

    /**
     * 回调退款金额：元/单位
     */
    private BigDecimal callbackRefundAmount;

    /**
     * 回调应结退款总金额：分/单位
     */
    private Integer callbackSettlementRefundFee;

    /**
     * 回调应结退款总金额：元/单位
     */
    private BigDecimal callbackSettlementRefundAmount;

    /**
     * 回调订单总金额：分/单位
     */
    private Integer callbackTotalFee;

    /**
     * 回调应结订单总金额：分/单位
     */
    private Integer callbackSettlementTotalFee;

    /**
     * 回调退款货币种类
     */
    private String callbackFeeType;

    /**
     * 回调现金支付金额：分/单位
     */
    private Integer callbackCashFee;

    /**
     * 回调现金支付币种
     */
    private String callbackCashFeeType;

    /**
     * 回调现金退款金额：分/单位
     */
    private Integer callbackCashRefundFee;

    /**
     * 退款状态 0失败 1成功
     */
    private Short callbackStatus;

    /**
     * 退款状态描述
     */
    private String callbackStatusDesc;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 创建人ID
     */
    private Long createdBy;

    /**
     * 创建人姓名
     */
    private String createdByName;

    /**
     * 修改时间
     */
    private Date updated;

    /**
     * 修改人ID
     */
    private Long updatedBy;

    /**
     * 修改人姓名
     */
    private String updatedByName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid == null ? null : appid.trim();
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId == null ? null : mchId.trim();
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

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType == null ? null : signType.trim();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId == null ? null : transactionId.trim();
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo == null ? null : outRefundNo.trim();
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public Integer getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Integer refundFee) {
        this.refundFee = refundFee;
    }

    public String getRefundFeeType() {
        return refundFeeType;
    }

    public void setRefundFeeType(String refundFeeType) {
        this.refundFeeType = refundFeeType == null ? null : refundFeeType.trim();
    }

    public String getRefundDesc() {
        return refundDesc;
    }

    public void setRefundDesc(String refundDesc) {
        this.refundDesc = refundDesc == null ? null : refundDesc.trim();
    }

    public String getRefundAccount() {
        return refundAccount;
    }

    public void setRefundAccount(String refundAccount) {
        this.refundAccount = refundAccount == null ? null : refundAccount.trim();
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl == null ? null : notifyUrl.trim();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Short getPayType() {
        return payType;
    }

    public void setPayType(Short payType) {
        this.payType = payType;
    }

    public String getCallbackRefundId() {
        return callbackRefundId;
    }

    public void setCallbackRefundId(String callbackRefundId) {
        this.callbackRefundId = callbackRefundId == null ? null : callbackRefundId.trim();
    }

    public Integer getCallbackRefundFee() {
        return callbackRefundFee;
    }

    public void setCallbackRefundFee(Integer callbackRefundFee) {
        this.callbackRefundFee = callbackRefundFee;
    }

    public Integer getCallbackSettlementRefundFee() {
        return callbackSettlementRefundFee;
    }

    public void setCallbackSettlementRefundFee(Integer callbackSettlementRefundFee) {
        this.callbackSettlementRefundFee = callbackSettlementRefundFee;
    }

    public Integer getCallbackTotalFee() {
        return callbackTotalFee;
    }

    public void setCallbackTotalFee(Integer callbackTotalFee) {
        this.callbackTotalFee = callbackTotalFee;
    }

    public Integer getCallbackSettlementTotalFee() {
        return callbackSettlementTotalFee;
    }

    public void setCallbackSettlementTotalFee(Integer callbackSettlementTotalFee) {
        this.callbackSettlementTotalFee = callbackSettlementTotalFee;
    }

    public String getCallbackFeeType() {
        return callbackFeeType;
    }

    public void setCallbackFeeType(String callbackFeeType) {
        this.callbackFeeType = callbackFeeType == null ? null : callbackFeeType.trim();
    }

    public Integer getCallbackCashFee() {
        return callbackCashFee;
    }

    public void setCallbackCashFee(Integer callbackCashFee) {
        this.callbackCashFee = callbackCashFee;
    }

    public String getCallbackCashFeeType() {
        return callbackCashFeeType;
    }

    public void setCallbackCashFeeType(String callbackCashFeeType) {
        this.callbackCashFeeType = callbackCashFeeType == null ? null : callbackCashFeeType.trim();
    }

    public Integer getCallbackCashRefundFee() {
        return callbackCashRefundFee;
    }

    public void setCallbackCashRefundFee(Integer callbackCashRefundFee) {
        this.callbackCashRefundFee = callbackCashRefundFee;
    }

    public Short getCallbackStatus() {
        return callbackStatus;
    }

    public void setCallbackStatus(Short callbackStatus) {
        this.callbackStatus = callbackStatus;
    }

    public String getCallbackStatusDesc() {
        return callbackStatusDesc;
    }

    public void setCallbackStatusDesc(String callbackStatusDesc) {
        this.callbackStatusDesc = callbackStatusDesc == null ? null : callbackStatusDesc.trim();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName == null ? null : createdByName.trim();
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName == null ? null : updatedByName.trim();
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public BigDecimal getCallbackRefundAmount() {
        return callbackRefundAmount;
    }

    public void setCallbackRefundAmount(BigDecimal callbackRefundAmount) {
        this.callbackRefundAmount = callbackRefundAmount;
    }

    public BigDecimal getCallbackSettlementRefundAmount() {
        return callbackSettlementRefundAmount;
    }

    public void setCallbackSettlementRefundAmount(BigDecimal callbackSettlementRefundAmount) {
        this.callbackSettlementRefundAmount = callbackSettlementRefundAmount;
    }
}