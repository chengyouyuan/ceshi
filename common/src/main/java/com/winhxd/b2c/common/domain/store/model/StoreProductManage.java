package com.winhxd.b2c.common.domain.store.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description: 门店商品管理类
 * @author: lvsen
 * @date: 2018/8/2 14:34
 */
public class StoreProductManage {
    private Long id;

    private Long storeId;

    private String prodId;

    private Integer prodRange;

    private BigDecimal sellMoney;

    private Byte recommend;

    private Byte prodStatus;

    private String prodSpu;

    private String prodSku;

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

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public Integer getProdRange() {
        return prodRange;
    }

    public void setProdRange(Integer prodRange) {
        this.prodRange = prodRange;
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

    public String getProdSpu() {
        return prodSpu;
    }

    public void setProdSpu(String prodSpu) {
        this.prodSpu = prodSpu;
    }

    public String getProdSku() {
        return prodSku;
    }

    public void setProdSku(String prodSku) {
        this.prodSku = prodSku;
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