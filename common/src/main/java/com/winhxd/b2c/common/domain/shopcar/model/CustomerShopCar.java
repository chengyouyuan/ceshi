package com.winhxd.b2c.common.domain.shopcar.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @auther: wangbaokuo
 * @date: 2018/8/2 19:16
 * @description: 购物车
 */
@ApiModel("购物车")
@Data
public class CustomerShopCar {
    @ApiModelProperty(value = "购物车ID")
    private Long id;

    @ApiModelProperty(value = "用户ID")
    private Long customerId;

    @ApiModelProperty(value = "门店ID")
    private Long storeId;

    @ApiModelProperty(value = "商品sku")
    private String skuCode;

    @ApiModelProperty(value = "商品数量")
    private Integer prodNum;

    @ApiModelProperty(value = "创建时间")
    private Date created;

    @ApiModelProperty(value = "修改时间")
    private Date updated;

    @ApiModelProperty(value = "创建人")
    private Long createdby;

    @ApiModelProperty(value = "更新人")
    private Long updatedby;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public Integer getProdNum() {
        return prodNum;
    }

    public void setProdNum(Integer prodNum) {
        this.prodNum = prodNum;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Long getCreatedby() {
        return createdby;
    }

    public void setCreatedby(Long createdby) {
        this.createdby = createdby;
    }

    public Long getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(Long updatedby) {
        this.updatedby = updatedby;
    }
}