package com.winhxd.b2c.pay.weixin.model;

import java.math.BigDecimal;
import java.util.Date;

public class PayFinancialBill {
    private Long id;

    private Date accountingTime;

    private String wxPayNo;

    private String swiftNo;

    private String busiName;

    private String busiType;

    private String budget;

    private BigDecimal budgetAmount;

    private Long accountBalance;

    private String submitBy;

    private String remark;

    private String busiCredentialNo;

    private Date billDate;

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

    public Long getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Long accountBalance) {
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
}