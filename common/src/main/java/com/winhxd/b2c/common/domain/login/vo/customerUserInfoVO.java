package com.winhxd.b2c.common.domain.login.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author wufuyun
 * @date  2018年8月2日 下午4:38:25
 * @Description 
 * @version
 */
@ApiModel("小程序登录返参")
@Data
public class customerUserInfoVO implements Serializable {
	@ApiModelProperty(value = "用户主键")
    private Long customerId;
	@ApiModelProperty(value = "账号")
    private String customerMobile;
    
    private static final long serialVersionUID = 1L;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile == null ? null : customerMobile.trim();
    }

}