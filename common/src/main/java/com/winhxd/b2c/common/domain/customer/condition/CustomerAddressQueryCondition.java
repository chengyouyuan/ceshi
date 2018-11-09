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
@ApiModel("查询C端用户收货地址")
@Data
public class CustomerAddressQueryCondition{

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "收货人姓名")
    private String contacterName;

    @ApiModelProperty(value = "收货人手机号")
    private String contacterMobile;

    @ApiModelProperty(value = "收货人所属区域")
    private String contacterRegion;

    @ApiModelProperty(value = "收货人详细地址")
    private String contacterDetailAddress;

    @ApiModelProperty(value = "收货人所选标签")
    private Long labelId;

    @ApiModelProperty(value = "是否设置为默认地址 true：默认  false不是默认")
    private Boolean defaultAddress;

    @ApiModelProperty("地址所属用户")
    private Long customerId;
}
