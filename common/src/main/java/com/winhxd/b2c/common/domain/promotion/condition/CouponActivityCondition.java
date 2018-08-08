package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.base.condition.BaseCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询用的condition
 * @author shijinxing
 * @date 2018/8/6
 */
@Data
@ApiModel(value = "用户请求参数",description = "后台用户列表请求参数")
public class CouponActivityCondition extends BaseCondition implements Serializable {

    @ApiModelProperty(value = "优惠券活动ID")
    private Long id;

    @ApiModelProperty(value = "发券名称")
    private String name;

    @ApiModelProperty(value = "发券编码")
    private String code;

    @ApiModelProperty(value = "优惠券编码")
    private String templateCode;

    @ApiModelProperty(value = "C端用户手机号")
    private String customerMobile;

    @ApiModelProperty(value = "门店编码")
    private Long storeId;

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty(value = "优惠券数量的限制 1优惠券总数2每个门店优惠券数")
    private Short couponNumType;

    @ApiModelProperty(value = "用户领券限制 1不限制 2每个门店可领取数量")
    private Short customerVoucherLimitType;

    @ApiModelProperty(value = "创建人")
    private String createdByName;

    @ApiModelProperty(value = "创建时间开始")
    private Date createdStart;

    @ApiModelProperty(value = "创建时间结束")
    private Date createdEnd;

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

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

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
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

    public Short getCouponNumType() {
        return couponNumType;
    }

    public void setCouponNumType(Short couponNumType) {
        this.couponNumType = couponNumType;
    }

    public Short getCustomerVoucherLimitType() {
        return customerVoucherLimitType;
    }

    public void setCustomerVoucherLimitType(Short customerVoucherLimitType) {
        this.customerVoucherLimitType = customerVoucherLimitType;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Date getCreatedStart() {
        return createdStart;
    }

    public void setCreatedStart(Date createdStart) {
        this.createdStart = createdStart;
    }

    public Date getCreatedEnd() {
        return createdEnd;
    }

    public void setCreatedEnd(Date createdEnd) {
        this.createdEnd = createdEnd;
    }
}
