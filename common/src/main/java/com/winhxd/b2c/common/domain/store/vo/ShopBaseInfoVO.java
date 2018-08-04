package com.winhxd.b2c.common.domain.store.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author liutong
 * @date 2018-08-04 16:32:19
 */
public class ShopBaseInfoVO {

    @ApiModelProperty(value = "门店编码", required = true)
    private Long storeId;

    @ApiModelProperty(value = "门店名称", required = true)
    private String storeName;

}
