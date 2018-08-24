package com.winhxd.b2c.common.domain.pay.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author wl
 * @Date 2018/8/14 18:22
 * @Description 提现记录
 **/

@ApiModel("门店提现记录")
public class PayWithdrawalsVO {

    @ApiModelProperty("前端返回唯一标识")
    private String index;
    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("门店id")
    @Excel(name = "门店ID", width = 30)
    private Long storeId;
    @ApiModelProperty("门店名称")
    @Excel(name = "门店名称", width = 30)
    private String storeName;
    @ApiModelProperty("提现订单号")
    @Excel(name = "提现订单号", width = 30)
    private String withdrawalsNo;
    @ApiModelProperty("提现金额")
    @Excel(name = "提现金额", width = 30)
    private BigDecimal totalFee;
    @ApiModelProperty("实际到账金额")
    @Excel(name = "实际到账金额", width = 30)
    private BigDecimal realFee;
    @ApiModelProperty("手续费")
    @Excel(name = "手续费", width = 30)
    private BigDecimal cmmsAmt;
    @ApiModelProperty("费率")
    private BigDecimal rate;
    @ApiModelProperty("状态 0未审核 1审核通过 2审核不通过")
    private Short auditStatus;
    @Excel(name = "审核状态", width = 30)
    private String auditStatusName;
    @ApiModelProperty("原因")
    @Excel(name = "原因", width = 30)
    private String auditDesc;
    @ApiModelProperty("流向类型 1微信 2银行卡")
    private Short flowDirectionType;
    @ApiModelProperty("流向名称 微信或者各个银行卡名称")
    @Excel(name = "流向", width = 30)
    private String flowDirectionName;
    @ApiModelProperty("提款人")
    @Excel(name = "提款人", width = 30)
    private String name;
    @ApiModelProperty("手机号")
    @Excel(name = "手机号", width = 30)
    private String mobile;
    @ApiModelProperty("流向账户")
    @Excel(name = "付款账户", width = 30)
    private String paymentAccount;
    @ApiModelProperty("创建时间")
    private Date created;
    @ApiModelProperty("创建人id")
    private Long createdBy;
    @ApiModelProperty("创建人名称")
    private String createdByName;
    @ApiModelProperty("修改人id")
    private Long updatedBy;
    @ApiModelProperty("修改人名称")
    @Excel(name = "审核人", width = 30)
    private String updatedByName;
    @ApiModelProperty("修改时间")
    @Excel(name = "提款时间", width = 30, exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date updated;
    @ApiModelProperty("回调状态 0.申请中，1.提现成功，2提现失败")
    private Short callbackStatus;
    @ApiModelProperty("原因")
    private String callbackReason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getWithdrawalsNo() {
        return withdrawalsNo;
    }

    public void setWithdrawalsNo(String withdrawalsNo) {
        this.withdrawalsNo = withdrawalsNo;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public BigDecimal getRealFee() {
        return realFee;
    }

    public void setRealFee(BigDecimal realFee) {
        this.realFee = realFee;
    }

    public BigDecimal getCmmsAmt() {
        return cmmsAmt;
    }

    public void setCmmsAmt(BigDecimal cmmsAmt) {
        this.cmmsAmt = cmmsAmt;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Short getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Short auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditDesc() {
        return auditDesc;
    }

    public void setAuditDesc(String auditDesc) {
        this.auditDesc = auditDesc;
    }

    public Short getFlowDirectionType() {
        return flowDirectionType;
    }

    public void setFlowDirectionType(Short flowDirectionType) {
        this.flowDirectionType = flowDirectionType;
    }

    public String getFlowDirectionName() {
        return flowDirectionName;
    }

    public void setFlowDirectionName(String flowDirectionName) {
        this.flowDirectionName = flowDirectionName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
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
        this.createdByName = createdByName;
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
        this.updatedByName = updatedByName;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getAuditStatusName() {
        if (Short.valueOf("0").compareTo(auditStatus) == 0) {
            auditStatusName = "未审核";
        }
        if (Short.valueOf("1").compareTo(auditStatus) == 0) {
            auditStatusName = "审核通过";
        }
        if (Short.valueOf("2").compareTo(auditStatus) == 0) {
            auditStatusName = "审核不通过";
        }
        return auditStatusName;
    }

    public Short getCallbackStatus() {
        return callbackStatus;
    }

    public void setCallbackStatus(Short callbackStatus) {
        this.callbackStatus = callbackStatus;
    }

    public String getCallbackReason() {
        return callbackReason;
    }

    public void setCallbackReason(String callbackReason) {
        this.callbackReason = callbackReason;
    }

    public String getIndex() {
        return String.valueOf(getId());
    }
}
