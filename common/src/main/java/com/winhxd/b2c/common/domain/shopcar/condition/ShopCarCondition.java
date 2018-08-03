package com.winhxd.b2c.common.domain.shopcar.condition;

import com.winhxd.b2c.common.domain.condition.BaseCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @auther: wangbaokuo
 * @date: 2018/8/3 09:29
 * @description:
 */
@ApiModel("用户加购传参")
public class ShopCarCondition extends BaseCondition{

    @ApiModelProperty(value = "门店ID")
    private Long storeId;

    @ApiModelProperty(value = "自提地址")
    private String extractAddress;

    @ApiModelProperty(value = "自提时间")
    private Date extractTime;

    @ApiModelProperty(value = "商品sku")
    private String skuCode;

    @ApiModelProperty(value = "商品数量")
    private Integer prodNum;

    @ApiModelProperty(value = "支付方式(1:微信在线付款,2微信扫码付款)")
    private Byte payType;

    @ApiModelProperty(value = "优惠券ID")
    private Long couponId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "金额")
    private BigDecimal orderTotalMoney;

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getExtractAddress() {
		return extractAddress;
	}

	public void setExtractAddress(String extractAddress) {
		this.extractAddress = extractAddress;
	}

	public Date getExtractTime() {
		return extractTime;
	}

	public void setExtractTime(Date extractTime) {
		this.extractTime = extractTime;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public Integer getProdNum() {
		return prodNum;
	}

	public void setProdNum(Integer prodNum) {
		this.prodNum = prodNum;
	}

	public Byte getPayType() {
		return payType;
	}

	public void setPayType(Byte payType) {
		this.payType = payType;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getOrderTotalMoney() {
		return orderTotalMoney;
	}

	public void setOrderTotalMoney(BigDecimal orderTotalMoney) {
		this.orderTotalMoney = orderTotalMoney;
	}
    
    
    
}
