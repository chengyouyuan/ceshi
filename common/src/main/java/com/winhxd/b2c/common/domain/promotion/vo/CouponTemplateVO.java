package com.winhxd.b2c.common.domain.promotion.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Author wl
 * @Date 2018/8/6 13:32
 * @Description  用于页面显示查询返回的数据信息
 **/
public class CouponTemplateVO {
    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "优惠券标题")
    private String title;
    @ApiModelProperty(value = "优惠券说明")
    private String exolian;
    @ApiModelProperty(value = "备注")
    private String remarks;
    @ApiModelProperty(value = "出资方规则ID")
    private Long investorId;
    @ApiModelProperty(value = "使用范围规则ID")
    private Long gradeId;
    @ApiModelProperty(value = "适用对象规则ID")
    private Long applyRuleId;
    @ApiModelProperty(value = "角标")
    private String corner;
    @ApiModelProperty(value = "优惠券标签")
    private String couponLabel;
    @ApiModelProperty(value = "优惠券标签颜色")
    private Integer couponLabelColor;
    @ApiModelProperty(value = "是否有效 0有效1无效")
    private Short status;
    @ApiModelProperty(value = "无效原因")
    private String reason;
    @ApiModelProperty(value = "优惠券模板编码(UUID）")
    private String code;
    @ApiModelProperty(value = "优惠券金额计算方式  1订单金额 2商品金额")
    private Short calType;
    @ApiModelProperty(value = "支付方式 1扫码支付 2线上支付")
    private Short payType;
    @ApiModelProperty(value = "创建人id")
    private Long createdBy;
    @ApiModelProperty(value = "创建人")
    private String createdByName;
    @ApiModelProperty(value = "创建时间(使用时间)")
    private Date created;
    @ApiModelProperty(value = "修改人id")
    private Long updatedBy;
    @ApiModelProperty(value = "修改人")
    private String updatedByName;
    @ApiModelProperty(value = "修改时间")
    private Date updated;
    @ApiModelProperty(value = "出资方规则名称")
    private String investorName;
    @ApiModelProperty(value = "优惠券优惠方式名称")
    private String gradeName;
    @ApiModelProperty(value = "优惠券类型规则名称")
    private String applyRuleName;
    @ApiModelProperty(value = "优惠券类型规则类型")
    private Short applyRuleType;
    @ApiModelProperty(value = "适用对象范围类型--转汉字")
    private String applyRuleTypeName;
    @ApiModelProperty(value = "计算方式--转汉字")
    private String calTypeName;
    @ApiModelProperty(value = "状态--转汉字")
    private String statusName;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExolian() {
        return exolian;
    }

    public void setExolian(String exolian) {
        this.exolian = exolian;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getInvestorId() {
        return investorId;
    }

    public void setInvestorId(Long investorId) {
        this.investorId = investorId;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public Long getApplyRuleId() {
        return applyRuleId;
    }

    public void setApplyRuleId(Long applyRuleId) {
        this.applyRuleId = applyRuleId;
    }

    public String getCorner() {
        return corner;
    }

    public void setCorner(String corner) {
        this.corner = corner;
    }

    public String getCouponLabel() {
        return couponLabel;
    }

    public void setCouponLabel(String couponLabel) {
        this.couponLabel = couponLabel;
    }

    public Integer getCouponLabelColor() {
        return couponLabelColor;
    }

    public void setCouponLabelColor(Integer couponLabelColor) {
        this.couponLabelColor = couponLabelColor;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Short getCalType() {
        return calType;
    }

    public void setCalType(Short calType) {
        this.calType = calType;
    }

    public Short getPayType() {
        return payType;
    }

    public void setPayType(Short payType) {
        this.payType = payType;
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

    public String getInvestorName() {
        return investorName;
    }

    public void setInvestorName(String investorName) {
        this.investorName = investorName;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getApplyRuleName() {
        return applyRuleName;
    }

    public void setApplyRuleName(String applyRuleName) {
        this.applyRuleName = applyRuleName;
    }

    public Short getApplyRuleType() {
        return applyRuleType;
    }

    public void setApplyRuleType(Short applyRuleType) {
        this.applyRuleType = applyRuleType;
    }

    public String getApplyRuleTypeName() {
        return applyRuleTypeName;
    }

    public void setApplyRuleTypeName(String applyRuleTypeName) {
        this.applyRuleTypeName = applyRuleTypeName;
    }

    public String getCalTypeName() {
        return calTypeName;
    }

    public void setCalTypeName(String calTypeName) {
        this.calTypeName = calTypeName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
