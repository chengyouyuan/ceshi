package com.winhxd.b2c.common.domain.promotion.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author sjx
 * @date 2018/8/17
 */
@ApiModel("优惠券活动导入小店信息")
@Data
public class CouponActivityImportStoreVO {
    @ApiModelProperty(value = "小店ID")
    private String storeId;

    @ApiModelProperty(value = "小店名字")
    private String storeName;
}
