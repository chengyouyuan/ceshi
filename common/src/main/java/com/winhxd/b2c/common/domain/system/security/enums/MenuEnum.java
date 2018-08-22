package com.winhxd.b2c.common.domain.system.security.enums;

/**
 * 菜单配置
 *
 * @author lixiaodong
 */
public enum MenuEnum {
    /**
     * 商品管理
     */
    ORDER_MANAGEMENT("订单管理", PermissionEnum.ORDER_MANAGEMENT),
    ORDER_MANAGEMENT_LIST("订单列表", ORDER_MANAGEMENT, PermissionEnum.ORDER_MANAGEMENT),

    /**
     * 门店管理
     */
    STORE_MANAGEMENT("门店管理", PermissionEnum.STORE_MANAGEMENT),
    STORE_MANAGEMENT_LIST("门店列表", STORE_MANAGEMENT, PermissionEnum.STORE_MANAGEMENT),
    STORE_MANAGEMENT_REGION("门店测试区域管理列表", STORE_MANAGEMENT, PermissionEnum.STORE_MANAGEMENT_REGION),


    /**
     * 商品管理
     */
    PRODUCT_MANAGEMENT("商品管理"),
    PRODUCT_MANAGEMENT_STORE("门店商品管理"),
    PRODUCT_MANAGEMENT_SUBMIT("门店提报商品管理"),
    /**
     * 系统管理
     */
    SYSTEM_MANAGEMENT("系统管理", PermissionEnum.SYSTEM_MANAGEMENT),
    SYSTEM_MANAGEMENT_ROLE("角色管理", SYSTEM_MANAGEMENT, PermissionEnum.SYSTEM_MANAGEMENT_ROLE),
    SYSTEM_MANAGEMENT_USER("用户管理", SYSTEM_MANAGEMENT, PermissionEnum.SYSTEM_MANAGEMENT_USER),

    /**
     * 财务管理
     */
    VERIFY_MANAGEMENT("财务管理"),
    VERIFY_MANAGEMENT_SUMMARY_LIST("待结算金额审核", VERIFY_MANAGEMENT, PermissionEnum.VERIFY_MANAGEMENT_VERIFY),
    VERIFY_MANAGEMENT_DETAIL_LIST("待结算费用明细", VERIFY_MANAGEMENT, PermissionEnum.VERIFY_MANAGEMENT_VERIFY),
    VERIFY_MANAGEMENT_WITHDRAWALS_LIST("提款申请", VERIFY_MANAGEMENT, PermissionEnum.VERIFY_MANAGEMENT_VERIFY);



    private String name;
    private MenuEnum parent;
    private PermissionEnum[] permissions;

    MenuEnum(String name, PermissionEnum... permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    MenuEnum(String name, MenuEnum parent, PermissionEnum... permissions) {
        this.name = name;
        this.parent = parent;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public MenuEnum getParent() {
        return parent;
    }

    public PermissionEnum[] getPermissions() {
        return permissions;
    }
}
