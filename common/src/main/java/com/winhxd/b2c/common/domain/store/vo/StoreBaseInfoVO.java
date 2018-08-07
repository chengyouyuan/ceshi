package com.winhxd.b2c.common.domain.store.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liutong
 * @date 2018-08-04 16:32:19
 */
@Data
@ApiModel("惠小店开店验证返参")
public class StoreBaseInfoVO {

    @ApiModelProperty(value = "门店编码", required = true)
    private Long storeId;

    @ApiModelProperty(value = "门店名称", required = true)
    private String storeName;

    @ApiModelProperty(value = "取货地址", required = true)
    private String storeAddress;

    @ApiModelProperty(value = "店主姓名", required = true)
    private String shopkeeper;

}
