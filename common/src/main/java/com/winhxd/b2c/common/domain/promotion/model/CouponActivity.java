package com.winhxd.b2c.common.domain.promotion.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author shijinxing
 * @date: 2018-8-6
 * @description: 优惠券活动
 */
@ApiModel("优惠券活动")
@Data
public class CouponActivity {

    @ApiModelProperty(value = "优惠券活动ID")
    private Long id;

    @ApiModelProperty(value = "发券名称")
    private String name;

    @ApiModelProperty(value = "发券编码")
    private String code;

    @ApiModelProperty(value = "发券说明")
    private String exolian;

    @ApiModelProperty(value = "发券备注")
    private String remarks;

    @ApiModelProperty(value = "1领券 2推券")
    private Short type;

    @ApiModelProperty(value = "优惠券类型 1新用户注册 2老用户活动")
    private Short couponType;

    @ApiModelProperty(value = "活动开始时间")
    private Date activityStart;

    @ApiModelProperty(value = "活动结束时间")
    private Date activityEnd;

    @ApiModelProperty(value = "优惠券数量的限制 1优惠券总数2每个门店优惠券数")
    private Short couponNumType;

    @ApiModelProperty(value = "数量")
    private Integer couponNum;

    @ApiModelProperty(value = "用户领券限制 1不限制 2每个门店可领取数量")
    private Short customerVoucherLimitType;

    @ApiModelProperty(value = "用户可领取数量")
    private Integer customerVoucherLimitNum;

    @ApiModelProperty(value = "推送数量")
    private Integer sendNum;

    @ApiModelProperty(value = "发券时间")
    private Date sendTime;

    @ApiModelProperty(value = "活动状态 1开启 2停止")
    private Short activityStatus;

    @ApiModelProperty(value = "是否有效0有效1无效")
    private Short status;

    @ApiModelProperty(value = "创建人id")
    private Long createdBy;

    @ApiModelProperty(value = "创建人")
    private String createdByName;

    @ApiModelProperty(value = "创建时间(使用时间)")
    private Date created;

    @ApiModelProperty(value = "修改人id")
    private Long updateBy;

    @ApiModelProperty(value = "修改人")
    private String updatedByName;

    @ApiModelProperty(value = "修改时间")
    private Date updated;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExolian() {
        return exolian;
    }

    public void setExolian(String exolian) {
        this.exolian = exolian;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Short getCouponType() {
        return couponType;
    }

    public void setCouponType(Short couponType) {
        this.couponType = couponType;
    }

    public Date getActivityStart() {
        return activityStart;
    }

    public void setActivityStart(Date activityStart) {
        this.activityStart = activityStart;
    }

    public Date getActivityEnd() {
        return activityEnd;
    }

    public void setActivityEnd(Date activityEnd) {
        this.activityEnd = activityEnd;
    }

    public Short getCouponNumType() {
        return couponNumType;
    }

    public void setCouponNumType(Short couponNumType) {
        this.couponNumType = couponNumType;
    }

    public Integer getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(Integer couponNum) {
        this.couponNum = couponNum;
    }

    public Short getCustomerVoucherLimitType() {
        return customerVoucherLimitType;
    }

    public void setCustomerVoucherLimitType(Short customerVoucherLimitType) {
        this.customerVoucherLimitType = customerVoucherLimitType;
    }

    public Integer getCustomerVoucherLimitNum() {
        return customerVoucherLimitNum;
    }

    public void setCustomerVoucherLimitNum(Integer customerVoucherLimitNum) {
        this.customerVoucherLimitNum = customerVoucherLimitNum;
    }

    public Integer getSendNum() {
        return sendNum;
    }

    public void setSendNum(Integer sendNum) {
        this.sendNum = sendNum;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Short getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(Short activityStatus) {
        this.activityStatus = activityStatus;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
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

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
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