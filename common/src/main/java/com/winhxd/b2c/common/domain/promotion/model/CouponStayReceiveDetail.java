package com.winhxd.b2c.common.domain.promotion.model;

import java.util.Date;

/**
 * @author shijinxing
 */
public class CouponStayReceiveDetail {
    /**
     * 优惠券待领取详情表ID
     */
    private Long id;
    /**
     * 优惠券待领取表ID
     */
    private Long couponSendId;
    /**
     * 模板ID
     */
    private Long templateId;
    /**
     * 门店ID
     */
    private Long storeId;
    /**
     * 门店手机号
     */
    private String storeMobile;
    /**
     * 用户编码
     */
    private Long customerId;
    /**
     * 用户手机号
     */
    private String customerMobile;
    /**
     * 数量
     */
    private Integer count;
    /**
     * 优惠券开始时间
     */
    private Date startTime;
    /**
     * 优惠券结束时间
     */
    private Date endTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCouponSendId() {
        return couponSendId;
    }

    public void setCouponSendId(Long couponSendId) {
        this.couponSendId = couponSendId;
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
}