package com.winhxd.b2c.webserver.common.security;

/**
 * 菜单配置
 *
 * @author lixiaodong
 */
public enum Menu {
    /**
     * 商品管理
     */
    ORDER_MANAGEMENT("订单管理", Permission.ORDER_MANAGEMENT),
    ORDER_MANAGEMENT_LIST("订单列表", ORDER_MANAGEMENT, Permission.ORDER_MANAGEMENT),


    /**
     * 系统管理
     */
    SYSTEM_MANAGEMENT("系统管理", Permission.SYSTEM_MANAGEMENT),
    SYSTEM_MANAGEMENT_ROLE("角色管理", SYSTEM_MANAGEMENT, Permission.SYSTEM_MANAGEMENT_ROLE),
    SYSTEM_MANAGEMENT_USER("用户管理", SYSTEM_MANAGEMENT, Permission.SYSTEM_MANAGEMENT_USER);


    private String name;
    private Menu parent;
    private Permission[] permissions;

    Menu(String name, Permission... permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    Menu(String name, Menu parent, Permission... permissions) {
        this.name = name;
        this.parent = parent;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public Menu getParent() {
        return parent;
    }

    public Permission[] getPermissions() {
        return permissions;
    }
}
