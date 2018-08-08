package com.winhxd.b2c.common.domain.store.condition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class StoreBusinessInfoCondition {

    @ApiModelProperty(value = "门店编码", required = true)
    private Long storeCustomerId;

    @ApiModelProperty(value = "店铺名称", required = true)
    private String storeName;

    @ApiModelProperty(value = "取货方式", required = true)
    private Byte pickupWay;

    @ApiModelProperty(value = "支付方式", required = true)
    private Byte paymentWay;

    @ApiModelProperty(value = "店主姓名", required = true)
    private String shopkeeper;

    @ApiModelProperty(value = "联系方式", required = true)
    private String contactMobile;

    @ApiModelProperty(value = "取货地址", required = true)
    private String storeAddress;

}
