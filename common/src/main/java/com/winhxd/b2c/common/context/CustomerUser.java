package com.winhxd.b2c.common.context;

/**
 * C端用户数据
 */
public class CustomerUser {
    private Long customerId;
    private String customerMobile;

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
        this.customerMobile = customerMobile;
    }
}
