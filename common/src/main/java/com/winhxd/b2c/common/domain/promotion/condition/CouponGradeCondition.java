package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.common.PagedCondition;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author wl
 * @Date 2018/8/8 16:43
 * @Description
 **/
public class CouponGradeCondition extends PagedCondition implements Serializable {
    private Long id;

    private String code;

    private String name;

    private String remarks;

    private Short status;

    private Short type;

    private BigDecimal reducedAmt;

    private Short reducedType;

    private BigDecimal discountedAmt;

    private BigDecimal discounted;

    private BigDecimal discountedMaxAmt;

    private BigDecimal fullGiveAmt;

    private BigDecimal increaseAmt;

    private BigDecimal cost;

    private Integer count;

    private Integer times;


    private String userId;
    private String userName;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }


    public BigDecimal getReducedAmt() {
        return reducedAmt;
    }

    public void setReducedAmt(BigDecimal reducedAmt) {
        this.reducedAmt = reducedAmt;
    }

    public Short getReducedType() {
        return reducedType;
    }

    public void setReducedType(Short reducedType) {
        this.reducedType = reducedType;
    }

    public BigDecimal getDiscountedAmt() {
        return discountedAmt;
    }

    public void setDiscountedAmt(BigDecimal discountedAmt) {
        this.discountedAmt = discountedAmt;
    }

    public BigDecimal getDiscounted() {
        return discounted;
    }

    public void setDiscounted(BigDecimal discounted) {
        this.discounted = discounted;
    }

    public BigDecimal getDiscountedMaxAmt() {
        return discountedMaxAmt;
    }

    public void setDiscountedMaxAmt(BigDecimal discountedMaxAmt) {
        this.discountedMaxAmt = discountedMaxAmt;
    }

    public BigDecimal getFullGiveAmt() {
        return fullGiveAmt;
    }

    public void setFullGiveAmt(BigDecimal fullGiveAmt) {
        this.fullGiveAmt = fullGiveAmt;
    }

    public BigDecimal getIncreaseAmt() {
        return increaseAmt;
    }

    public void setIncreaseAmt(BigDecimal increaseAmt) {
        this.increaseAmt = increaseAmt;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
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
