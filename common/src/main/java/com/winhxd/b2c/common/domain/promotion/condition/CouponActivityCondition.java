package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.common.PagedCondition;
import com.winhxd.b2c.common.domain.common.inputmodel.DateInterval;
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
public class CouponActivityCondition extends PagedCondition implements Serializable {

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

    @ApiModelProperty(value = "1领券2推券")
    private Short type;

    @ApiModelProperty(value = "创建人")
    private String createdByName;

    @ApiModelProperty(value = "创建人")
    private Long createdBy;

    @ApiModelProperty(value = "创建时间开始")
    private Date createdStart;

    @ApiModelProperty(value = "创建时间结束")
    private Date createdEnd;

    @ApiModelProperty(value = "创建时间")
    private DateInterval dateInterval;

}
