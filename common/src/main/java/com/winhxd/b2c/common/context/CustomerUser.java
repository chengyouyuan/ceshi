package com.winhxd.b2c.common.context;

/**
 * C端用户数据
 */
public class CustomerUser {
    /**
     * C端用户主键
     */
    private Long customerId;
    /**
     * C端用户微信openid
     */
    private String openid;
    /**
     * 用于指定微服务版本
     */
    private String msVer;

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

    public String getMsVer() {
        return msVer;
    }

    public void setMsVer(String msVer) {
        this.msVer = msVer;
    }
}
