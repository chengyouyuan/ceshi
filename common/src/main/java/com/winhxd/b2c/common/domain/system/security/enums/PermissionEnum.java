package com.winhxd.b2c.common.domain.system.security.enums;

/**
 * 权限
 * @author lixiaodong
 */
public enum PermissionEnum {

    AUTHENTICATED("已登录验证"),

    /**
     * 订单管理
     */
    ORDER_MANAGEMENT("订单管理"),
    ORDER_MANAGEMENT_LIST("订单查询", ORDER_MANAGEMENT),
    ORDER_MANAGEMENT_EDIT("订单编辑", ORDER_MANAGEMENT),

    /***
     * 顾客管理
     */
    CUSTOMER_MANAGEMENT("顾客管理"),
    CUSTOMER_MANAGEMENT_LIST("顾客列表查询",CUSTOMER_MANAGEMENT),
    CUSTOMER_MANAGEMENT_EXPORT("顾客列表导出",CUSTOMER_MANAGEMENT),
    CUSTOMER_MANAGEMENT_BLACK("顾客黑名单权限",CUSTOMER_MANAGEMENT),
    CUSTOMER_MANAGEMENT_DETAIL("顾客详情",CUSTOMER_MANAGEMENT),
    CUSTOMER_MANAGEMENT_ORDER_DETAIL("顾客订单详情",CUSTOMER_MANAGEMENT),
    CUSTOMER_MANAGEMENT_UN_BIND("顾客解绑", CUSTOMER_MANAGEMENT),
    CUSTOMER_MANAGEMENT_CHANGE_BIND("顾客换绑", CUSTOMER_MANAGEMENT),

    /***
     * 消息管理
     */
    MESSAGE_MANAGEMENT("消息管理"),
    MESSAGE_MANAGEMENT_LIST("消息推送列表查询",MESSAGE_MANAGEMENT),

    /**
     * 商品管理
     */
	PROD_MANAGEMENT("商品管理"),
	PROD_MANAGEMENT_STORE("店铺商品管理",PROD_MANAGEMENT),
	PROD_MANAGEMENT_STORE_PUTAWAY("店铺商品上下架",PROD_MANAGEMENT_STORE),
	
	PROD_MANAGEMENT_SUBMIT("店铺提报商品管理",PROD_MANAGEMENT),
	PROD_MANAGEMENT_SUBMIT_AUDIT("审核店铺提报商品",PROD_MANAGEMENT_SUBMIT),
	PROD_MANAGEMENT_SUBMIT_ADD_PROD("店铺提报商品添加商品",PROD_MANAGEMENT_SUBMIT),

	/**
	 *  促销管理
	 */
	PROMOTION_MANAGEMENT("促销管理"),
    PROMOTION_INVESTOR_RULE("出资方规则",PROMOTION_MANAGEMENT),
    PROMOTION_GRADE_RULE("坎级规则",PROMOTION_MANAGEMENT),
    PROMOTION_APPLY_RULE("适用对象规则",PROMOTION_MANAGEMENT),
    PROMOTION_TEMPLETE_MANAGEMENT("模板管理",PROMOTION_MANAGEMENT),
    PROMOTION_ACTIVITY_PULL("领券管理",PROMOTION_MANAGEMENT),
    PROMOTION_ACTIVITY_PUSH("推券管理",PROMOTION_MANAGEMENT),

    PROMOTION_INVESTOR_RULE_LIST("出资方规则列表查询",PROMOTION_INVESTOR_RULE),
    PROMOTION_INVESTOR_RULE_ADD("出资方规则添加",PROMOTION_INVESTOR_RULE),
    PROMOTION_INVESTOR_RULE_LIST_VIEW("出资方规则查看",PROMOTION_INVESTOR_RULE_LIST),
    PROMOTION_INVESTOR_RULE_LIST_REL("出资方规则关联模板数",PROMOTION_INVESTOR_RULE_LIST),

    PROMOTION_GRADE_RULE_LIST("坎级规则列表查询",PROMOTION_GRADE_RULE),
    PROMOTION_GRADE_RULE_ADD("坎级规则添加",PROMOTION_GRADE_RULE),
    PROMOTION_GRADE_RULE_LIST_VIEW("坎级规则列表查看",PROMOTION_GRADE_RULE_LIST),
    PROMOTION_GRADE_RULE_LIST_REL("坎级规则关联模板数量",PROMOTION_GRADE_RULE_LIST),

    PROMOTION_APPLY_RULE_LIST("适用对象规则列表查询",PROMOTION_APPLY_RULE),
    PROMOTION_APPLY_RULE_ADD("适用对象规则添加",PROMOTION_APPLY_RULE),
    PROMOTION_APPLY_RULE_LIST_VIEW("适用对象规则列表查看",PROMOTION_APPLY_RULE_LIST),
    PROMOTION_APPLY_RULE_LIST_REL("适用对象规则关联模板数量",PROMOTION_APPLY_RULE_LIST),

    PROMOTION_TEMPLETE_MANAGEMENT_LIST("优惠券模板列表查询",PROMOTION_TEMPLETE_MANAGEMENT),
    PROMOTION_TEMPLETE_MANAGEMENT_ADD("优惠券模板添加",PROMOTION_TEMPLETE_MANAGEMENT),
    PROMOTION_TEMPLETE_MANAGEMENT_LIST_VIEW("优惠券模板列表查看",PROMOTION_TEMPLETE_MANAGEMENT_LIST),

