package com.winhxd.b2c.common.domain.store.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description: 门店管理商品类
 * @author: lvsen
 * @date: 2018/8/2 14:34
 */
@ApiModel("门店管理商品")
public class StoreProductManage {

    @ApiModelProperty("id主键")
    private Long id;
    @ApiModelProperty("门店id")
    private Long storeId;
    @ApiModelProperty("商品id")
    private String prodId;
    @ApiModelProperty("商品规则")
    private Integer skuAttributeOption;
    @ApiModelProperty("售卖价格")
    private BigDecimal sellMoney;
    @ApiModelProperty("是否推荐 0不推荐 1推荐")
    private Byte recommend;
    @ApiModelProperty("商品状态 0下架1上架2已删除")
    private Byte prodStatus;
    @ApiModelProperty("商品sku")
    private String prodCode;
    @ApiModelProperty("商品sku")
    private String skuCode;
    @ApiModelProperty("创建人id")
    private Long createdBy;
    @ApiModelProperty("创建人名称")
    private String createdByName;
    @ApiModelProperty("创建时间")
    private Date created;
    @ApiModelProperty("更新人id")
    private Long updatedBy;
    @ApiModelProperty("更新人名称")
    private String updatedByName;
    @ApiModelProperty("更新时间")
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

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public BigDecimal getSellMoney() {
        return sellMoney;
    }

    public void setSellMoney(BigDecimal sellMoney) {
        this.sellMoney = sellMoney;
    }

    public Byte getRecommend() {
        return recommend;
    }

    public void setRecommend(Byte recommend) {
        this.recommend = recommend;
    }

    public Byte getProdStatus() {
        return prodStatus;
    }

    public void setProdStatus(Byte prodStatus) {
        this.prodStatus = prodStatus;
    }

    public Integer getSkuAttributeOption() {
        return skuAttributeOption;
    }

    public void setSkuAttributeOption(Integer skuAttributeOption) {
        this.skuAttributeOption = skuAttributeOption;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
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
}