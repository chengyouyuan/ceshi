package com.winhxd.b2c.common.domain.promotion.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *
 * @author sjx
 * @date 2018/8/9
 */
@ApiModel("优惠券活动关联优惠券及门店")
@Data
public class CouponActivityStoreVO {

    @ApiModelProperty(value = "优惠券活动ID")
    private Long id;

    @ApiModelProperty(value = "活动编码")
    private String activityCode;

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "状态")
    private Short status;

    @ApiModelProperty(value = "状态")
    private String statusName;

    @ApiModelProperty(value = "优惠券标题")
    private String couponName;

    @ApiModelProperty(value = "优惠券编码")
    private String couponCode;

    @ApiModelProperty(value = "小店名称")
    @Excel(name = "惠小店名称", orderNum = "2", width = 30)
    private String storeName;

    @ApiModelProperty(value = "小店ID")
    @Excel(name = "惠小店ID", orderNum = "1", width = 30)
    private String storeId;

    @ApiModelProperty(value = "小店账号")
    private String storeMobile;

    @ApiModelProperty(value = "修改时间")
    private Date updated;

}
