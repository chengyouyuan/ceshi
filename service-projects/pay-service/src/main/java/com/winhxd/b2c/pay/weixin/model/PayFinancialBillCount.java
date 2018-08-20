package com.winhxd.b2c.pay.weixin.model;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 微信资金账单统计表
 * @author yuluyuan
 *
 * 2018年8月18日
 */
public class PayFinancialBillCount {
	
	/**
	 * 主键
	 */
    private Long id;

	/**
	 * 资金流水总笔数
	 */
    private BigDecimal financialSwiftNumCount;

	/**
	 * 收入笔数
	 */
    private BigDecimal incomeNumCount;

	/**
	 * 收入金额
	 */
    private BigDecimal incomeAmountCount;

	/**
	 * 支出笔数
	 */
    private BigDecimal expenditureNumCount;

	/**
	 * 支出金额
	 */
    private BigDecimal expenditureAmountCount;

	/**
	 * 资金账单日期
	 */
    private Date billDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getFinancialSwiftNumCount() {
        return financialSwiftNumCount;
    }

    public void setFinancialSwiftNumCount(BigDecimal financialSwiftNumCount) {
        this.financialSwiftNumCount = financialSwiftNumCount;
    }

    public BigDecimal getIncomeNumCount() {
        return incomeNumCount;
    }

    public void setIncomeNumCount(BigDecimal incomeNumCount) {
        this.incomeNumCount = incomeNumCount;
    }

    public BigDecimal getIncomeAmountCount() {
        return incomeAmountCount;
    }

    public void setIncomeAmountCount(BigDecimal incomeAmountCount) {
        this.incomeAmountCount = incomeAmountCount;
    }

    public BigDecimal getExpenditureNumCount() {
        return expenditureNumCount;
    }

    public void setExpenditureNumCount(BigDecimal expenditureNumCount) {
        this.expenditureNumCount = expenditureNumCount;
    }

    public BigDecimal getExpenditureAmountCount() {
        return expenditureAmountCount;
    }

    public void setExpenditureAmountCount(BigDecimal expenditureAmountCount) {
        this.expenditureAmountCount = expenditureAmountCount;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }
}