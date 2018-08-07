package com.winhxd.b2c.common.domain.promotion.condition;

import com.winhxd.b2c.common.domain.base.condition.BaseCondition;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author wl
 * @Date 2018/8/6 09:56
 * @Description
 **/
public class CouponTemplateCondition extends BaseCondition implements Serializable {
    @ApiModelProperty(value = "主键")
    private String id;
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
    @ApiModelProperty(value = "优惠券标签")
    private String couponLabel;
    @ApiModelProperty(value = "优惠券金额计算方式  1订单金额 2商品金额")
    private Short calType;
    @ApiModelProperty(value = "支付方式 1扫码支付 2线上支付")
    private Short payType;

    @ApiModelProperty(value = "优惠券类型")
    private Short applyRuleType;
    @ApiModelProperty(value = "是否有效 0有效1无效")
    private Short status;
    @ApiModelProperty(value = "优惠券模板编码(UUID")
    private String code;
    @ApiModelProperty(value = "出资方规则名称")
    private String investorName;
    @ApiModelProperty(value = "优惠券优惠方式名称")
    private String gradeName;
    @ApiModelProperty(value = "优惠券类型规则名称")
    private String applyRuleName;
    @ApiModelProperty(value = "创建人")
    private String createdByName;

    @ApiModelProperty(value = "创建时间 开始")
    private String createdBegin;
    @ApiModelProperty(value = "创建时间 结束")
    private String createdEnd;


    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getCouponLabel() {
        return couponLabel;
    }

    public void setCouponLabel(String couponLabel) {
        this.couponLabel = couponLabel;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getCreatedBegin() {
        return createdBegin;
    }

    public void setCreatedBegin(String createdBegin) {
        this.createdBegin = createdBegin;
    }

    public String getCreatedEnd() {
        return createdEnd;
    }

    public void setCreatedEnd(String createdEnd) {
        this.createdEnd = createdEnd;
    }
}
