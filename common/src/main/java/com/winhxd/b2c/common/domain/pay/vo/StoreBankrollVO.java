package com.winhxd.b2c.common.domain.pay.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author wl
 * @Date 2018/8/14 15:21
 * @Description 门店资金前端展示类
 **/
@ApiModel("门店资金信息")
public class StoreBankrollVO {
    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("门店id")
    private Long storeId;
    @ApiModelProperty("总收入")
    private BigDecimal totalMoeny;
    @ApiModelProperty("可提现金额")
    private BigDecimal presentedMoney;
    @ApiModelProperty("提现冻结金额")
    private BigDecimal presentedFrozenMoney;
    @ApiModelProperty("待结算金额")
    private BigDecimal settlementSettledMoney;
    @ApiModelProperty("已提现金额（包含提现中的金额）")
    private BigDecimal alreadyPresentedMoney;
    @ApiModelProperty("今日收入合计")
    private BigDecimal totalMoneyToday;
    @ApiModelProperty("创建人id")
    private Long createdBy;
    @ApiModelProperty("创建人名称")
    private String createdByName;
    @ApiModelProperty("创建时间")
    private Date created;
    @ApiModelProperty("修改人id")
    private Long updatedBy;
    @ApiModelProperty("修改人名称")
    private String updatedByName;
    @ApiModelProperty("修改时间")
    private Date updated;


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

    public BigDecimal getTotalMoeny() {
        return totalMoeny;
    }

    public void setTotalMoeny(BigDecimal totalMoeny) {
        this.totalMoeny = totalMoeny;
    }

    public BigDecimal getPresentedMoney() {
        return presentedMoney;
    }

    public void setPresentedMoney(BigDecimal presentedMoney) {
        this.presentedMoney = presentedMoney;
    }

    public BigDecimal getPresentedFrozenMoney() {
        return presentedFrozenMoney;
    }

    public void setPresentedFrozenMoney(BigDecimal presentedFrozenMoney) {
        this.presentedFrozenMoney = presentedFrozenMoney;
    }

    public BigDecimal getSettlementSettledMoney() {
        return settlementSettledMoney;
    }

    public void setSettlementSettledMoney(BigDecimal settlementSettledMoney) {
        this.settlementSettledMoney = settlementSettledMoney;
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

    public BigDecimal getAlreadyPresentedMoney() {
        return alreadyPresentedMoney;
    }

    public void setAlreadyPresentedMoney(BigDecimal alreadyPresentedMoney) {
        this.alreadyPresentedMoney = alreadyPresentedMoney;
    }

    public BigDecimal getTotalMoneyToday() {
        return totalMoneyToday;
    }

    public void setTotalMoneyToday(BigDecimal totalMoneyToday) {
        this.totalMoneyToday = totalMoneyToday;
    }
}
