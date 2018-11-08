package com.winhxd.b2c.common.domain.customer.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("用户收货地址信息")
public class CustomerAddressVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("地址所属用户")
    private Long customerId;

    @ApiModelProperty("收货人姓名")
    private String contacterName;

    @ApiModelProperty("收货人手机号")
    private String contacterMobile;

    @ApiModelProperty("收货人所属省")
    private String contacterProvince;

    @ApiModelProperty("收货人所属市")
    private String contacterCity;

    @ApiModelProperty("收货人所属区")
    private String contacterCounty;

    @ApiModelProperty("收货人详细地址")
    private String contacterDetailAddress;

    @ApiModelProperty("用户标签ID")
    private Long labelId;

    @ApiModelProperty("1：默认地址  0不是默认")
    private Integer defaultAddress;

    @ApiModelProperty("创建时间")
    private Date created;

    @ApiModelProperty("更新时间")
    private Date updated;

}