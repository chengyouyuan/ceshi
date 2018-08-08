package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.promotion.model.CouponActivityTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 新增、编辑用的condition
 * @author shijinxing
 * @date 2018/8/7
 */
@Data
@ApiModel(value = "用户请求参数",description = "新增、编辑请求参数")
public class CouponActivityAddCondition {

    @ApiModelProperty(value = "优惠券活动ID")
    private Long id;

    @ApiModelProperty(value = "活动名称")
    private String name;

    @ApiModelProperty(value = "活动编码")
    private String code;

    @ApiModelProperty(value = "活动说明")
    private String exolian;

    @ApiModelProperty(value = "活动备注")
    private String remarks;

    @ApiModelProperty(value = "1领券 2推券")
    private Short type;

    @ApiModelProperty(value = "优惠券详情")
    private List<CouponActivityTemplate> couponActivityTemplateList;

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

    @ApiModelProperty(value = "优惠券类型 1新用户注册 2老用户活动")
    private Short couponType;

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

    public List<CouponActivityTemplate> getCouponActivityTemplateList() {
        return couponActivityTemplateList;
    }

    public void setCouponActivityTemplateList(List<CouponActivityTemplate> couponActivityTemplateList) {
        this.couponActivityTemplateList = couponActivityTemplateList;
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

    public Short getCouponType() {
        return couponType;
    }

    public void setCouponType(Short couponType) {
        this.couponType = couponType;
    }
}
