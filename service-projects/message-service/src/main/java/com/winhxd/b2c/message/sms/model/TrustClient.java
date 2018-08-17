package com.winhxd.b2c.message.sms.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 信任客户端列表
 *
 * @author yaoshuai
 */
public class TrustClient implements Serializable {

    private static final long serialVersionUID = -2073943184104016735L;

    private Long id;
    private String ip;
    private Date createDate;
    private long storeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

}
