package com.winhxd.b2c.common.domain.pay.model;

<<<<<<< HEAD
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("出入账明细")
@Data
public class PayFinanceAccountDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty("主键")
    private Long id;
	@ApiModelProperty("订单编码")
    private String orderNo;
	@ApiModelProperty("交易流水号")
    private String tradeNo;
	@ApiModelProperty("金额")
    private BigDecimal money;
	@ApiModelProperty("门店Id")
    private Long storeId;
	@ApiModelProperty("地理区域编码")
    private String regionCode;
	@ApiModelProperty("地理区域名称")
    private String regionName;
	@ApiModelProperty("类型")// 1出账 2入账
    private Short type;
	@ApiModelProperty("1门店提现 2用户退款")//1门店提现 2用户退款
    private Short outType;
	@ApiModelProperty("微信账单状态")//1未入账 2已入账
    private Short wechatAccountStatus;
	@ApiModelProperty("状态")//0无效 1有效
    private Short status;
	@ApiModelProperty("入账状态")//1预入账 2实入账
    private Short dataStatus;
	@ApiModelProperty("流向类型")//1微信 2银行卡
    private Short flowDirectionType;
	@ApiModelProperty("流向名称")//微信或者各个银行卡名称
    private String flowDirectionName;
	@ApiModelProperty("退款账户")
    private String paymentAccount;
	@ApiModelProperty("手续费")
    private BigDecimal cmmsAmt;
	@ApiModelProperty("费率")
    private BigDecimal rate;
	@ApiModelProperty("出入账时间")
    private Date outInDate;
	@ApiModelProperty("创建人id")
    private Long createdBy;
	@ApiModelProperty("创建人")
    private String createdByName;
	@ApiModelProperty("创建时间")
    private Date created;
	@ApiModelProperty("修改人id")
    private Long updatedBy;
	@ApiModelProperty("修改人")
    private String updatedByName;
	@ApiModelProperty("修改时间")
    private Date updated;
=======
import java.math.BigDecimal;
import java.util.Date;

public class PayFinanceAccountDetail {
    private Long id;

    private String orderNo;

    private String tradeNo;

    private BigDecimal money;

    private Long storeId;

    private String regionCode;

    private String regionName;

    private Short type;

    private Short outType;

    private Short wechatAccountStatus;

    private Short status;

    private Short dataStatus;

    private Short flowDirectionType;

    private String flowDirectionName;

    private String paymentAccount;

    private BigDecimal cmmsAmt;

    private BigDecimal rate;

    private Date outInDate;

    private Long createdBy;

    private String createdByName;

    private Date created;

    private Long updatedBy;

    private String updatedByName;

    private Date updated;

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

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Short getOutType() {
        return outType;
    }

    public void setOutType(Short outType) {
        this.outType = outType;
    }

    public Short getWechatAccountStatus() {
        return wechatAccountStatus;
    }

    public void setWechatAccountStatus(Short wechatAccountStatus) {
        this.wechatAccountStatus = wechatAccountStatus;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Short getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Short dataStatus) {
        this.dataStatus = dataStatus;
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

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
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

    public Date getOutInDate() {
        return outInDate;
    }

    public void setOutInDate(Date outInDate) {
        this.outInDate = outInDate;
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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
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
>>>>>>> branch 'master' of git@192.168.1.101:retail2c/retail2c-backend.git
}