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

    @ApiModelProperty(value = "收货人所属区域",required = true)
    private String contacterRegion;

    @ApiModelProperty(value = "收货人详细地址",required = true)
    private String contacterDetailAddress;

    @ApiModelProperty(value = "收货人所选标签",required = true)
    private Long labelId;
}
