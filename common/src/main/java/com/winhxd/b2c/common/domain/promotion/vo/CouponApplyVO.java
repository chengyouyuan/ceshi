package com.winhxd.b2c.common.domain.promotion.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Author wl
 * @Date 2018/8/9 12:16
 * @Description CouponApplyVO  适用对象页面用于页面显示
 **/
public class CouponApplyVO {
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "规则名称")
    private String name;
    @ApiModelProperty(value = "规则编码")
    private String code;
    @ApiModelProperty(value = "备注")
    private String remarks;
    @ApiModelProperty(value = "规则类型")
    private Short applyRuleType;
    @ApiModelProperty(value = "状态")
    private Short status;
    @ApiModelProperty(value = "创建人")
    private Long createdBy;
    @ApiModelProperty(value = "创建人名称")
    private String createdByName;
    @ApiModelProperty(value = "创建时间")
    private Date created;
    @ApiModelProperty(value = "修改人")
    private Long updatedBy;
    @ApiModelProperty(value = "修改人名称")
    private String updatedByName;
    @ApiModelProperty(value = "修改时间")
    private Date updated;
    @ApiModelProperty(value = "关联模板数量")
    private String relTempleteCount;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Short getApplyRuleType() {
        return applyRuleType;
    }

    public void setApplyRuleType(Short applyRuleType) {
        this.applyRuleType = applyRuleType;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getRelTempleteCount() {
        return relTempleteCount;
    }

    public void setRelTempleteCount(String relTempleteCount) {
        this.relTempleteCount = relTempleteCount;
    }
}
