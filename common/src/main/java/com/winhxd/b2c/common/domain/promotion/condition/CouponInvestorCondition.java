package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.common.PagedCondition;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @Author wl
 * @Date 2018/8/4 14:31
 * @Description
 **/
public class CouponInvestorCondition extends PagedCondition implements Serializable {
    @ApiModelProperty(value = "出资方规则id")
    private String id;
    @ApiModelProperty(value = "出资方规则编码")
    private String code;
    @ApiModelProperty(value = "出资方规则名称")
    private String name;
    @ApiModelProperty(value = "出资方名称")
    private String names;
    @ApiModelProperty(value = "备注")
    private String remarks;
    @ApiModelProperty(value = "状态 0 无效 1 有效")
    private Short status;
    @ApiModelProperty(value = "出资方详情列表")
    private List details;
    @ApiModelProperty(value = "当前操作人id")
    private String userId;
    @ApiModelProperty(value = "当前操作人名称")
    private String userName;

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

    public List getDetails() {
        return details;
    }

    public void setDetails(List details) {
        this.details = details;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }
}
