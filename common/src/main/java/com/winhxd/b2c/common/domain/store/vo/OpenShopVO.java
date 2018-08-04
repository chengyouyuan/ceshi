package com.winhxd.b2c.common.domain.store.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 惠小店开店验证返参
 *
 * @author liutong
 * @date 2018-08-03 11:14:18
 */
@ApiModel("惠小店开店验证返参")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OpenShopVO {

    @ApiModelProperty(value = "是否创建过了惠小店，0未创建，1已创建", required = true)
    private Byte shopStatus;

    @ApiModelProperty(value = "门店是否完善全部信息，0未完善，1已完善", required = true)
    private Byte perfectStatus;

    @ApiModelProperty(value = "未完善信息时，提示列表")
    private List<String> noPerfectMessage;

    @ApiModelProperty(value = "门店编码", required = true)
    private Long storeId;

    @ApiModelProperty(value = "门店头像")
    private String storePhoto;

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
    private String regionCode;

}
