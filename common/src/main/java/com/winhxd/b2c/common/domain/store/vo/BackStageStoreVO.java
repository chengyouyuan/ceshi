package com.winhxd.b2c.common.domain.store.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
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

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "门店有效状态 1有效 2无效")
    private Short storeStatus;

    @ApiModelProperty(value = "门店状态描述 1有效 2无效")
    @Excel(name = "门店状态", width = 30)
    private String storeStatusDesc;

    @ApiModelProperty(value = "门店用户编码")
    @Excel(name = "门店编码", width = 30)
    private Long storeCustomerId;

    @ApiModelProperty(value = "门店账号")
    @Excel(name = "门店账号", width = 30)
    private String storeMobile;

    @ApiModelProperty(value = "门店名称")
    @Excel(name = "门店名称", width = 30)
    private String storeName;

    @ApiModelProperty(value = "省")
    @Excel(name = "省", width = 30)
    private String province;

    @ApiModelProperty(value = "市")
    @Excel(name = "市", width = 30)
    private String city;

    @ApiModelProperty(value = "县")
    @Excel(name = "县", width = 30)
    private String county;

    @ApiModelProperty(value = "乡/镇")
    @Excel(name = "乡/镇", width = 30)
    private String town;

    @ApiModelProperty(value = "村")
    @Excel(name = "村", width = 30)
    private String village;

    @ApiModelProperty(value = "门店地址")
    @Excel(name = "地址", width = 30)
    private String storeAddress;

    @ApiModelProperty(value = "联系人")
    @Excel(name = "联系人", width = 30)
    private String shopkeeper;

    @ApiModelProperty(value = "联系方式")
    private String contactMobile;

    @ApiModelProperty(value = "支付方式（1、微信在线付款2、微信扫码付款）")
    private String payType;

    @ApiModelProperty(value = "支付方式描述（1、微信在线付款2、微信扫码付款）")
    @Excel(name = "支付方式", width = 30)
    private String payTypeDesc;

    @ApiModelProperty(value = "取货方式列表（1、自提）")
    private String pickupType;

    @ApiModelProperty(value = "取货方式描述（1、自提）")
    private String pickupTypeDesc;
}
