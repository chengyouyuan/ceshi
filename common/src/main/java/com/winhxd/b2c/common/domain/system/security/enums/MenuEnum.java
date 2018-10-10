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
    PRODUCT_MANAGEMENT("商品管理", PermissionEnum.PROD_MANAGEMENT),
    PRODUCT_MANAGEMENT_STORE("门店商品管理", PRODUCT_MANAGEMENT, "/template/module/store/store_product_list.json", PermissionEnum.PROD_MANAGEMENT_STORE),
    PRODUCT_MANAGEMENT_SUBMIT("门店提报商品管理", PRODUCT_MANAGEMENT, "/template/module/store/store_submitProduct_list.json", PermissionEnum.PROD_MANAGEMENT_SUBMIT),

    /**
     * 门店管理
     */
    STORE_MANAGEMENT("门店管理", PermissionEnum.STORE_MANAGEMENT),
    STORE_MANAGEMENT_LIST("门店管理", STORE_MANAGEMENT, "/template/module/store/store_user_list.json", PermissionEnum.STORE_MANAGEMENT),
    STORE_MANAGEMENT_REGION("门店测试区域管理", STORE_MANAGEMENT, "/template/module/store/store_region_list.json", PermissionEnum.STORE_MANAGEMENT_REGION),

    /**
     * 顾客管理
     */
    CUSTOMER_MANAGEMENT("顾客管理", PermissionEnum.CUSTOMER_MANAGEMENT),
    CUSTOMER_MANAGEMENT_LIST("顾客管理", CUSTOMER_MANAGEMENT, "/template/module/customer/customer_list.json", PermissionEnum.CUSTOMER_MANAGEMENT),


    /**
     * 订单管理
     */
    ORDER_MANAGEMENT("订单管理", PermissionEnum.ORDER_MANAGEMENT),
    ORDER_MANAGEMENT_LIST("订单管理", ORDER_MANAGEMENT, "/template/module/order/order_list.json", PermissionEnum.ORDER_MANAGEMENT),

    /**
     * 财务管理
     */
    VERIFY_MANAGEMENT("财务管理", PermissionEnum.VERIFY_MANAGEMENT),
    VERIFY_MANAGEMENT_LIST("财务管理", VERIFY_MANAGEMENT, "", "/financeManage/index", PermissionEnum.VERIFY_MANAGEMENT_LIST),
    VERIFY_MANAGEMENT_SUMMARY_LIST("结算审核", VERIFY_MANAGEMENT, "/template/module/pay/pay_verify_list.json", PermissionEnum.VERIFY_MANAGEMENT_VERIFY),
    VERIFY_MANAGEMENT_DETAIL_LIST("费用明细", VERIFY_MANAGEMENT, "/template/module/pay/accounting_detail_list.json", PermissionEnum.VERIFY_MANAGEMENT_VERIFY),
    VERIFY_MANAGEMENT_WITHDRAWALS_LIST("提款申请", VERIFY_MANAGEMENT, "/template/module/pay/withdrawals_list.json", PermissionEnum.VERIFY_MANAGEMENT_VERIFY),


    /**
     * 促销管理
     */
    PROMOTION_MANAGEMENT("促销管理", PermissionEnum.PROMOTION_MANAGEMENT),
    PROMOTION_INVESTOR_RULE("出资方规则", PROMOTION_MANAGEMENT, PermissionEnum.PROMOTION_INVESTOR_RULE),
    PROMOTION_INVESTOR_RULE_ADD("出资方规则添加", PROMOTION_INVESTOR_RULE, "", "/contributiveRule/index", PermissionEnum.PROMOTION_INVESTOR_RULE_ADD),
    PROMOTION_INVESTOR_RULE_LIST("出资方规则列表", PROMOTION_INVESTOR_RULE, "/template/module/promotion/coupon_investor_list.json", PermissionEnum.PROMOTION_INVESTOR_RULE_LIST),


    PROMOTION_APPLY_RULE("适用对象规则", PROMOTION_MANAGEMENT, PermissionEnum.PROMOTION_APPLY_RULE),
    PROMOTION_APPLY_RULE_ADD("适用对象规则添加", PROMOTION_APPLY_RULE, "", "/appliyObjectRule/index", PermissionEnum.PROMOTION_APPLY_RULE_ADD),
    PROMOTION_APPLY_RULE_LIST("适用对象规则列表", PROMOTION_APPLY_RULE, "/template/module/promotion/coupon_apply_list.json", PermissionEnum.PROMOTION_APPLY_RULE_LIST),

    PROMOTION_GRADE_RULE("优惠券坎级", PROMOTION_MANAGEMENT, PermissionEnum.PROMOTION_GRADE_RULE),
    PROMOTION_GRADE_RULE_ADD("坎级规则添加", PROMOTION_GRADE_RULE, "", "/promotion/candyruleAdd", PermissionEnum.PROMOTION_GRADE_RULE_ADD),
    PROMOTION_GRADE_RULE_LIST("坎级规则列表", PROMOTION_GRADE_RULE, "/template/module/promotion/coupon_grade_list.json", PermissionEnum.PROMOTION_GRADE_RULE_LIST),

    PROMOTION_TEMPLETE_MANAGEMENT("优惠券模板管理", PROMOTION_MANAGEMENT, PermissionEnum.PROMOTION_TEMPLETE_MANAGEMENT),
    PROMOTION_TEMPLETE_MANAGEMENT_ADD("模版新增", PROMOTION_TEMPLETE_MANAGEMENT, "", "/promotion/addtemplate", PermissionEnum.PROMOTION_TEMPLETE_MANAGEMENT_ADD),
    PROMOTION_TEMPLETE_MANAGEMENT_LIST("模版列表", PROMOTION_TEMPLETE_MANAGEMENT, "/template/module/promotion/coupon_templete_list.json", PermissionEnum.PROMOTION_TEMPLETE_MANAGEMENT_LIST),

    PROMOTION_ACTIVITY_PULL("优惠券领券管理", PROMOTION_MANAGEMENT, PermissionEnum.PROMOTION_ACTIVITY_PULL),
    PROMOTION_ACTIVITY_PULL_ADD("优惠券领券管理", PROMOTION_ACTIVITY_PULL, "", "/couponReceive/index", PermissionEnum.PROMOTION_ACTIVITY_PULL_ADD),
    PROMOTION_ACTIVITY_PULL_LIST("领券管理列表", PROMOTION_ACTIVITY_PULL, "/template/module/promotion/coupon_activity_pull_list.json", PermissionEnum.PROMOTION_ACTIVITY_PULL_LIST),

    PROMOTION_ACTIVITY_PUSH("优惠券推券管理", PROMOTION_MANAGEMENT, PermissionEnum.PROMOTION_ACTIVITY_PUSH),
    PROMOTION_ACTIVITY_PUSH_ADD("优惠券推券管理", PROMOTION_ACTIVITY_PUSH, "", "/promotion/pushvolume", PermissionEnum.PROMOTION_ACTIVITY_PUSH_ADD),
    PROMOTION_ACTIVITY_PUSH_LIST("推券管理列表", PROMOTION_ACTIVITY_PUSH, "/template/module/promotion/coupon_activity_push_list.json", PermissionEnum.PROMOTION_ACTIVITY_PUSH_LIST),

    /***
     * 消息管理
     */
    MESSAGE_MANAGEMENT("消息管理", PermissionEnum.MESSAGE_MANAGEMENT),
    MESSAGE_MANAGEMENT_LIST("消息推送列表查询", MESSAGE_MANAGEMENT, "/template/module/message/message_batch_push_list.json", PermissionEnum.MESSAGE_MANAGEMENT_LIST),

    /**
     * 系统管理
     */
    SYSTEM_MANAGEMENT("系统管理", PermissionEnum.SYSTEM_MANAGEMENT),
    SYSTEM_MANAGEMENT_ROLE("权限管理", SYSTEM_MANAGEMENT, "/template/module/system/system_role_list.json", PermissionEnum.SYSTEM_MANAGEMENT_ROLE),
    SYSTEM_MANAGEMENT_USER("成员管理", SYSTEM_MANAGEMENT, "/template/module/system/system_user_list.json", PermissionEnum.SYSTEM_MANAGEMENT_USER),
    SYSTEM_MANAGEMENT_DICT("字典管理", SYSTEM_MANAGEMENT, "/template/module/system/system_dict_list.json", PermissionEnum.SYSTEM_MANAGEMENT_DICT),

    /**
     * 监测监控管理
     */
    DETECTION_MANAGEMENT("监控管理", PermissionEnum.SYSTEM_MANAGEMENT),
    DETECTION_MANAGEMENT_QUARTZ_JOB("任务管理", DETECTION_MANAGEMENT, "/template/module/detection/detection_quartz_job_list.json", PermissionEnum.DETECTION_MANAGEMENT_QUARTZ_JOB),
    DETECTION_MANAGEMENT_USER("用户管理", DETECTION_MANAGEMENT, "/template/module/detection/detection_user_list.json", PermissionEnum.DETECTION_MANAGEMENT_USER),
    DETECTION_MANAGEMENT_DBSOURCE("数据源管理", DETECTION_MANAGEMENT, "/template/module/detection/detection_dbsource_list.json", PermissionEnum.DETECTION_MANAGEMENT_DBSOURCE);


    private String name;
    private MenuEnum parent;
    private String configKey = "";
    private String path = "";
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

    MenuEnum(String name, MenuEnum parent, String configKey, PermissionEnum... permissions) {
        this.name = name;
        this.parent = parent;
        this.configKey = configKey;
        this.permissions = permissions;
    }

    MenuEnum(String name, MenuEnum parent, String configKey, String path, PermissionEnum... permissions) {
        this.name = name;
        this.parent = parent;
        this.configKey = configKey;
        this.path = path;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public MenuEnum getParent() {
        return parent;
    }

    public String getConfigKey() {
        return configKey;
    }

    public String getPath() {
        return path;
    }

    public PermissionEnum[] getPermissions() {
        return permissions;
    }
}
