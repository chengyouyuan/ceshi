package com.winhxd.b2c.common.domain.promotion.enums;


/**
 * @Author wl
 * @Date 2018/8/6 10:10
 * @Description  优惠券模板相关枚举类
 **/
public enum CouponTemplateEnum {
    /**
     * 优惠券金额计算方式
     */
    ORDER_CALTYPE((short) 1, "订单金额"),
    PROD_CALTYPE((short) 2, "商品金额"),
    /**
     * 优惠券模板是否有效  0有效 1无效
     */
    EFFICTIVE((short) 1, "有效"),
    VALIDATE((short) 0, "无效"),

    /**
     * 支付方式
     */
    SCAN_CODE_PAY((short) 1,"订单金额"),
    ONLINE_PAY((short) 2, "订单金额"),

    PERCENT_NOT_EQ100((short) 1,"占比之和不等于100"),
    INVERSTOR_REPEAT((short) 2,"出资方重复"),
    INVERSTOR_DETAIL_ISNULL((short) 3,"出资方明细为空"),
    /**
     * 出资方类型
     */
    INVERTOR_H_TYPE((short) 1, "惠下单"),
    INVERTOR_S_TYPE((short) 2, "品牌商");

    private short code;
    private String desc;

    CouponTemplateEnum(short code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public short getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }

}
