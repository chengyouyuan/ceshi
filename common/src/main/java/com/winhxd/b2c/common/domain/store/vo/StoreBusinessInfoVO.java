package com.winhxd.b2c.common.domain.store.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 惠小店基础信息保存返参
 *
 * @author liutong
 * @date 2018-08-03 14:22:44
 */
@ApiModel("惠小店开店店铺信息保存返参")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StoreBusinessInfoVO {

    @ApiModelProperty(value = "门店编码", required = true)
    private Long storeCustomerId;

    @ApiModelProperty(value = "店铺名称", required = true)
    private String storeName;

    @ApiModelProperty(value = "店主姓名", required = true)
    private String shopkeeper;

    @ApiModelProperty(value = "取货地址", required = true)
    private String storeAddress;

}