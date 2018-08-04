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
public class SaveShopCondition {

    @ApiModelProperty(value = "用户编码", required = true)
    private Long customerId;

    @ApiModelProperty(value = "门店编码", required = true)
    private Long storeId;

    @ApiModelProperty(value = "店铺名称", required = true)
    private Long storeName;

    @ApiModelProperty(value = "取货方式", required = true)
    private Byte pickupWay;

    @ApiModelProperty(value = "支付方式", required = true)
    private Byte paymentWay;

    @ApiModelProperty(value = "店主姓名", required = true)
    private Long shopkeeper;

    @ApiModelProperty(value = "联系方式", required = true)
    private String contactMobile;

    @ApiModelProperty(value = "区域编码", required = true)
    private String regionCode;

    @ApiModelProperty(value = "门店头像", required = true)
    private String shopOwnerImg;

    @ApiModelProperty(value = "店主头像", required = true)
    private String storePhoto;
}