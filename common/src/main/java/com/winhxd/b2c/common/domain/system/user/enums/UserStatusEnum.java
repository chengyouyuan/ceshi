package com.winhxd.b2c.common.domain.system.user.enums;

/**
 * 用户状态
 * @author zhangzhengyang
 * @date 2018/8/2
 */
public enum UserStatusEnum {

    ENABLED(new Short("1"), "启用"),
    DISABLED(new Short("0"), "未启用");

    private Short code;
    private String desc;

    UserStatusEnum(Short code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Short getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
