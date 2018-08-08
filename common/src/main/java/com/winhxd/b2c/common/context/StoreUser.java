package com.winhxd.b2c.common.context;

/**
 * 门店用户数据
 */
public class StoreUser {
    private Long BusinessId;
    private Long storeId;


    public Long getBusinessId() {
		return BusinessId;
	}

	public void setBusinessId(Long businessId) {
		BusinessId = businessId;
	}

	public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
}
