package com.winhxd.b2c.common.domain.promotion.condition;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author wl
 * @Date 2018/8/17 12:18
 * @Description
 **/
public class CouponSetToValidCondition {
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "用户名称")
    private String userName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
