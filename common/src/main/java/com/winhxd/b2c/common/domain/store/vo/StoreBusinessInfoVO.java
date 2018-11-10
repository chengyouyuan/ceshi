package com.winhxd.b2c.common.domain.store.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

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

    @ApiModelProperty(value = "店铺名称", required = true)
    private String storeName;

    @ApiModelProperty(value = "店铺简称", required = true)
    private String storeShortName;

    @ApiModelProperty(value = "店主姓名", required = true)
    private String shopkeeper;

    @ApiModelProperty(value = "取货方式（1:到店自提;2:送货上门）", required = true)
    private List<StoreEnumObject> pickupType;

    @ApiModelProperty(value = "支付方式列表（1、微信在线付款2、微信扫码付款）", required = true)
    private List<StoreEnumObject> payType;

    @ApiModelProperty(value = "联系方式", required = true)
    private String contactMobile;

    @ApiModelProperty(value = "取货地址", required = true)
    private String storeAddress;

}
