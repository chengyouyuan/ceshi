package com.winhxd.b2c.common.domain.backstage.store.condition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.winhxd.b2c.common.domain.base.condition.BaseCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 后台门店账户管理请求参数
 *
 * @author caiyulong
 * @date 2018/8/4
 */
@ApiModel("后台门店账户管理请求参数")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BackStageStoreInfoCondition extends BaseCondition{

    @ApiModelProperty(value = "门店有效状态 1有效 2无效")
    private Byte storeStatus;

    @ApiModelProperty(value = "门店编码")
    private Long storeId;

    @ApiModelProperty(value = "门店账号")
    private String storeMobile;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "省code")
    private String provinceCode;

    @ApiModelProperty(value = "市code")
    private String cityCode;

    @ApiModelProperty(value = "县code")
    private String countyCode;

    @ApiModelProperty(value = "乡/镇code")
    private String townCode;

    @ApiModelProperty(value = "村code")
    private String villageCode;

    @ApiModelProperty(value = "用户编码")
    private Long customerId;

    @ApiModelProperty(value = "门店地址")
    private String storeAddress;

    @ApiModelProperty(value = "联系方式")
    private String contactMobile;

    @ApiModelProperty(value = "支付方式（1、微信在线付款2、微信扫码付款）")
    private Byte paymentWay;

    @ApiModelProperty(value = "筛选的最小级别reginCode")
    private String reginCode;

}
