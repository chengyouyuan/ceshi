package com.winhxd.b2c.webserver.common.security;

/**
 * 权限
 * @author lixiaodong
 */
public enum Permission {
    /**
     * 订单管理
     */
    ORDER_MANAGEMENT("订单管理"),
    ORDER_MANAGEMENT_LIST("订单查询", ORDER_MANAGEMENT),
    ORDER_MANAGEMENT_EDIT("订单编辑", ORDER_MANAGEMENT),


    SYSTEM_MANAGEMENT("系统管理"),
    SYSTEM_MANAGEMENT_ROLE("角色管理", SYSTEM_MANAGEMENT),
    SYSTEM_MANAGEMENT_USER("用户管理", SYSTEM_MANAGEMENT);

    private String name;
    private Permission parent;

    Permission(String name) {
        this.name = name;
    }

    Permission(String name, Permission parent) {
        this.name = name;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public Permission getParent() {
        return parent;
    }
}
