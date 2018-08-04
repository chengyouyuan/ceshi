package com.winhxd.b2c.common.domain.promotion.model;

import java.util.Date;
/**
 *
 *@Deccription 出资方规则
 *@User  wl
 *@Date   2018/8/4 17:22
 */
public class CouponInvestor {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 出资方规则编码
     */
    private String code;
    /**
     * 出资方名称
     */
    private String name;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 状态 0有效 1无效
     */
    private Short status;
    /**
     * 创建人id
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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