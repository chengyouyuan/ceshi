package com.winhxd.b2c.common.domain.store.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @description: 门店提报商品信息类
 * @author: lvsen
 * @date: 2018/8/2 14:34
 */
@ApiModel("门店提报商品")
public class StoreSubmitProduct {
    @ApiModelProperty("id主键")
    private Long id;
    @ApiModelProperty("门店id")
    private Long storeId;
    @ApiModelProperty("门店名称")
    private String storeName;
    @ApiModelProperty("商品名称")
    private String prodName;
    @ApiModelProperty("商品code")
    private String prodCode;
    @ApiModelProperty("商品图片1")
    private String prodImage1;
    @ApiModelProperty("商品图片2")
    private String prodImage2;
    @ApiModelProperty("商品图片3")
    private String prodImage3;
    @ApiModelProperty("商品规则")
    private String prodSize;
    @ApiModelProperty("商品规则")
    private Integer prodStatus;
    @ApiModelProperty("审核备注")
    private String auditRemark;
    @ApiModelProperty("商品信息（语音）")
    private String prodInfoVoice;
    @ApiModelProperty("商品信息（文字）")
    private String prodInfoText;
    @ApiModelProperty("更新时间")
    private Date updated;
    @ApiModelProperty("更新人id")
    private Long updatedBy;
    @ApiModelProperty("更新人名称")
    private String updatedByName;
    @ApiModelProperty("创建时间")
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
}