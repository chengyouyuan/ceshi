package com.winhxd.b2c.common.domain.customer.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: sunwenwu
 * @Date: 2018/11/8 14：26
 * @Description:
 */
@ApiModel("保存C端用户收货地址-请求参数")
@Data
public class CustomerAddressCondition extends ApiCondition {

    @ApiModelProperty(value = "主键",required = false)
    private Long id;

    @ApiModelProperty(value = "收货人姓名",required = true)
    private String contacterName;

    @ApiModelProperty(value = "收货人手机号",required = true)
    private String contacterMobile;

    @ApiModelProperty("收货人所属省")
    private String contacterProvince;

    @ApiModelProperty("收货人所属省code")
    private String contacterProvinceCode;

    @ApiModelProperty("收货人所属市")
    private String contacterCity;

    @ApiModelProperty("收货人所属市code")
    private String contacterCityCode;

    @ApiModelProperty("收货人所属区")
    private String contacterCounty;

    @ApiModelProperty("收货人所属区code")
    private String contacterCountyCode;

    @ApiModelProperty(value = "收货人详细地址",required = true)
    private String contacterDetailAddress;

    @ApiModelProperty(value = "收货人所选标签",required = false)
    private Long labelId;

    @ApiModelProperty(value = "是否设置为默认地址 true：默认  false不是默认",required = false)
    private Boolean defaultAddress;
}
