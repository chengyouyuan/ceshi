package com.winhxd.b2c.common.domain.store.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wufuyun
 * @date 2018年8月2日 下午3:43:57
 * @Description 门店信息
 */
@ApiModel("门店信息")
@Data
public class StoreUserInfoVO {
    /**
     * 主键
     */
    @ApiModelProperty("id主键")
    private Long id;
    /**
     * 门店名称
     */
    @ApiModelProperty("门店名称")
    private String storeName;

    /**
     * 店主头像
     */
    @ApiModelProperty("店主头像")
    private String shopOwnerImg;

    /**
     * 店主名称
     */
    @ApiModelProperty("店主名称")
    private String shopkeeper;

    /**
     * 联系方式
     */
    @ApiModelProperty("联系方式")
    private String contactMobile;

    /**
     * 门店地址
     */
    @ApiModelProperty("门店地址")
    private String storeAddress;

    /**
     * 取货方式（1、自提，多个用逗号分隔）
     */
    @ApiModelProperty("1、自提，多个用逗号分隔")
    private String pickupType;

    /**
     * 商品的月销量
     */
    @ApiModelProperty("商品的月销量")
    private Integer monthlySales;

    /**
     * 区域编码
     */
    @ApiModelProperty("区域编码")
    private String storeRegionCode;

    /**
     * 用户账号
     */
    @ApiModelProperty("用户账号")
    private String storeMobile;

    /**门店小程序码的地址*/
    @ApiModelProperty("门店小程序码的地址")
    private String miniProgramCodeUrl;

}