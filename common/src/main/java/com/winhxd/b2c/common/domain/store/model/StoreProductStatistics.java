package com.winhxd.b2c.common.domain.store.model;

import java.util.Date;

/**
 * @description: 门店商品统计表
 * @author: lvsen
 * @date: 2018/8/6 14:38
 */
public class StoreProductStatistics {
    private Long id;

    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 商品id
     */
    private String prodCode;
    /**
     * sku编码
     */
    private String skuCode;
    /**
     * 售出数据
     */
    private Integer quantitySoldOut;
    /**
     * 浏览数量
     */
    private Integer browseNumber;
    /**
     * 订单编号
     */
    private String orderNo;

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

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
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

    public Integer getQuantitySoldOut() {
        return quantitySoldOut;
    }

    public void setQuantitySoldOut(Integer quantitySoldOut) {
        this.quantitySoldOut = quantitySoldOut;
    }

    public Integer getBrowseNumber() {
        return browseNumber;
    }

    public void setBrowseNumber(Integer browseNumber) {
        this.browseNumber = browseNumber;
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    
}