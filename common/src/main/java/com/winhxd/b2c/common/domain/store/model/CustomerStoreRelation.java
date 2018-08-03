package com.winhxd.b2c.common.domain.store.model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Description: 用户商品关系绑定表
 * @author chengyy
 * @date 2018/8/3 10:10
 */
@ApiModel("用户商品关系绑定表")
public class CustomerStoreRelation {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("用户id主键")
    private Long customerId;

    @ApiModelProperty("商店id主键")
    private Long storeUserId;

    @ApiModelProperty("绑定时间")
    private Date bindingTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getStoreUserId() {
        return storeUserId;
    }

    public void setStoreUserId(Long storeUserId) {
        this.storeUserId = storeUserId;
    }

    public Date getBindingTime() {
        return bindingTime;
    }

    public void setBindingTime(Date bindingTime) {
        this.bindingTime = bindingTime;
    }
}