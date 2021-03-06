package com.winhxd.b2c.common.domain.store.condition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 惠小店营业信息保存入参
 *
 * @author liutong
 * @date 2018-08-03 14:22:44
 */
@ApiModel("惠小店营业信息保存入参")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StoreBusinessInfoCondition extends ApiCondition {

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "店铺简称")
    private String storeShortName;

    @ApiModelProperty(value = "取货方式（1、到店自提  2:送货上门 多个用逗号分隔  ）", required = true)
    private String pickupType;

    @ApiModelProperty(value = "支付方式（1、微信在线付款2、微信扫码付款，多个用逗号分隔）", required = true)
    private String payType;

    @ApiModelProperty(value = "店主姓名", required = true)
    private String shopkeeper;

    @ApiModelProperty(value = "联系方式", required = true)
    private String contactMobile;

    @ApiModelProperty(value = "取货地址", required = true)
    private String storeAddress;

}
