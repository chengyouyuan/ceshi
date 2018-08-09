package com.winhxd.b2c.common.domain.backstage.store.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by caiyulong on 2018/8/4.
 */
@ApiModel("后台-门店账户管理返参")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BackStageStoreVO {

    @ApiModelProperty(value = "门店有效状态 1有效 2无效")
    private Integer storeStatus;

    @ApiModelProperty(value = "门店用户编码")
    private Long storeCustomerId;

    @ApiModelProperty(value = "门店账号")
    private String storeMobile;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "县")
    private String county;

    @ApiModelProperty(value = "乡/镇")
    private String town;

    @ApiModelProperty(value = "村")
    private String village;

    @ApiModelProperty(value = "门店地址")
    private String storeAddress;

    @ApiModelProperty(value = "联系人")
    private String shopkeeper;

    @ApiModelProperty(value = "联系方式")
    private String contactMobile;

    @ApiModelProperty(value = "支付方式（1、微信在线付款2、微信扫码付款）")
    private String paymentWay;

}
