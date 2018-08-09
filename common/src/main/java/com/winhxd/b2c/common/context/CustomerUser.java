package com.winhxd.b2c.common.context;

/**
 * C端用户数据
 */
public class CustomerUser {
    private Long customerId;
    private String openId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

   
}
