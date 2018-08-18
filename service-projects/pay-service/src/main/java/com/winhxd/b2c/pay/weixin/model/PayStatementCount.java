package com.winhxd.b2c.pay.weixin.model;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 对账单统计表
 * @author yuluyuan
 *
 * 2018年8月18日
 */
public class PayStatementCount {

	/**
	 * 主键
	 */
    private Long id;

	/**
	 * 总交易单数
	 */
    private Integer payNumCount;

	/**
	 * 总交易额
	 */
    private BigDecimal payAmountCount;

	/**
	 * 总退款金额
	 */
    private BigDecimal refundAmountCount;

	/**
	 * 总代金券或立减优惠退款金额
	 */
    private BigDecimal refundDiscountCount;

	/**
	 * 手续费总金额
	 */
    private BigDecimal feeCount;

	/**
	 * 对账单日期
	 */
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