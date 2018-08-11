package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.common.BaseCondition;
import com.winhxd.b2c.common.domain.promotion.model.CouponApplyBrandList;
import com.winhxd.b2c.common.domain.promotion.model.CouponApplyProductList;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @Author wl
 * @Date 2018/8/9 11:03
 * @Description
 **/
public class CouponApplyCondition extends BaseCondition implements Serializable {

    @ApiModelProperty(value = "适用对象规则名称")
    private String name;
    @ApiModelProperty(value = "适用对象规则编码")
    private String code;
    @ApiModelProperty(value = "备注")
    private String remarks;
    @ApiModelProperty(value = "适用对象类型 1、通用 2、品牌 3、品类4、商品")
    private Short applyRuleType;
    @ApiModelProperty(value = "状态")
    private Short status;
    @ApiModelProperty(value = "当前操作人id")
    private String userId;
    @ApiModelProperty(value = "当前操作人名称")
    private String userName;
    @ApiModelProperty(value = "品牌信息列表")
    private List<CouponApplyBrandList> couponApplyBrandList;
    @ApiModelProperty(value = "商品信息列表")
    private List<CouponApplyProductList> couponApplyProductList;

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Short getApplyRuleType() {
        return applyRuleType;
    }

    public void setApplyRuleType(Short applyRuleType) {
        this.applyRuleType = applyRuleType;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public List<CouponApplyBrandList> getCouponApplyBrandList() {
        return couponApplyBrandList;
    }

    public void setCouponApplyBrandList(List<CouponApplyBrandList> couponApplyBrandList) {
        this.couponApplyBrandList = couponApplyBrandList;
    }

    public List<CouponApplyProductList> getCouponApplyProductList() {
        return couponApplyProductList;
    }

    public void setCouponApplyProductList(List<CouponApplyProductList> couponApplyProductList) {
        this.couponApplyProductList = couponApplyProductList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
