package com.winhxd.b2c.common.domain.store.model;

import java.util.Date;

 /**
  * @description: 门店提报商品信息类
  * @author: lvsen
  * @date: 2018/8/2 14:34
  */
public class StoreSubmitProduct {
    private Long id;

    private Long storeId;

    private String storeName;

    private String prodName;

    private String prodCode;

    private String prodImage1;

    private String prodImage2;

    private String prodImage3;

    private String prodSize;

    private Integer prodStatus;

    private String auditRemark;

    private String prodInfoVoice;

    private String prodInfoText;

    private Date updated;

    private String updateBy;

    private Date created;

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

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getProdImage1() {
        return prodImage1;
    }

    public void setProdImage1(String prodImage1) {
        this.prodImage1 = prodImage1;
    }

    public String getProdImage2() {
        return prodImage2;
    }

    public void setProdImage2(String prodImage2) {
        this.prodImage2 = prodImage2;
    }

    public String getProdImage3() {
        return prodImage3;
    }

    public void setProdImage3(String prodImage3) {
        this.prodImage3 = prodImage3;
    }

    public String getProdSize() {
        return prodSize;
    }

    public void setProdSize(String prodSize) {
        this.prodSize = prodSize;
    }

    public Integer getProdStatus() {
        return prodStatus;
    }

    public void setProdStatus(Integer prodStatus) {
        this.prodStatus = prodStatus;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public String getProdInfoVoice() {
        return prodInfoVoice;
    }

    public void setProdInfoVoice(String prodInfoVoice) {
        this.prodInfoVoice = prodInfoVoice;
    }

    public String getProdInfoText() {
        return prodInfoText;
    }

    public void setProdInfoText(String prodInfoText) {
        this.prodInfoText = prodInfoText;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}