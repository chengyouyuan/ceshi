package com.winhxd.b2c.common.domain.promotion.vo;

import com.winhxd.b2c.common.domain.product.vo.BrandVO;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * @Author wl
 * @Date 2018/8/13 11:09
 * @Description  显示门店下优惠券的领取数量和使用数量
 **/
public class CouponInStoreGetedAndUsedVO {
    @ApiModelProperty(value = "模板id")
    private Long templeteId;
    @ApiModelProperty(value = "门店id")
    private Long storeId;
    @ApiModelProperty(value = "活动id")
    private Long couponActivityId;
    @ApiModelProperty(value = "领取数量")
    private Integer totalCount ;
    @ApiModelProperty(value = "使用数量")
    private Integer usedCount;
    @ApiModelProperty(value = "满减优惠类型(1-金额/2-折扣)")
    private Short reduceType;
    @ApiModelProperty(value = "满减金额")
    private Float reduceAmt;
    @ApiModelProperty(value = "成本")
    private Float cost ;
    @ApiModelProperty(value = "优惠金额")
    private Float discountedAmt ;
    @ApiModelProperty(value = "满减优惠折扣")
    private Float discounted;
    @ApiModelProperty(value = "优惠最大限额")
    private Float maxAmt;
    @ApiModelProperty(value = "有效期开始时间")
    private String startTime;
    @ApiModelProperty(value = "有效期结束时间")
    private String endTime;
    @ApiModelProperty(value = "适用对象类型")
    private Short applyRuleType;
    @ApiModelProperty(value = "适用对象类型")
    private Long applyId;
    @ApiModelProperty(value = "适用品牌信息")
    private List<BrandVO> brands;
    @ApiModelProperty(value = "适用商品信息")
    private List<ProductSkuVO> products;
    @ApiModelProperty(value = "是否过期  0已过期  1未过期 2快过期")
    private Short expire;


    @ApiModelProperty(value = "logo地址")
    private String logoUrl;
    @ApiModelProperty(value = "已推给数量")
    private Integer pushCount;


    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Integer getPushCount() {
        return pushCount;
    }

    public void setPushCount(Integer pushCount) {
        this.pushCount = pushCount;
    }

    public Long getTempleteId() {
        return templeteId;
    }

    public void setTempleteId(Long templeteId) {
        this.templeteId = templeteId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Long getCouponActivityId() {
        return couponActivityId;
    }

    public void setCouponActivityId(Long couponActivityId) {
        this.couponActivityId = couponActivityId;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }


    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public Short getReduceType() {
        return reduceType;
    }

    public void setReduceType(Short reduceType) {
        this.reduceType = reduceType;
    }

    public Float getReduceAmt() {
        return reduceAmt;
    }

    public void setReduceAmt(Float reduceAmt) {
        this.reduceAmt = reduceAmt;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Float getDiscountedAmt() {
        return discountedAmt;
    }

    public void setDiscountedAmt(Float discountedAmt) {
        this.discountedAmt = discountedAmt;
    }

    public Float getDiscounted() {
        return discounted;
    }

    public void setDiscounted(Float discounted) {
        this.discounted = discounted;
    }

    public Float getMaxAmt() {
        return maxAmt;
    }

    public void setMaxAmt(Float maxAmt) {
        this.maxAmt = maxAmt;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Short getApplyRuleType() {
        return applyRuleType;
    }

    public void setApplyRuleType(Short applyRuleType) {
        this.applyRuleType = applyRuleType;
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

    public Short getExpire() {
        return expire;
    }

    public void setExpire(Short expire) {
        this.expire = expire;
    }
}