	PROMOTION_ACTIVITY_PULL_LIST("优惠券活动领券列表查询",PROMOTION_ACTIVITY_PULL),
	PROMOTION_ACTIVITY_PULL_ADD("优惠券活动领券列表添加",PROMOTION_ACTIVITY_PULL),
    PROMOTION_ACTIVITY_PULL_VIEW("优惠券活动领券查看详情",PROMOTION_ACTIVITY_PULL_LIST),
    PROMOTION_ACTIVITY_PULL_TEMPLATE_VIEW("领券引用信息",PROMOTION_ACTIVITY_PULL_LIST),
    PROMOTION_ACTIVITY_PULL_UPDATE_STATUS("领券停止优惠券活动",PROMOTION_ACTIVITY_PULL_LIST),
    PROMOTION_ACTIVITY_PULL_REVOCATION("领券撤销优惠券",PROMOTION_ACTIVITY_PULL_LIST),

	PROMOTION_ACTIVITY_PUSH_LIST("优惠券活动推券列表查询",PROMOTION_ACTIVITY_PUSH),
    PROMOTION_ACTIVITY_PUSH_ADD("优惠券活动推券列表添加",PROMOTION_ACTIVITY_PUSH),
    PROMOTION_ACTIVITY_PUSH_VIEW("优惠券活动推券查看详情",PROMOTION_ACTIVITY_PUSH_LIST),
    PROMOTION_ACTIVITY_PUSH_TEMPLATE_VIEW("推券引用信息",PROMOTION_ACTIVITY_PUSH_LIST),
    PROMOTION_ACTIVITY_PUSH_UPDATE_STATUS("推券停止优惠券活动",PROMOTION_ACTIVITY_PUSH_LIST),
    PROMOTION_ACTIVITY_PUSH_REVOCATION("推券撤销优惠券",PROMOTION_ACTIVITY_PUSH_LIST),

    /**
     * 门店管理
     */
    STORE_MANAGEMENT("门店管理"),
    STORE_MANAGEMENT_REGION("门店测试区域管理", STORE_MANAGEMENT),
    STORE_MANAGEMENT_REGION_LIST("门店测试区域列表", STORE_MANAGEMENT_REGION),
    STORE_MANAGEMENT_REGION_ADD("门店测试区域添加", STORE_MANAGEMENT_REGION),
    STORE_MANAGEMENT_REGION_DEL("门店测试区域删除", STORE_MANAGEMENT_REGION),
    STORE_MANAGEMENT_STORE("门店管理", STORE_MANAGEMENT),
    STORE_MANAGEMENT_STORE_EDIT("门店管理编辑", STORE_MANAGEMENT_STORE),

    /**
     * 财务管理
     */
    VERIFY_MANAGEMENT("财务管理"),
    VERIFY_MANAGEMENT_LIST("财务管理", VERIFY_MANAGEMENT),
    VERIFY_MANAGEMENT_SUMMARY_LIST("待结算金额审核", VERIFY_MANAGEMENT),
    VERIFY_MANAGEMENT_DETAIL_LIST("待结算费用明细", VERIFY_MANAGEMENT),
    VERIFY_MANAGEMENT_WITHDRAWALS_LIST("提款申请", VERIFY_MANAGEMENT),
    VERIFY_MANAGEMENT_VERIFY("结算", VERIFY_MANAGEMENT),

    /***
     * 系统管理
     */
    SYSTEM_MANAGEMENT("系统管理"),
    SYSTEM_MANAGEMENT_ROLE("权限组管理", SYSTEM_MANAGEMENT),
    SYSTEM_MANAGEMENT_ROLE_ADD("新增权限组", SYSTEM_MANAGEMENT_ROLE),
    SYSTEM_MANAGEMENT_ROLE_EDIT("编辑权限组", SYSTEM_MANAGEMENT_ROLE),
    SYSTEM_MANAGEMENT_ROLE_DELETE("删除权限组", SYSTEM_MANAGEMENT_ROLE),
    SYSTEM_MANAGEMENT_USER("成员管理", SYSTEM_MANAGEMENT),
    SYSTEM_MANAGEMENT_USER_ADD("新增成员", SYSTEM_MANAGEMENT_USER),
    SYSTEM_MANAGEMENT_USER_EDIT("编辑成员", SYSTEM_MANAGEMENT_USER),
    SYSTEM_MANAGEMENT_USER_DELETE("删除成员", SYSTEM_MANAGEMENT_USER),
    SYSTEM_MANAGEMENT_USER_ENABLE("启用成员", SYSTEM_MANAGEMENT_USER),
    SYSTEM_MANAGEMENT_DICT("字典管理", SYSTEM_MANAGEMENT),
    SYSTEM_MANAGEMENT_DICT_ADD("新增字典", SYSTEM_MANAGEMENT_DICT),
    SYSTEM_MANAGEMENT_DICT_EDIT("编辑字典", SYSTEM_MANAGEMENT_DICT),
    SYSTEM_MANAGEMENT_DICT_DELETE("删除字典", SYSTEM_MANAGEMENT_DICT),

    /**
     * 监测监控管理
     */
    DETECTION_MANAGEMENT("监控管理"),
    DETECTION_MANAGEMENT_QUARTZ_JOB("任务管理",DETECTION_MANAGEMENT),
    DETECTION_MANAGEMENT_USER("用户管理",DETECTION_MANAGEMENT),
    DETECTION_MANAGEMENT_DBSOURCE("数据源管理",DETECTION_MANAGEMENT);

    private String name;
    private PermissionEnum parent;

    PermissionEnum(String name) {
        this.name = name;
    }

    PermissionEnum(String name, PermissionEnum parent) {
        this.name = name;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public PermissionEnum getParent() {
        return parent;
    }
}
