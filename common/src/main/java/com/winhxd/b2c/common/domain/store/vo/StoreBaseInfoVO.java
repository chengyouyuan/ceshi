package com.winhxd.b2c.common.domain.store.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 惠小店开店基础信息查询返参
 *
 * @author liutong
 * @date 2018-08-04 16:32:19
 */
@Data
@ApiModel("惠小店开店基础信息查询返参")
public class StoreBaseInfoVO {

    @ApiModelProperty(value = "门店编码", required = true)
    private Long storeCustomerId;

    @ApiModelProperty(value = "门店头像")
    private String storeImg;

    @ApiModelProperty(value = "门店名称", required = true)
    private String storeName;

    @ApiModelProperty(value = "店主姓名", required = true)
    private String shopkeeper;

    @ApiModelProperty(value = "店主头像")
    private String shopOwnerImg;

    @ApiModelProperty(value = "省", required = true)
    private String province;

    @ApiModelProperty(value = "市", required = true)
    private String city;

    @ApiModelProperty(value = "区县", required = true)
    private String county;

    @ApiModelProperty(value = "区域编码", required = true)
    private String storeRegionCode;

    @ApiModelProperty(value = "联系方式", required = true)
    private String contactMobile;

    @ApiModelProperty(value = "门店地址", required = true)
    private String storeAddress;

}
