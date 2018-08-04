package com.winhxd.b2c.common.domain.promotion.model;

import java.util.Date;

/**
 *
 *@Deccription 优惠券模板类
 *@User  wl
 *@Date   2018/8/4 17:09
 */
public class CouponTemplate {
    /**
     * 主键
     */
    private Long id;
    /**
     * 优惠券标题
     */
    private String title;
    /**
     * 优惠券说明
     */
    private String exolian;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 出资方规则ID
     */
    private Long investorId;
    /**
     * 使用范围规则ID
     */
    private Long useRangeId;
    /**
     * 适用对象规则ID
     */
    private Long applyRuleId;
    /**
     * 角标
     */
    private String corner;
    /**
     * 优惠券标签
     */
    private String couponLabel;
    /**
     * 优惠券标签颜色
     */
    private Integer couponLabelColor;
    /**
     * 是否有效 0有效1无效
     */
    private Short status;
    /**
     * 无效原因
     */
    private String reason;
    /**
     * 优惠券模板编码(UUID）
     */
    private String code;
    /**
     * 优惠券金额计算方式  1订单金额 2商品金额
     */
    private Short calType;
    /**
     *支付方式 1扫码支付 2线上支付
     */
    private String payType;
    /**
     * '创建人id
     */
    private Long createdBy;
    /**
     * 创建人
     */
    private String createdByName;
    /**
     * 创建时间(使用时间)
     */
    private Date created;
    /**
     * 修改人id
     */
    private Long updateBy;
    /**
     * 修改人
     */
    private String updatedByName;
    /**
     * 修改时间
     */
    private Date updated;

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

    public Long getUseRangeId() {
        return useRangeId;
    }

    public void setUseRangeId(Long useRangeId) {
        this.useRangeId = useRangeId;
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

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
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

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
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
}