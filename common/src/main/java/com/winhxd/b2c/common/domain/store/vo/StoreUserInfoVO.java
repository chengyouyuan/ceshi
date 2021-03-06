package com.winhxd.b2c.common.domain.store.vo;

import com.winhxd.b2c.common.domain.store.enums.StoreBindingStatus;
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

    @ApiModelProperty("门店简称")
    private String storeShortName;

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
    @ApiModelProperty("1、到店自提  2：送货上门  多个用逗号分隔")
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

    /**支付方式*/
    @ApiModelProperty("支付方式（1、微信在线付款2、微信扫码付款，多个用逗号分隔）")
    private String payType;

    /**门头照地址*/
    @ApiModelProperty("门头照地址")
    private String storePicImg;

    /** StoreStatusEnum*/
    @ApiModelProperty("惠小店状态（0、未开店，1、有效，2、无效）")
    private Short storeStatus;

    /** StoreBindingStatus*/
    @ApiModelProperty(value = "处理绑定状态(0:未绑定门店 1:初次绑定该门店，2.已经绑定过该门店，3.已绑过其它门店)")
    private Short bindingStatus;

    /**微信openID*/
    @ApiModelProperty("微信openid")
    private String openid;

    /**微信昵称*/
    @ApiModelProperty("微信昵称")
    private String wechatName;

    @ApiModelProperty(value = "推送优惠券状态 0 不可推送  1 可推送")
    private Short pushCouponStatus;

}