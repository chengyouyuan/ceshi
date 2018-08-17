package com.winhxd.b2c.pay.weixin.model;

import java.math.BigDecimal;
import java.util.Date;

public class PayStatementCount {
    private Long id;

    private Integer payNumCount;

    private BigDecimal payAmountCount;

    private BigDecimal refundAmountCount;

    private BigDecimal refundDiscountCount;

    private BigDecimal feeCount;

    private Date billDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPayNumCount() {
        return payNumCount;
    }

    public void setPayNumCount(Integer payNumCount) {
        this.payNumCount = payNumCount;
    }

    public BigDecimal getPayAmountCount() {
        return payAmountCount;
    }

    public void setPayAmountCount(BigDecimal payAmountCount) {
        this.payAmountCount = payAmountCount;
    }

    public BigDecimal getRefundAmountCount() {
        return refundAmountCount;
    }

    public void setRefundAmountCount(BigDecimal refundAmountCount) {
        this.refundAmountCount = refundAmountCount;
    }

    public BigDecimal getRefundDiscountCount() {
        return refundDiscountCount;
    }

    public void setRefundDiscountCount(BigDecimal refundDiscountCount) {
        this.refundDiscountCount = refundDiscountCount;
    }

    public BigDecimal getFeeCount() {
        return feeCount;
    }

    public void setFeeCount(BigDecimal feeCount) {
        this.feeCount = feeCount;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }
}