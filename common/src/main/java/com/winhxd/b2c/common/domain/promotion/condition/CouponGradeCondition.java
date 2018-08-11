package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.common.PagedCondition;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author wl
 * @Date 2018/8/8 16:43
 * @Description
 **/
public class CouponGradeCondition extends PagedCondition implements Serializable {
    @ApiModelProperty(value = "坎级规则id")
    private Long id;
    @ApiModelProperty(value = "坎级规则编码")
    private String code;
    @ApiModelProperty(value = "坎级规则名称")
    private String name;
    @ApiModelProperty(value = "备注")
    private String remarks;
    @ApiModelProperty(value = "状态")
    private Short status;
    @ApiModelProperty(value = "类型 (1-满减/2-满赠/3-按件减阶梯/4-按件减翻倍/5-按件增阶梯/6-按件增翻倍)")
    private Short type;
    @ApiModelProperty(value = "满减金额")
    private BigDecimal reducedAmt;
    @ApiModelProperty(value = "满减优惠类型(1-金额/2-折扣)")
    private Short reducedType;
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discountedAmt;
    @ApiModelProperty(value = "满减优惠折扣")
    private BigDecimal discounted;
    @ApiModelProperty(value = "优惠最大限额")
    private BigDecimal discountedMaxAmt;
    @ApiModelProperty(value = "满赠金额")
    private BigDecimal fullGiveAmt;
    @ApiModelProperty(value = "加价金额")
    private BigDecimal increaseAmt;
    @ApiModelProperty(value = "成本")
    private BigDecimal cost;
    @ApiModelProperty(value = "按件减(阶梯/翻倍)件数")
    private Integer count;
    @ApiModelProperty(value = "按件增(翻倍)次数上限")
    private Integer times;
    @ApiModelProperty(value = "当前操作人id")
    private String userId;
    @ApiModelProperty(value = "当前操作人名称")
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
