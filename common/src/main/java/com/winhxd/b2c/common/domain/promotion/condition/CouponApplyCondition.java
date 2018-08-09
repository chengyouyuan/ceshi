package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.base.condition.BaseCondition;

import java.io.Serializable;

/**
 * @Author wl
 * @Date 2018/8/9 11:03
 * @Description
 **/
public class CouponApplyCondition extends BaseCondition implements Serializable {
    private String name;

    private String code;

    private String remarks;

    private Short applyRuleType;

    private Short status;




}
