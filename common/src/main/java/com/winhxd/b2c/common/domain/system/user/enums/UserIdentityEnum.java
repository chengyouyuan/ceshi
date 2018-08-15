package com.winhxd.b2c.common.domain.system.user.enums;

/**
 * 用户身份枚举
 *
 * @author songkai
 * @date 2018/8/15 14:26
 * @description
 */
public enum UserIdentityEnum {
    SUPER_ADMIN(1, "超级管理员");

    private int identity;
    private String identityName;

    UserIdentityEnum(int identity, String identityName) {
        this.identity = identity;
        this.identityName = identityName;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public String getIdentityName() {
        return identityName;
    }

    public void setIdentityName(String identityName) {
        this.identityName = identityName;
    }
}
