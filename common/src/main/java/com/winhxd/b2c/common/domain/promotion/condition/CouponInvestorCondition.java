package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.base.condition.BaseCondition;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author wl
 * @Date 2018/8/4 14:31
 * @Description
 **/
public class CouponInvestorCondition extends BaseCondition implements Serializable {
    private String code;
    private String name;
    private String remarks;
    private Short status; // 0 无效 1 有效

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }


}
