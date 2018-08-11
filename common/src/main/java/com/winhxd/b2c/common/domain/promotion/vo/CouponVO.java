package com.winhxd.b2c.common.domain.promotion.vo;

import com.winhxd.b2c.common.domain.product.vo.BrandVO;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/6 10 56
 * @Description
 */
public class CouponVO {
    @ApiModelProperty(value = "活动id", required=true)
    private Long activityId;
    @ApiModelProperty(value = "优惠券id", required=true)
    private Long templateId;
    @ApiModelProperty(value = "优惠券发放id", required=true)
    private Long sendId;
    @ApiModelProperty(value = "适用规则id", required=true)
    private Long applyId;
    @ApiModelProperty(value = "是否有效 0 无效 1 有效", required=true)
    private String status;
    @ApiModelProperty(value = "无效原因", required=true)
    private String reason;
    @ApiModelProperty(value = "优惠券金额计算方式1订单金额 2商品金额", required=true)
    private String payType;
    @ApiModelProperty(value = "支付方式1扫码支付2线上支付", required=true)
    private String calType;
    @ApiModelProperty(value = "坎级类型 1-满减/2-满赠", required=true)
    private String type;
    @ApiModelProperty(value = "满减优惠类型(1-金额/2-折扣）", required=true)
    private String reducedType;
    @ApiModelProperty(value = "满减金额", required=true)
    private BigDecimal reducedAmt;
    @ApiModelProperty(value = "优惠金额", required=true)
    private BigDecimal discountedAmt;
    @ApiModelProperty(value = "满减优惠折扣", required=true)
    private BigDecimal discounted;
    @ApiModelProperty(value = "优惠最大限额", required=true)
    private BigDecimal discountedMaxAmt;
    @ApiModelProperty(value = "优惠券适用对象类型 1、通用 2、品牌 3、品类4、商品", required=true)
    private String applyRuleType;
    @ApiModelProperty(value = "优惠券类型 1新用户注册 2老用户活动", required=true)
    private String couponType;
    @ApiModelProperty(value = "开始时间", required=true)
    private Date activityStart;
    @ApiModelProperty(value = "结束时间", required=true)
    private Date activityEnd;
    @ApiModelProperty(value = "优惠券状态", required=true)
    private String useStatus;
    @ApiModelProperty(value = "优惠券是否可领取 0 已领取  1 可领取", required=true)
    private String receiveStatus;
    @ApiModelProperty(value = "适用品牌信息")
    private List<BrandVO> brands;
    @ApiModelProperty(value = "适用商品信息")
    private List<ProductSkuVO> products;
    @ApiModelProperty(value = "优惠券是否可用 0 不可用  1 可用", required=true)
    private String availableStatus = "0";

    private String couponNumType;

    private Integer couponNum;

    private String limitType;

    private Integer limitNum;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getCalType() {
        return calType;
    }

    public void setCalType(String calType) {
        this.calType = calType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReducedType() {
        return reducedType;
    }

    public void setReducedType(String reducedType) {
        this.reducedType = reducedType;
    }

    public BigDecimal getReducedAmt() {
        return reducedAmt;
    }

    public void setReducedAmt(BigDecimal reducedAmt) {
        this.reducedAmt = reducedAmt;
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

    public String getApplyRuleType() {
        return applyRuleType;
    }

    public void setApplyRuleType(String applyRuleType) {
        this.applyRuleType = applyRuleType;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
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

    public String getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }

    public String getCouponNumType() {
        return couponNumType;
    }

    public void setCouponNumType(String couponNumType) {
        this.couponNumType = couponNumType;
    }

    public Integer getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(Integer couponNum) {
        this.couponNum = couponNum;
    }

    public String getLimitType() {
        return limitType;
    }

    public void setLimitType(String limitType) {
        this.limitType = limitType;
    }

    public Integer getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(Integer limitNum) {
        this.limitNum = limitNum;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(String receiveStatus) {
        this.receiveStatus = receiveStatus;
    }

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public List<BrandVO> getBrands() {
        return brands;
    }

    public void setBrands(List<BrandVO> brands) {
        this.brands = brands;
    }

    public List<ProductSkuVO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductSkuVO> products) {
        this.products = products;
    }

    public Long getSendId() {
        return sendId;
    }

    public void setSendId(Long sendId) {
        this.sendId = sendId;
    }

    public String getAvailableStatus() {
        return availableStatus;
    }

    public void setAvailableStatus(String availableStatus) {
        this.availableStatus = availableStatus;
    }
}
