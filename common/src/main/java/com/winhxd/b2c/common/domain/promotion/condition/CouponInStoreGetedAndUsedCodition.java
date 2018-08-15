package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author wl
 * @Date 2018/8/13 16:37
 * @Description  查询门店下的优惠券使用情况 以及领取情况 统计
 **/
public class CouponInStoreGetedAndUsedCodition extends ApiCondition {
    @ApiModelProperty(value = "页号")
    private Integer pageNo;
    @ApiModelProperty(value = "页大小")
    private Long pageSize;


    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }
}
