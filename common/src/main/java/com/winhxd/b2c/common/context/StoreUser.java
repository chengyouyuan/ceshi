package com.winhxd.b2c.common.context;

/**
 * 门店用户数据
 */
public class StoreUser {
    /**
     * 门店用户主键
     */
    private Long businessId;
    /**
     * 惠下单门店用户customerId(对应CRM_WS_CUSTOMER)
     */
    private Long storeCustomerId;
    /**
     * 用于指定微服务版本
     */
    private String msVer;

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Long getStoreCustomerId() {
        return storeCustomerId;
    }

    public void setStoreCustomerId(Long storeCustomerId) {
        this.storeCustomerId = storeCustomerId;
    }

    public String getMsVer() {
        return msVer;
    }

    public void setMsVer(String msVer) {
        this.msVer = msVer;
    }
}