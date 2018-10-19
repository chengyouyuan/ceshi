package com.winhxd.b2c.common.domain.promotion.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.winhxd.b2c.common.domain.product.vo.BrandVO;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class CouponPushVO {
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
    @ApiModelProperty(value = "优惠券金额计算方式1订单金额 2商品金额", required=true)
    private String payType;
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
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy.MM.dd")
    private Date activityStart;
    @ApiModelProperty(value = "结束时间", required=true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy.MM.dd")
    private Date activityEnd;
    @ApiModelProperty(value = "优惠券状态状态(0-无效,1-已使用，2-未使用， 3-已过期,4-退回", required=true)
    private String useStatus;
    @ApiModelProperty(value = "优惠券是否可领取 0 已领取,1 可领取,2已领完", required=true)
    private String receiveStatus;
    @ApiModelProperty(value = "优惠券数量", required=true)
    private Integer sendNum;
    @ApiModelProperty(value = "（品牌、品牌商）logo")
    private String logoImg;
    @ApiModelProperty(value = "每种优惠券总数量")
    private Integer couponNum;
    @ApiModelProperty(value = "是否领取 0 :未领取 1：已领取")
    private Integer receive;
    @ApiModelProperty(value = "优惠券有效期")
    private Integer effectiveDays;
    @ApiModelProperty(value = "适用品牌信息")
    private List<BrandVO> brands;
    @ApiModelProperty(value = "适用商品信息")
    private List<ProductSkuVO> products;
}
