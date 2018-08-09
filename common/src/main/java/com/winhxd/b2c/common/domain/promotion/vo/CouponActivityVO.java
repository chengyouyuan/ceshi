package com.winhxd.b2c.common.domain.promotion.vo;

import com.winhxd.b2c.common.domain.promotion.model.CouponActivityTemplate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 *
 * @author shijinxing
 * @date 2018/8/6
 */
@ApiModel("优惠券活动")
@Data
public class CouponActivityVO {

    @ApiModelProperty(value = "优惠券活动ID")
    private Long id;

    @ApiModelProperty(value = "活动编码")
    private String code;

    @ApiModelProperty(value = "活动名称")
    private String name;

    @ApiModelProperty(value = "活动说明")
    private String exolian;

    @ApiModelProperty(value = "活动备注")
    private String remarks;

    @ApiModelProperty(value = "1领券2推券")
    private Short type;

    @ApiModelProperty(value = "优惠券类型 1新用户注册 2老用户活动")
    private Short couponType;

    @ApiModelProperty(value = "活动状态1开启2停止")
    private Short activityStatus;

    @ApiModelProperty(value = "引用数量")
    private Integer citeNum;

    @ApiModelProperty(value = "配置可领券小店数量")
    private Integer storeNum;

    @ApiModelProperty(value = "优惠券数量的限制方式")
    private Short couponNumType;

    @ApiModelProperty(value = "优惠券数量的限制数量")
    private Integer couponNum;

    @ApiModelProperty(value = "己领取数量")
    private Integer getNum;

    @ApiModelProperty(value = "己使用数量")
    private Integer useNum;

    @ApiModelProperty(value = "己撤销优惠券数量")
    private Integer revocationNum;

    @ApiModelProperty(value = "活动开始时间")
    private Date activityStart;

    @ApiModelProperty(value = "活动结束时间")
    private Date activityEnd;

    @ApiModelProperty(value = "优惠券有效期开始时间")
    private Date couponStartTime;

    @ApiModelProperty(value = "优惠券有效期结束时间")
    private Date couponEndTime;

    @ApiModelProperty(value = "创建人")
    private String createdByName;

    @ApiModelProperty(value = "创建时间")
    private Date created;

    @ApiModelProperty(value = "修改人")
    private String updatedByName;

    @ApiModelProperty(value = "修改时间")
    private Date updated;

    @ApiModelProperty(value = "优惠券详情")
    private List<CouponActivityTemplate> couponActivityTemplateList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Short getCouponType() {
        return couponType;
    }

    public void setCouponType(Short couponType) {
        this.couponType = couponType;
    }

    public Short getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(Short activityStatus) {
        this.activityStatus = activityStatus;
    }

    public Integer getCiteNum() {
        return citeNum;
    }

    public void setCiteNum(Integer citeNum) {
        this.citeNum = citeNum;
    }

    public Integer getStoreNum() {
        return storeNum;
    }

    public void setStoreNum(Integer storeNum) {
        this.storeNum = storeNum;
    }

    public Short getCouponNumType() {
        return couponNumType;
    }

    public void setCouponNumType(Short couponNumType) {
        this.couponNumType = couponNumType;
    }

    public Integer getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(Integer couponNum) {
        this.couponNum = couponNum;
    }

    public Integer getGetNum() {
        return getNum;
    }

    public void setGetNum(Integer getNum) {
        this.getNum = getNum;
    }

    public Integer getUseNum() {
        return useNum;
    }

    public void setUseNum(Integer useNum) {
        this.useNum = useNum;
    }

    public Integer getRevocationNum() {
        return revocationNum;
    }

    public void setRevocationNum(Integer revocationNum) {
        this.revocationNum = revocationNum;
    }

    public Date getActivityStart() {
        return activityStart;
    }

    public void setActivityStart(Date activityStart) {
        this.activityStart = activityStart;
    }

    public Date getActivityEnd() {
        return activityEnd;
    }

    public void setActivityEnd(Date activityEnd) {
        this.activityEnd = activityEnd;
    }

    public Date getCouponStartTime() {
        return couponStartTime;
    }

    public void setCouponStartTime(Date couponStartTime) {
        this.couponStartTime = couponStartTime;
    }

    public Date getCouponEndTime() {
        return couponEndTime;
    }

    public void setCouponEndTime(Date couponEndTime) {
        this.couponEndTime = couponEndTime;
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

    public List<CouponActivityTemplate> getCouponActivityTemplateList() {
        return couponActivityTemplateList;
    }

    public void setCouponActivityTemplateList(List<CouponActivityTemplate> couponActivityTemplateList) {
        this.couponActivityTemplateList = couponActivityTemplateList;
    }
}
