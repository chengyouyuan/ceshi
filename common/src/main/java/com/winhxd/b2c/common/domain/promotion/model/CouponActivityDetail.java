package com.winhxd.b2c.common.domain.promotion.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
/**
 * @author shijinxing
 * @date: 2018-8-6
 * @description: 优惠券活动详情
 */
@ApiModel("优惠券活动详情")
@Data
public class CouponActivityDetail {

    @ApiModelProperty(value = "优惠券活动详情ID")
    private Long id;

    @ApiModelProperty(value = "优惠券活动ID")
    private Long couponActivityId;

    @ApiModelProperty(value = "模板ID")
    private Long templateId;

    @ApiModelProperty(value = "门店ID")
    private Long storeId;

    @ApiModelProperty(value = "门店手机号")
    private String storeMobile;

    @ApiModelProperty(value = "用户编码")
    private Long customerId;

    @ApiModelProperty(value = "用户手机号")
    private String customerMobile;

    @ApiModelProperty(value = "数量")
    private Integer count;

    @ApiModelProperty(value = "优惠券开始时间")
    private Date startTime;

    @ApiModelProperty(value = "优惠券结束时间")
    private Date endTime;

    @ApiModelProperty(value = "优惠券有效天数")
    private Integer effectiveDays;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCouponActivityId() {
        return couponActivityId;
    }

    public void setCouponActivityId(Long couponActivityId) {
        this.couponActivityId = couponActivityId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getStoreMobile() {
        return storeMobile;
    }

    public void setStoreMobile(String storeMobile) {
        this.storeMobile = storeMobile;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getEffectiveDays() {
        return effectiveDays;
    }

    public void setEffectiveDays(Integer effectiveDays) {
        this.effectiveDays = effectiveDays;
    }
}