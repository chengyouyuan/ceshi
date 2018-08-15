package com.winhxd.b2c.common.context;

/**
 * C端用户数据
 */
public class CustomerUser {
    private Long customerId;
    private String openid;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

   
}
