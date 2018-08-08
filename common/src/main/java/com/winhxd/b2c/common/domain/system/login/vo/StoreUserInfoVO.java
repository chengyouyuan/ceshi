package com.winhxd.b2c.common.domain.system.login.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wufuyun
 * @date  2018年8月2日 下午3:43:57
 * @Description 门店信息
 * @version
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
     * 门店编码
     */
    @ApiModelProperty("门店编码")
    private Long storeId;
    /**
     * 用户账号
     */
    private String storeMobile;
    /**
     * 门店地址
     */
    @ApiModelProperty("门店地址")
    private String storeAddress;
    /**
     * 区域编码
     */
    @ApiModelProperty("区域编码")
    private String storeRegionCode;
    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String storePassword;
    /**
     * 店主名称
     */
    @ApiModelProperty("店主名称")
    private String shopkeeper;
    /**
     * 店主头像
     */
    @ApiModelProperty("店主头像")
    private String shopOwnerUrl;
    /**
     * 取货方式（1、自提，多个用逗号分隔）
     */
    @ApiModelProperty("1、自提，多个用逗号分隔")
    private String pickupWay;
    /**
     * 支付方式（1、微信在线付款2、微信扫码付款，多个用逗号分隔）
     */
    @ApiModelProperty("支付方式（1、微信在线付款2、微信扫码付款，多个用逗号分隔）")
    private String paymentWay;
    /**
     * 联系方式 
     */
    @ApiModelProperty("contactMobile")
    private String contactMobile;
    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private Double lat;
    /**
     * 经度
     */
    @ApiModelProperty("经度")
    private Double lon;
    /**
     * 微信openid
     */
    private String openid;
    private Date created;
    private Long createdBy;
    private String createdByName;
    private Date updated;
    private Long updatedBy;
    private String updatedByName;
    /**
     * 来源
     */
    @ApiModelProperty("来源")
    private String source;
    /**
     * 惠小店状态（0、未开店，1、有效，2、无效）
     */
    @ApiModelProperty("惠小店状态（0、未开店，1、有效，2、无效）")
    private Byte storeStatus;


}