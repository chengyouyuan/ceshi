package com.winhxd.b2c.common.mq.support;

/**
 * MQ配置文件
 *
 * @author lixiaodong
 */
public class MessageQueueProperties {
    private String address;
    private String username;
    private String password;
    private String virtualHost;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }
}
