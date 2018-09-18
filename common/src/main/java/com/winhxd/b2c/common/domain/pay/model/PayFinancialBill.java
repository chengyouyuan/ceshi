package com.winhxd.b2c.common.domain.pay.model;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 微信资金账单
 * @author yuluyuan
 *
 * 2018年8月18日
 */
public class PayFinancialBill {
	
	/**
	 * 主键
	 */
    private Long id;

	/**
	 * 记账时间
	 */
    private Date accountingTime;

	/**
	 * 微信支付业务单号
	 */
    private String wxPayNo;

	/**
	 * 资金流水单号
	 */
    private String swiftNo;

	/**
	 * 业务名称
	 */
    private String busiName;

	/**
	 * 业务类型
	 */
    private String busiType;

	/**
	 * 收支类型
	 */
    private String budget;

	/**
	 * 收支金额（元）
	 */
    private BigDecimal budgetAmount;

	/**
	 * 账户结余（元）
	 */
    private BigDecimal accountBalance;

	/**
	 * 资金变更提交申请人
	 */
    private String submitBy;

	/**
	 * 备注
	 */
    private String remark;

	/**
	 * 业务凭证号
	 */
    private String busiCredentialNo;

	/**
	 * 资金账单日期
	 */
    private Date billDate;
    
    /**
     * 插入日期
     */
    private Date updated;

	/**
	 * 资金账单统计表id
	 */
    private Long financialBillCountId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getAccountingTime() {
        return accountingTime;
    }

    public void setAccountingTime(Date accountingTime) {
        this.accountingTime = accountingTime;
    }

    public String getWxPayNo() {
        return wxPayNo;
    }

    public void setWxPayNo(String wxPayNo) {
        this.wxPayNo = wxPayNo;
    }

    public String getSwiftNo() {
        return swiftNo;
    }

    public void setSwiftNo(String swiftNo) {
        this.swiftNo = swiftNo;
    }

    public String getBusiName() {
        return busiName;
    }

    public void setBusiName(String busiName) {
        this.busiName = busiName;
    }

    public String getBusiType() {
        return busiType;
    }

    public void setBusiType(String busiType) {
        this.busiType = busiType;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public BigDecimal getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(BigDecimal budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getSubmitBy() {
        return submitBy;
    }

    public void setSubmitBy(String submitBy) {
        this.submitBy = submitBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBusiCredentialNo() {
        return busiCredentialNo;
    }

    public void setBusiCredentialNo(String busiCredentialNo) {
        this.busiCredentialNo = busiCredentialNo;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public Long getFinancialBillCountId() {
        return financialBillCountId;
    }

    public void setFinancialBillCountId(Long financialBillCountId) {
        this.financialBillCountId = financialBillCountId;
    }

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
}