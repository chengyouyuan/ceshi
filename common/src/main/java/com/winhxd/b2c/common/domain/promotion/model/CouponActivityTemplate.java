package com.winhxd.b2c.common.domain.promotion.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author sjx
 */
@Data
@ApiModel(value = "优惠券活动模板",description = "优惠券活动模板")
public class CouponActivityTemplate {

    @ApiModelProperty(value = "关联id")
    private Long id;

    @ApiModelProperty(value = "活动id")
    private Long couponActivityId;

    @ApiModelProperty(value = "优惠券模板id")
    private Long templateId;

    @ApiModelProperty(value = "数量")
    private Integer count;

    @ApiModelProperty(value = "优惠券开始时间")
    private Date startTime;

    @ApiModelProperty(value = "优惠券结束时间")
    private Date endTime;

    @ApiModelProperty(value = "优惠券有效期")
    private Integer effectiveDays;

    @ApiModelProperty(value = "优惠券数量的限制 1优惠券总数2每个门店优惠券数")
    private Short couponNumType;

    @ApiModelProperty(value = "限制数量")
    private Integer couponNum;

    @ApiModelProperty(value = "用户领券限制 1不限制 2限制")
    private Short customerVoucherLimitType;

    @ApiModelProperty(value = "用户可领取数量")
    private Integer customerVoucherLimitNum;

    @ApiModelProperty(value = "推送数量")
    private Integer sendNum;

    @ApiModelProperty(value = "发券时间")
    private Date sendTime;

    @ApiModelProperty(value = "是否有效0无效1有效")
    private Short status;

    @ApiModelProperty(value = "优惠券对应的门店用户集合")
    private List<CouponActivityStoreCustomer> couponActivityStoreCustomerList;

}