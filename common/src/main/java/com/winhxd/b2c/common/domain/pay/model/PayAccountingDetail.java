package com.winhxd.b2c.common.domain.pay.model;

import java.math.BigDecimal;
import java.util.Date;

public class PayAccountingDetail {
    private Long id;

    private String orderNo;

    private Byte detailType;

    private BigDecimal detailMoney;

    private Long storeId;

    private Date recordedTime;

    private Date insertTime;

    private Byte thirdPartyVerifyStatus;

    private BigDecimal thirdPartyFeeMoney;

    private Date thirdPartyVerifyTime;

    private Byte verifyStatus;

    private String verifyCode;

    private Date verifyTime;

    private Date operatedTime;

    private Long operatedBy;

    private String operatedByName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Byte getDetailType() {
        return detailType;
    }

    public void setDetailType(Byte detailType) {
        this.detailType = detailType;
    }

    public BigDecimal getDetailMoney() {
        return detailMoney;
    }

    public void setDetailMoney(BigDecimal detailMoney) {
        this.detailMoney = detailMoney;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Date getRecordedTime() {
        return recordedTime;
    }

    public void setRecordedTime(Date recordedTime) {
        this.recordedTime = recordedTime;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Byte getThirdPartyVerifyStatus() {
        return thirdPartyVerifyStatus;
    }

    public void setThirdPartyVerifyStatus(Byte thirdPartyVerifyStatus) {
        this.thirdPartyVerifyStatus = thirdPartyVerifyStatus;
    }

    public BigDecimal getThirdPartyFeeMoney() {
        return thirdPartyFeeMoney;
    }

    public void setThirdPartyFeeMoney(BigDecimal thirdPartyFeeMoney) {
        this.thirdPartyFeeMoney = thirdPartyFeeMoney;
    }

    public Date getThirdPartyVerifyTime() {
        return thirdPartyVerifyTime;
    }

    public void setThirdPartyVerifyTime(Date thirdPartyVerifyTime) {
        this.thirdPartyVerifyTime = thirdPartyVerifyTime;
    }

    public Byte getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Byte verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public Date getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    public Date getOperatedTime() {
        return operatedTime;
    }

    public void setOperatedTime(Date operatedTime) {
        this.operatedTime = operatedTime;
    }

    public Long getOperatedBy() {
        return operatedBy;
    }

    public void setOperatedBy(Long operatedBy) {
        this.operatedBy = operatedBy;
    }

    public String getOperatedByName() {
        return operatedByName;
    }

    public void setOperatedByName(String operatedByName) {
        this.operatedByName = operatedByName;
    }
}