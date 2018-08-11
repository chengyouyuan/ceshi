package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.common.BaseCondition;
import com.winhxd.b2c.common.domain.promotion.model.CouponApplyBrandList;
import com.winhxd.b2c.common.domain.promotion.model.CouponApplyProductList;

import java.io.Serializable;
import java.util.List;

/**
 * @Author wl
 * @Date 2018/8/9 11:03
 * @Description
 **/
public class CouponApplyCondition extends BaseCondition implements Serializable {
    private String name;

    private String code;

    private String remarks;

    private Short applyRuleType;

    private Short status;

    private String userId;

    private String userName;

    private List<CouponApplyBrandList> couponApplyBrandList;
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
